package no.nav.pensjon.brev.pdfbygger.kafka

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService,
    private val properties: Map<String, String>,
    private val topic: String,
    private val retryTopic: String,
) {
    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")
    private val retryProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFRequestAsync>>()
    private val successProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFCompilationResponse.Base64PDF>>()
    private var shuttingDown = false

    private val logger = LoggerFactory.getLogger(PdfRequestConsumer::class.java)

    // TODO add message id to logger
    // TODO should i close producers when repartitioning occurs? It should in theory just be a waste of threads/ a tiny bit of RAM
    private val consumer = KafkaConsumer(
        properties
            .plus("max.poll.records" to parallelism),
        StringDeserializer(),
        PDFRequestAsyncDeserializer()
    )

    private val retryConsumer =
        KafkaConsumer(
            properties
                .plus("max.poll.records" to parallelism),
            StringDeserializer(),
            PDFRequestAsyncDeserializer()
        )

    init {
        consumer.subscribe(listOf(topic))
        retryConsumer.subscribe(listOf(retryTopic))
    }

    private var nextRetryPoll: Instant = Instant.MIN

    fun flow() =
        flow {
            while (!shuttingDown) {
                if (Instant.now() > nextRetryPoll && !shuttingDown) {
                    println("Polling message from render retry queue")
                    val retryMessages = pollRenderRetryQueue()
                    if (retryMessages.requests.isEmpty) {
                        nextRetryPoll = Instant.now().plus(FAILURE_RETRY_INTERVAL_SECONDS)
                    } else {
                        emit(retryMessages)
                    }
                }
                if (!shuttingDown) {
                    println("Polling message render queue")
                    emit(pollRenderQueue())
                }
            }
        }.onEach { renderRequests ->
            val results = renderLetters(renderRequests)
            if (results.isNotEmpty()) {
                val (successes, failures) = results.partition { it.pdf != null }

                if (renderRequests.isFromRetryQueue) {
                    println("Test: got ${successes.size} successes and ${failures.size} failures from retry queue")
                    if (failures.isEmpty()) {
                        println("Test: rendered ${successes.size} from retry queue")
                    } else {
                        processRetryQueueResults(results, retryConsumer.groupMetadata())
                    }
                } else {
                    if (failures.isEmpty()) {
                        println("Test: rendered ${successes.size} from normal queue without failures")
                    } else {
                        sendToRetryOrSuccessQueue(results, consumer.groupMetadata())
                    }
                }
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
                    val producer = getOrCreateSuccessProducer(topic, it.consumedPartiton)
                    sendSingleSynchronous(it, it.renderRequest.replyTopic, it.pdf, producer, groupMetadata )
                } else {
                    val producer = getOrCreateRetryProducer(it.consumedTopic, it.consumedPartiton)
                    sendSingleSynchronous(it, retryTopic, it.renderRequest, producer, groupMetadata )
                }
            }
    }

    private fun <Value> sendSingleSynchronous(
        it: RenderResult,
        topic: String,
        value: Value,
        producer: KafkaProducer<String, Value>,
        groupMetadata: ConsumerGroupMetadata
    ) {
        val offsetAndMetadata = OffsetAndMetadata(it.consumedOffset + 1)
        val topicPartition = TopicPartition(it.consumedTopic, it.consumedPartiton)
        try {
            producer.beginTransaction()
            producer.sendOffsetsToTransaction(mapOf(topicPartition to offsetAndMetadata), groupMetadata)
            producer.send(ProducerRecord(topic, value))
            producer.commitTransaction()
        } catch (e: Exception) {
            producer.abortTransaction()
            throw QueueCommitFailure("Failed to commit pdf to reply queue", e)
        }
    }

    private fun processRetryQueueResults(results: List<RenderResult>, groupMetadata: ConsumerGroupMetadata) {
        var anySucceess = false
        results.groupBy { it.consumedPartiton }
            .mapNotNull { (partition, partitionResults) ->
                val sortedPartitionResults = partitionResults.sortedBy { it.consumedOffset }
                val successesBeforeFailure = sortedPartitionResults.takeWhile { it.pdf != null }

                if (successesBeforeFailure.isNotEmpty()) {
                    anySucceess = true
                    val successProducer = getOrCreateSuccessProducer(retryTopic, partition)
                    val consumedFrom = TopicPartition(retryTopic, partition)
                    val newOffset = OffsetAndMetadata(successesBeforeFailure.last().consumedOffset + 1)

                    successProducer.beginTransaction()
                    successProducer.sendOffsetsToTransaction(mapOf(consumedFrom to newOffset), groupMetadata)
                    successesBeforeFailure.forEach {
                        successProducer.send(ProducerRecord(it.renderRequest.replyTopic, it.pdf!!))
                    }
                    successProducer.commitTransaction()
                }
            }//.toMap()

        if (!anySucceess) {
            nextRetryPoll = Instant.now().plus(FAILURE_RETRY_INTERVAL_SECONDS)
        }
    }


    private fun groupResultsPerTransaction(results: List<RenderResult>) {
        // TODO split based on consumed topic, consumed partition and success topic.
        val resultsPerConsumedTopic = results.groupBy { it.consumedTopic }.map { it.value }
        val mapresultsPerConsumedTopicAndPartition = resultsPerConsumedTopic
            .map { perConsumedTopicResults -> perConsumedTopicResults.groupBy { it.consumedPartiton } }
            .flatMap { it.values }

        mapresultsPerConsumedTopicAndPartition
            .map { results ->
                val sorted = results.sortedBy { it.consumedOffset }
                // TODO group by adjacent success topic
            }
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
        val transactionId = "$consumedTopic-$consumedPartition"
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
        val transactionId = "$consumedTopic-$consumedPartition"
        return successProducers.getOrPut(transactionId) {
            KafkaProducer(
                properties
                    .plus("transactional.id" to transactionId),
                StringSerializer(),
                Base64PDFSerializer(),
            ).also { it.initTransactions() }
        }
    }


    fun stop() {
        shuttingDown = true
        consumer.close()
        retryConsumer.close()
        retryProducers.forEach { (_, producer) -> producer.close() }
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