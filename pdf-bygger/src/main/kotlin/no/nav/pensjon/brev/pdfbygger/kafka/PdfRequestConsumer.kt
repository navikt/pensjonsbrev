package no.nav.pensjon.brev.pdfbygger.kafka

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.model.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.pdfByggerObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Instant
import kotlin.collections.partition
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val RETRY_INTERVAL_SECONDS = 150L

private val POLL_SIZE_MULTIPLIER = 5

private val QUEUE_READ_TIMEOUT = 5.seconds.toJavaDuration()

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService, properties: Map<String, String>,
    topic: String,
    private val retryTopic: String,
) {
    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")

    private val consumer = KafkaConsumer<String, PDFRequest>(
        properties
            .plus("max.poll.records" to parallelism * POLL_SIZE_MULTIPLIER),
        StringDeserializer(),
        PDFRequestDeserializer()
    )

    private val retryConsumer =
        KafkaConsumer<String, PDFRequest>(
            properties
                .plus("max.poll.records" to parallelism),
            StringDeserializer(),
            PDFRequestDeserializer()
        )

    // we need one producer per partition to enable idempotent writing based on the incoming partition
    private val retryProducer =
        KafkaProducer<String, PDFRequest>(
            properties,
            StringSerializer(),
            PDFRequestSerializer()
        )

    init {
        consumer.subscribe(listOf(topic))
        retryConsumer.subscribe(listOf(retryTopic))
    }

    private var nextRetry: Instant = Instant.now()

    fun flow() =
        flow {
            while (true) {
                if (shouldPollFromRetryQueue()) {
                    nextRetry = Instant.now()
                    emit(pollRenderRetryQueue())
                    println("Polled message from render retry queue")
                } else {
                    emit(pollRenderQueue())
                    println("Polled message render queue")
                }
            }
        }.onEach { renderRequests ->
            val rendered = renderLetters(renderRequests)
            if(renderRequests.isFromRetryQueue) {
                processRetryQueueResults(rendered)
            } else {
                processRenderQueueResults(rendered)
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

    private fun processRenderQueueResults(renderResults: List<RenderResult>) {
        val (successes, failures) = renderResults.partition { it.response is PDFCompilationResponse.Base64PDF}
        failures.forEach { retryProducer.send(ProducerRecord(it.record.key(), it.record.value())) }
        sendToReplyTopic(successes)
    }

    private fun processRetryQueueResults(renderResults: List<RenderResult>) {

        val offsets = renderResults.groupBy { it.record.partition() }
            .map { (partition, partitionResults) ->
                val sortedPartitionResults = partitionResults.sortedBy { it.record.offset() }
                val firstFailure = sortedPartitionResults.firstOrNull { it.response is PDFCompilationResponse.Failure }

                // sets offset in partition to latest failure such that it will be re-processed
                val offset = firstFailure?.record?.offset()
                    ?: (sortedPartitionResults.last().record.offset() + 1)

                TopicPartition(retryTopic, partition) to OffsetAndMetadata(offset)
            }.toMap()

        sendToReplyTopic(renderResults.filter { it.response is PDFCompilationResponse.Base64PDF })
        retryConsumer.commitSync(offsets)
    }

    private suspend fun renderLetters(renderRequests: RenderRequests): List<RenderResult> {
        return coroutineScope {
            return@coroutineScope renderRequests.requests.map { record ->
                async {
                    RenderResult(compile(record.value()), record)
                }
            }.awaitAll()
        }
    }

    private fun sendToReplyTopic(renderResults: List<RenderResult>) {
        println("Rendered ${renderResults.size} PDFs successfully")
        // TODO actually send to reply topic.
    }

    private suspend fun compile(request: PDFRequest): PDFCompilationResponse {
        parallelismSemaphore.acquire()
        val result = LatexDocumentRenderer.render(request)
            .let { latexCompileService.createLetter(it.base64EncodedFiles()) }
        parallelismSemaphore.release()
        println("Compiled pdf successfully")
        return result
    }

    private fun shouldPollFromRetryQueue() =
        nextRetry.plusSeconds(RETRY_INTERVAL_SECONDS) < Instant.now()

}

private class PDFRequestDeserializer : Deserializer<PDFRequest> {
    private val mapper = pdfByggerObjectMapper()
    override fun deserialize(topic: String?, data: ByteArray): PDFRequest =
        mapper.readValue(data, PDFRequest::class.java)
}

private class PDFRequestSerializer : Serializer<PDFRequest> {
    private val mapper = pdfByggerObjectMapper()
    override fun serialize(topic: String?, data: PDFRequest): ByteArray = mapper.writeValueAsBytes(data)
}

private data class RenderRequests(
    val requests: ConsumerRecords<String, PDFRequest>,
    val isFromRetryQueue: Boolean,
)

private data class RenderResult(
    val response: PDFCompilationResponse,
    val record: ConsumerRecord<String, PDFRequest>
)