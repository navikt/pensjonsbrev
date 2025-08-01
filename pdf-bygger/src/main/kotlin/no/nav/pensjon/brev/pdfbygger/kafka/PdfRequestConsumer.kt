package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.server.config.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import no.nav.brev.brevbaker.AsyncPDFCompilationOutput
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.getProperty
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.mdc
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
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

// Consumer will be timed out after 5 minutes if no polls occur
private val QUEUE_READ_TIMEOUT = 1.seconds.toJavaDuration()

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService,
    kafkaConfig: ApplicationConfig,
    private val renderTopic: String,
) {
    private val logger = LoggerFactory.getLogger(PdfRequestConsumer::class.java)

    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")

    private val replyProducers = Producers<AsyncPDFCompilationOutput>(createKafkaProducerConfig(kafkaConfig))

    private val pdfEncoder = Base64.getEncoder()
    private lateinit var consumerJob: Job

    private val consumer = KafkaConsumer(
        createKafkaConsumerConfig(kafkaConfig, parallelism),
        StringDeserializer(),
        PDFRequestAsyncDeserializer()
    )

    private val flowDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val flowScope = CoroutineScope(flowDispatcher)

    fun start() {
        consumerJob = flowScope.launch {
            pollFlow()
                .filter { !it.isEmpty }
                .collect { produceResultsForTopic(renderLetters(it)) }
        }
    }

    fun stop() {
        consumerJob.cancel("Shutting down kafka consumer")
        flowScope.cancel("Shutting down")
    }


    private fun pollFlow(): Flow<ConsumerRecords<String, PDFRequestAsync>> = flow {
        try {
            while (currentCoroutineContext().isActive) {
                emit(consumer.poll(QUEUE_READ_TIMEOUT))
            }
        } finally {
            logger.info("Closing consumers and producers")
            consumer.close()
            replyProducers.closeAll()
            logger.info("Closed consumers and producers")
        }
    }

    private fun produceResultsForTopic(renderResults: List<RenderResult>) {
        renderResults.groupBy { it.consumedPartiton }
            .forEach { (partition, results) ->
                val replyProducer = replyProducers.getProducer(renderTopic, partition)
                val consumedFrom = TopicPartition(renderTopic, partition)
                val newOffset = OffsetAndMetadata(results.maxOf { it.consumedOffset } + 1)
                try {
                    replyProducer.beginTransaction()
                    replyProducer.sendOffsetsToTransaction(
                        mapOf(consumedFrom to newOffset),
                        consumer.groupMetadata()
                    )

                    results.forEach { result ->
                        val error = errorMessage(result)
                        mdc("messageId" to result.renderRequest.messageId) {
                            replyProducer.send(
                                ProducerRecord(
                                    result.renderRequest.replyTopic,
                                    result.renderRequest.messageId,
                                    AsyncPDFCompilationOutput(
                                        base64PDF = base64PDForNull(result),
                                        error = error?.also { logger.error(it) },
                                    )
                                )
                            )
                        }
                    }
                    replyProducer.commitTransaction()
                } catch (e: Exception) {
                    logger.error("Failed to commit message to transaction with exception: ${e.message}. Aborting transaction.")
                    replyProducer.abortTransaction()
                }
            }
    }

    private fun errorMessage(result: RenderResult): String? = when (val response = result.pDFCompilationResponse) {
        is PDFCompilationResponse.Failure.QueueTimeout -> response.reason
        is PDFCompilationResponse.Failure.Timeout -> result.pDFCompilationResponse.reason
        is PDFCompilationResponse.Failure.Server -> result.pDFCompilationResponse.reason
        is PDFCompilationResponse.Failure.Client -> {
            """${response.reason}
                |PDF Compilation error:
                | ${response.error ?: ""}
                |PDF Compilation output:
                | ${response.output ?: ""}
            """.trimMargin()
        }

        else -> null
    }

    private fun base64PDForNull(result: RenderResult): String? =
        (result.pDFCompilationResponse as? PDFCompilationResponse.Success)
            ?.pdfCompilationOutput?.bytes?.let { pdfEncoder.encodeToString(it) }

    private suspend fun renderLetters(
        renderRequests: ConsumerRecords<String, PDFRequestAsync>,
    ): List<RenderResult> =
        coroutineScope {
            renderRequests.map { record ->
                async {
                    RenderResult(
                        renderRequest = record.value(),
                        pDFCompilationResponse = compile(record.value()),
                        consumedTopic = record.topic(),
                        consumedPartiton = record.partition(),
                        consumedOffset = record.offset(),
                    )
                }
            }.awaitAll()
        }

    private suspend fun compile(request: PDFRequestAsync): PDFCompilationResponse =
        parallelismSemaphore.withPermit {
            LatexDocumentRenderer.render(request.request)
                .let { latexCompileService.createLetter(it.files) }
        }

}

private class PDFRequestAsyncDeserializer : Deserializer<PDFRequestAsync> {
    private val mapper = pdfByggerObjectMapper()
    override fun deserialize(topic: String?, data: ByteArray): PDFRequestAsync =
        mapper.readValue(data, PDFRequestAsync::class.java)
}

private class PDFByggerSerializer<T> : Serializer<T> {
    private val mapper = pdfByggerObjectMapper()
    override fun serialize(topic: String?, data: T): ByteArray = mapper.writeValueAsBytes(data)
}

private fun transactionId(topic: String, partition: Int) = "$topic-$partition"

private data class RenderResult(
    val renderRequest: PDFRequestAsync,
    val pDFCompilationResponse: PDFCompilationResponse,
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

private class RebalanceListener(
    private vararg val producers: Producers<*>,
) : ConsumerRebalanceListener {
    private val logger = LoggerFactory.getLogger(RebalanceListener::class.java)
    override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
        if (partitions != null) {
            producers.forEach {
                runBlocking {
                    it.closeProducersForTopicPartitions(partitions)
                }
            }
            logger.info("Revoked ${partitions.size} partitions")
        }
    }

    override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
        if (partitions != null) {
            producers.forEach {
                runBlocking {
                    it.createProducersForTopicPartitions(partitions)
                }
            }
            logger.info("Assigned ${partitions.size} partitions")
        }
    }


}

private class Producers<V>(val producerConfig: Map<String, String>) {

    private val logger = LoggerFactory.getLogger(Producers::class.java)
    val producers = ConcurrentHashMap<String, KafkaProducer<String, V>>()
    suspend fun closeProducersForTopicPartitions(partitions: MutableCollection<TopicPartition>) {
        logger.info("Closing producers")
        coroutineScope {
            partitions.map {
                launch {
                    producers.remove(transactionId(it.topic(), it.partition()))?.close()
                }
            }.joinAll()
        }
    }

    suspend fun createProducersForTopicPartitions(partitions: MutableCollection<TopicPartition>) {
        coroutineScope {
            partitions
                .chunked(10) // Don't create them too fast, or the cluster won't like you.
                .forEach { topicPartitions ->
                    topicPartitions.map { topicPartition ->
                        @OptIn(DelicateCoroutinesApi::class)
                        GlobalScope.launch {
                            val transactionId = transactionId(topicPartition.topic(), topicPartition.partition())
                            producers.getOrPut(transactionId) {
                                KafkaProducer(
                                    producerConfig
                                        .plus("transactional.id" to transactionId),
                                    StringSerializer(),
                                    PDFByggerSerializer<V>(),
                                ).also { it.initTransactions() }
                            }
                        }
                    }.joinAll()
                }
        }
    }

    fun getProducer(consumedTopic: String, consumedPartition: Int): KafkaProducer<String, V> =
        producers[transactionId(consumedTopic, consumedPartition)]
            ?: throw IllegalArgumentException("There was no producer found for topic $consumedTopic and partition $consumedPartition")

    fun closeAll() = producers.forEach { it.value.close() }
}