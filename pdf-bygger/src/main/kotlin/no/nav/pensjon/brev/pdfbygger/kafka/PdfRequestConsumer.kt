package no.nav.pensjon.brev.pdfbygger.kafka

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.model.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.pdfByggerObjectMapper
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.partition
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

// Consumer will be timed out after 5 minutes if no polls occur
private val FAILURE_RETRY_INTERVAL_SECONDS = 120.seconds.toJavaDuration()
private val QUEUE_READ_TIMEOUT = 5.seconds.toJavaDuration()
private const val SEQUENTIAL_WORK_SIZE = 3

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService,
    private val properties: Map<String, String>,
    private val renderTopic: String,
    private val retryTopic: String,
) {
    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")
    private val retryProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFRequestAsync>>()
    private val successProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFCompilationResponse.Base64PDF>>()
    private var shuttingDown = false
    private lateinit var flow: Flow<RenderRequests>

    private val logger = LoggerFactory.getLogger(PdfRequestConsumer::class.java)

    // TODO add message id to logger
    // TODO housekeeping on repartition and possibly interval
    // TODO should i close producers when repartitioning occurs? It should in theory just be a waste of threads/ a tiny bit of RAM
    private val consumer = KafkaConsumer(
        properties.plus("max.poll.records" to parallelism * SEQUENTIAL_WORK_SIZE),
        StringDeserializer(),
        PDFRequestAsyncDeserializer()
    )

    private val retryConsumer =
        KafkaConsumer(
            properties.plus("max.poll.records" to parallelism),
            StringDeserializer(),
            PDFRequestAsyncDeserializer()
        )

    private var nextRetryPoll: Instant = Instant.MIN

    fun start() {
        consumer.subscribe(listOf(renderTopic), PartitonRevokedListener { cleanUpProducers(it) })
        retryConsumer.subscribe(listOf(retryTopic), PartitonRevokedListener { cleanUpProducers(it) })
        flow = flow()
        @OptIn(DelicateCoroutinesApi::class)
        flow.launchIn(GlobalScope)
    }

    fun stop() {
        shuttingDown = true
    }

    fun flow() =
        flow {
            while (true) {
                if (Instant.now() > nextRetryPoll) {
                    println("Polling message from render retry queue")
                    val retryMessages = pollRenderRetryQueue()
                    if (retryMessages.requests.isEmpty) {
                        nextRetryPoll = Instant.now().plus(FAILURE_RETRY_INTERVAL_SECONDS)
                    } else {
                        emit(retryMessages)
                    }
                }
                println("Polling message render queue")
                emit(pollRenderQueue())
            }
        }.takeWhile {
            !shuttingDown
        }.onEach { renderRequests ->
            val results = renderLetters(renderRequests)
            if (results.isNotEmpty()) {
                val (successes, failures) = results.partition { it.pdf != null }
                val consumedTopic = renderRequests.topic()
                val consumerGroupMetadata = renderRequests.groupMetadata()

                if (failures.isEmpty()) {
                    produceSuccessesForTopic(successes, consumedTopic, consumerGroupMetadata)
                } else {
                    if (renderRequests.isFromRetryQueue) {
                        processRetryQueueResults(results, consumerGroupMetadata)
                    } else {
                        sendToRetryOrSuccessQueue(results, consumerGroupMetadata)
                    }
                }
            }
        }.onCompletion {
            logger.info("Closing consumers and producers")
            consumer.close()
            retryConsumer.close()
            retryProducers.forEach { it.value.close() }
            successProducers.forEach { it.value.close() }
        }

    private fun produceSuccessesForTopic(
        successes: List<RenderResult>, consumedTopic: String, consumerGroupMetadata: ConsumerGroupMetadata
    ) {
        successes.groupBy { it.consumedPartiton }
            .forEach { (partition, results) ->
                produceSuccessesForTopicAndPartition(partition, results, consumerGroupMetadata, consumedTopic)
            }
    }

    private fun pollRenderQueue(): RenderRequests = RenderRequests(
        requests = consumer.poll(QUEUE_READ_TIMEOUT),
        isFromRetryQueue = false
    )

    private fun pollRenderRetryQueue(): RenderRequests = RenderRequests(
        requests = retryConsumer.poll(QUEUE_READ_TIMEOUT),
        isFromRetryQueue = true
    )

    private fun sendToRetryOrSuccessQueue(results: List<RenderResult>, groupMetadata: ConsumerGroupMetadata) {
        results
            .sortedBy { it.consumedOffset } // make sure higher offsets are not committed before lower ones
            .forEach {
                if (it.pdf != null) {
                    val producer = getOrCreateSuccessProducer(renderTopic, it.consumedPartiton)
                    sendSingleSynchronous(it, it.renderRequest.replyTopic, it.pdf, producer, groupMetadata)
                } else {
                    val producer = getOrCreateRetryProducer(it.consumedTopic, it.consumedPartiton)
                    sendSingleSynchronous(it, retryTopic, it.renderRequest, producer, groupMetadata)
                }
            }
    }

    private fun <Value> sendSingleSynchronous(
        result: RenderResult,
        topic: String,
        value: Value,
        producer: KafkaProducer<String, Value>,
        groupMetadata: ConsumerGroupMetadata,
    ) {
        val offsetAndMetadata = OffsetAndMetadata(result.consumedOffset + 1)
        val topicPartition = TopicPartition(result.consumedTopic, result.consumedPartiton)
        try {
            producer.beginTransaction()
            producer.sendOffsetsToTransaction(mapOf(topicPartition to offsetAndMetadata), groupMetadata)
            producer.send(ProducerRecord(topic, result.renderRequest.messageId, value))
            producer.commitTransaction()
        } catch (e: Exception) {
            producer.abortTransaction()
            throw QueueCommitFailure("Failed to commit pdf to reply queue", e)
        }
    }

    private fun processRetryQueueResults(results: List<RenderResult>, groupMetadata: ConsumerGroupMetadata) {
        var onlyFailures = true
        results.groupBy { it.consumedPartiton }
            .mapNotNull { (partition, partitionResults) ->
                val successesBeforeFailure = partitionResults
                    .sortedBy { it.consumedOffset }
                    .takeWhile { it.pdf != null }

                if (successesBeforeFailure.isNotEmpty()) {
                    onlyFailures = false
                    produceSuccessesForTopicAndPartition(partition, successesBeforeFailure, groupMetadata, retryTopic)
                }
            }

        if (onlyFailures) {
            nextRetryPoll = Instant.now().plus(FAILURE_RETRY_INTERVAL_SECONDS)
        }
    }

    private fun produceSuccessesForTopicAndPartition(
        consumedPartition: Int,
        results: List<RenderResult>,
        groupMetadata: ConsumerGroupMetadata,
        consumedTopic: String
    ) {
        val successProducer = getOrCreateSuccessProducer(consumedTopic, consumedPartition)
        val consumedFrom = TopicPartition(consumedTopic, consumedPartition)
        val newOffset = OffsetAndMetadata(results.maxOf { it.consumedOffset } + 1)

        successProducer.beginTransaction()
        successProducer.sendOffsetsToTransaction(mapOf(consumedFrom to newOffset), groupMetadata)
        results.forEach {
            successProducer.send(ProducerRecord(it.renderRequest.replyTopic, it.renderRequest.messageId, it.pdf!!))
        }
        successProducer.commitTransaction()
    }

    private suspend fun renderLetters(
        renderRequests: RenderRequests,
    ): List<RenderResult> =
        coroutineScope {
            renderRequests.requests.map { record ->
                async {
                    RenderResult(
                        renderRequest = record.value(),
                        pdf = compile(record.value()),
                        consumedTopic = record.topic(),
                        consumedPartiton = record.partition(),
                        consumedOffset = record.offset(),
                    )
                }
            }.awaitAll()
        }

    private suspend fun compile(request: PDFRequestAsync): PDFCompilationResponse.Base64PDF? {
        parallelismSemaphore.acquire()
        val result = LatexDocumentRenderer.render(request.request)
            .let { latexCompileService.createLetter(it.files) }
        parallelismSemaphore.release()

        when (result) {
            is PDFCompilationResponse.Base64PDF -> return result
            is PDFCompilationResponse.Failure.Client -> logger.error(result.reason) // TODO better logging
            is PDFCompilationResponse.Failure.QueueTimeout -> logger.error(result.reason)
            is PDFCompilationResponse.Failure.Server -> logger.error(result.reason)
            is PDFCompilationResponse.Failure.Timeout -> logger.error(result.reason)
        }
        return null
    }

    private fun getOrCreateRetryProducer(
        consumedTopic: String,
        consumedPartition: Int
    ): KafkaProducer<String, PDFRequestAsync> {
        val transactionId = transactionId(consumedTopic, consumedPartition)
        return retryProducers.getOrPut(transactionId) {
            KafkaProducer(
                properties
                    .plus("transactional.id" to transactionId),
                StringSerializer(),
                PDFRequestAsyncSerializer(),
            ).also { it.initTransactions() }
        }
    }

    private fun getOrCreateSuccessProducer(
        consumedTopic: String,
        consumedPartition: Int
    ): KafkaProducer<String, PDFCompilationResponse.Base64PDF> {
        val transactionId = transactionId(consumedTopic, consumedPartition)
        return successProducers.getOrPut(transactionId) {
            KafkaProducer(
                properties
                    .plus("transactional.id" to transactionId),
                StringSerializer(),
                Base64PDFSerializer(),
            ).also { it.initTransactions() }
        }
    }

    private fun transactionId(topic: String, partition: Int) = "$topic-$partition"

    private fun cleanUpProducers(topicPartition: MutableCollection<TopicPartition>) {
        topicPartition.forEach {
            retryProducers[transactionId(it.topic(), it.partition())]?.close()
            successProducers[transactionId(it.topic(), it.partition())]?.close()
        }
    }

    private fun RenderRequests.topic() =
        if (isFromRetryQueue) {
            retryTopic
        } else {
            renderTopic
        }

    private fun RenderRequests.groupMetadata() =
        if (isFromRetryQueue) {
            retryConsumer.groupMetadata()
        } else {
            consumer.groupMetadata()
        }
}

private class PDFRequestAsyncDeserializer : Deserializer<PDFRequestAsync> {
    private val mapper = pdfByggerObjectMapper()
    override fun deserialize(topic: String?, data: ByteArray): PDFRequestAsync =
        mapper.readValue(data, PDFRequestAsync::class.java)
}

private class PDFRequestAsyncSerializer : Serializer<PDFRequestAsync> {
    private val mapper = pdfByggerObjectMapper()
    override fun serialize(topic: String?, data: PDFRequestAsync): ByteArray = mapper.writeValueAsBytes(data)
}

private class Base64PDFSerializer : Serializer<PDFCompilationResponse.Base64PDF> {
    private val mapper = pdfByggerObjectMapper()
    override fun serialize(topic: String?, data: PDFCompilationResponse.Base64PDF): ByteArray =
        mapper.writeValueAsBytes(data)
}


data class RenderRequests(
    val requests: ConsumerRecords<String, PDFRequestAsync>,
    val isFromRetryQueue: Boolean,
)


private data class RenderResult(
    val renderRequest: PDFRequestAsync,
    val pdf: PDFCompilationResponse.Base64PDF?,
    val consumedTopic: String,
    val consumedPartiton: Int,
    val consumedOffset: Long,
)

private class QueueCommitFailure(message: String, e: Throwable) : Exception(message, e)

private class PartitonRevokedListener(val onRevoked: (MutableCollection<TopicPartition>) -> Unit) :
    ConsumerRebalanceListener {
    override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>) {
        onRevoked(partitions)
    }

    override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>) {}

}