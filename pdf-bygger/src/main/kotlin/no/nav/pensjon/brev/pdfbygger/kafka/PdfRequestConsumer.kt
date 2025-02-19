package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.server.config.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.pdfbygger.getProperty
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
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

// Consumer will be timed out after 5 minutes if no polls occur
private val QUEUE_READ_TIMEOUT = 120.seconds.toJavaDuration()
private val RETRY_QUEUE_READ_TIMEOUT = 1.seconds.toJavaDuration()

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService,
    kafkaConfig: ApplicationConfig,
    private val renderTopic: String,
    private val retryTopic: String,
) {
    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")
    private val retryProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFRequestAsync>>()
    private val successProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFCompilationResponse.Base64PDF>>()
    private var shuttingDown = false
    private val consumerConfig = createKafkaConsumerConfig(kafkaConfig, parallelism)
    private val producerConfig = createKafkaProducerConfig(kafkaConfig)
    private lateinit var flow: Flow<RenderRequests>
    private lateinit var consumerJob: Job

    private val logger = LoggerFactory.getLogger(PdfRequestConsumer::class.java)

    // TODO add message id to logger
    // TODO housekeeping on repartition and possibly interval
    // TODO should i close producers when repartitioning occurs? It should in theory just be a waste of threads/ a tiny bit of RAM
    private val consumer = KafkaConsumer(consumerConfig, StringDeserializer(), PDFRequestAsyncDeserializer())

    private val retryConsumer =
        KafkaConsumer(consumerConfig, StringDeserializer(), PDFRequestAsyncDeserializer())

    fun start() {
        consumer.subscribe(listOf(renderTopic))
        retryConsumer.subscribe(listOf(retryTopic))
        flow = flow()
        @OptIn(DelicateCoroutinesApi::class)
        consumerJob = flow.launchIn(GlobalScope)
    }

    fun stop() {
        shuttingDown = true
    }

    fun flow() =
        flow {
            while (true) {
                logger.info("Polling render retry queue")
                emit(pollRenderRetryQueue())
                logger.info("Polling render queue")
                emit(pollRenderQueue())
            }
        }.takeWhile { !shuttingDown }
            .filter { !it.requests.isEmpty }
            .onEach { renderRequests ->
                logger.info("rendering ${renderRequests.requests.count()} requests")
                val results = renderLetters(renderRequests)
                if (results.isNotEmpty()) {
                    val (successes, failures) = results.partition { it.pdf != null }
                    val consumedTopic = renderRequests.topic()

                    if (failures.isEmpty()) {
                        produceSuccessesForTopic(successes, consumedTopic, renderRequests.isFromRetryQueue)
                    } else {
                        if (renderRequests.isFromRetryQueue) {
                            processRetryQueueResults(results)
                        } else {
                            sendToRetryOrSuccessQueue(results)
                        }
                    }
                    logger.info("Produced ${successes.size} successes and ${failures.size} failures")
                }
            }.onCompletion {
                logger.info("Closing consumers and producers")
                consumer.close()
                retryConsumer.close()
                retryProducers.forEach { it.value.close() }
                successProducers.forEach { it.value.close() }
            }

    private fun produceSuccessesForTopic(
        successes: List<RenderResult>, consumedTopic: String, isFromRetryQueue: Boolean
    ) {
        successes.groupBy { it.consumedPartiton }
            .forEach { (partition, results) ->
                produceSuccessesForTopicAndPartition(partition, results, isFromRetryQueue, consumedTopic)
            }
    }

    private fun pollRenderQueue(): RenderRequests = RenderRequests(
        requests = consumer.poll(QUEUE_READ_TIMEOUT),
        isFromRetryQueue = false
    )

    private fun pollRenderRetryQueue(): RenderRequests = RenderRequests(
        requests = retryConsumer.poll(RETRY_QUEUE_READ_TIMEOUT),
        isFromRetryQueue = true
    )

    private fun sendToRetryOrSuccessQueue(results: List<RenderResult>) {
        results
            .sortedBy { it.consumedOffset } // make sure higher offsets are not committed before lower ones
            .forEach {
                if (it.pdf != null) {
                    val producer = getOrCreateSuccessProducer(renderTopic, it.consumedPartiton)
                    sendSingleSynchronous(it, it.renderRequest.replyTopic, it.pdf, producer, consumer.groupMetadata())
                } else {
                    val producer = getOrCreateRetryProducer(it.consumedTopic, it.consumedPartiton)
                    sendSingleSynchronous(it, retryTopic, it.renderRequest, producer, consumer.groupMetadata())
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
            logger.error("Failed to commit message to transaction. Aborting transaction.")
            producer.abortTransaction()
        }
    }

    private fun processRetryQueueResults(results: List<RenderResult>) {
        results.groupBy { it.consumedPartiton }
            .mapNotNull { (partition, partitionResults) ->
                val successesBeforeFailure = partitionResults
                    .sortedBy { it.consumedOffset }
                    .takeWhile { it.pdf != null }

                if (successesBeforeFailure.isNotEmpty()) {
                    produceSuccessesForTopicAndPartition(partition, successesBeforeFailure, true, retryTopic)
                }
            }
    }

    private fun produceSuccessesForTopicAndPartition(
        consumedPartition: Int,
        results: List<RenderResult>,
        isFromRetryQueue: Boolean,
        consumedTopic: String
    ) {
        val successProducer = getOrCreateSuccessProducer(consumedTopic, consumedPartition)
        val consumedFrom = TopicPartition(consumedTopic, consumedPartition)
        val newOffset = OffsetAndMetadata(results.maxOf { it.consumedOffset } + 1)
        try {
            successProducer.beginTransaction()
            successProducer.sendOffsetsToTransaction(
                mapOf(consumedFrom to newOffset),
                consumerGroupMetadata(isFromRetryQueue)
            )
            results.forEach {
                successProducer.send(ProducerRecord(it.renderRequest.replyTopic, it.renderRequest.messageId, it.pdf!!))
            }
            successProducer.commitTransaction()
        } catch (e: Exception) {
            logger.error("Failed to commit message to transaction. Aborting transaction.")
            successProducer.abortTransaction()
        }

    }

    private fun consumerGroupMetadata(isFromRetryQueue: Boolean): ConsumerGroupMetadata? =
        if (isFromRetryQueue) retryConsumer.groupMetadata() else consumer.groupMetadata()

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
                producerConfig
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
                producerConfig
                    .plus("transactional.id" to transactionId),
                StringSerializer(),
                Base64PDFSerializer(),
            ).also { it.initTransactions() }
        }
    }

    private fun transactionId(topic: String, partition: Int) = "$topic-$partition"

    /*
        private fun cleanUpProducers(topicPartition: MutableCollection<TopicPartition>) {
            topicPartition.forEach {
                retryProducers[transactionId(it.topic(), it.partition())]?.close()
                successProducers[transactionId(it.topic(), it.partition())]?.close()
            }
        }
    */

    private fun RenderRequests.topic() =
        if (isFromRetryQueue) {
            retryTopic
        } else {
            renderTopic
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

private fun createKafkaConfig(kafkaConfig: ApplicationConfig): Map<String, String> = mapOf(
    "bootstrap.servers" to kafkaConfig.getProperty("bootstrap.servers"),
    "security.protocol" to "SSL",
    "ssl.keystore.type" to "PKCS12",
    "ssl.keystore.location" to kafkaConfig.getProperty("ssl.keystore.location"),
    "ssl.keystore.password" to kafkaConfig.getProperty("ssl.keystore.password"),
    "ssl.key.password" to kafkaConfig.getProperty("ssl.key.password"),
    "ssl.truststore.type" to "JKS",
    "ssl.truststore.location" to kafkaConfig.getProperty("ssl.truststore.location"),
    "ssl.truststore.password" to kafkaConfig.getProperty("ssl.truststore.password"),
)

private fun createKafkaConsumerConfig(kafkaConfig: ApplicationConfig, paralellism: Int) =
    createKafkaConfig(kafkaConfig).plus(
        mapOf(
            "enable.auto.commit" to "false",
            "group.id" to kafkaConfig.getProperty("group.id"),
            "auto.offset.reset" to "earliest",
            "auto.create.topics.enable" to "false",
            "max.poll.records" to paralellism,
            "session.timeout.ms" to "120000",
            "heartbeat.interval.ms" to "30000",
        )
    )

private fun createKafkaProducerConfig(kafkaConfig: ApplicationConfig) = createKafkaConfig(kafkaConfig).plus(
    mapOf(
        "enable.idempotence" to "true"
    )
)