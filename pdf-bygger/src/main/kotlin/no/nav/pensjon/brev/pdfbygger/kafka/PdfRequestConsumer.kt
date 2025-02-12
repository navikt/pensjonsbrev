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
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.partition
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val FAILURE_RETRY_INTERVAL_SECONDS = 150L


private val QUEUE_READ_TIMEOUT = 5.seconds.toJavaDuration()

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService,
    private val properties: Map<String, String>,
    topic: String,
    private val retryTopic: String,
) {
    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")
    private val retryProducers = ConcurrentHashMap<String, KafkaProducer<String, PDFRequest>>()

    private val consumer = KafkaConsumer<String, PDFRequest>(
        properties
            .plus("max.poll.records" to parallelism),
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

    init {
        consumer.subscribe(listOf(topic))
        retryConsumer.subscribe(listOf(retryTopic))
    }

    private var lastRetryFailure: Instant = Instant.MIN
    private var lastRetrySucceeded = false

    fun flow() =
        flow {
            while (true) {
                // TODO prioritize between retry and success queue polling.
                // maybe it's ok to just do every second one?
                if (shouldPollFromRetryQueue()) {
                    emit(pollRenderRetryQueue())
                    println("Polled message from render retry queue")
                } else {
                    emit(pollRenderQueue())
                    println("Polled message render queue")
                }
            }
        }.onEach { renderRequests ->
            val results = renderLetters(renderRequests)
            if (results.isNotEmpty()) {
                val (successes, failures) = results.partition { it.renderResult is PDFCompilationResponse.Base64PDF }

                if (renderRequests.isFromRetryQueue) {
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
                when (it.renderResult) {
                    is PDFCompilationResponse.Base64PDF -> {
                        println("Test: rendered a letter successfully that would be committed synchronously")
                    }

                    is PDFCompilationResponse.Failure -> {
                        val producer = getOrCreateRetryProducer(it.consumedTopic, it.consumedPartiton)
                        val offsetAndMetadata = OffsetAndMetadata(it.consumedOffset + 1)
                        val topicPartition = TopicPartition(
                            it.consumedTopic,
                            it.consumedPartiton
                        )
                        producer.beginTransaction()
                        producer.sendOffsetsToTransaction(mapOf(topicPartition to offsetAndMetadata), groupMetadata)
                        producer.send(ProducerRecord(retryTopic, it.key, it.renderRequest))
                        println("Test: sendt a request to failure queue successfully.")
                        producer.commitTransaction()
                        // TODO error handling on failed transactions.
                    }
                }
            }
    }

    private fun processRetryQueueResults(results: List<RenderResult>, groupMetadata: ConsumerGroupMetadata) {
        // send all to
        results.groupBy { it.consumedPartiton }
            .mapNotNull { (partition, partitionResults) ->
                val sortedPartitionResults = partitionResults.sortedBy { it.consumedOffset }
                val successesBeforeFailure =
                    sortedPartitionResults.takeWhile { it.renderResult is PDFCompilationResponse.Base64PDF }

                val firstResult = partitionResults.first()
                //val producer = getOrCreateRetryProducer(firstResult.consumedTopic, firstResult.consumedPartiton)

                //if (successesBeforeFailure.isNotEmpty()) {
                //    val consumedFrom = TopicPartition(retryTopic, partition)
                //    val newOffset = OffsetAndMetadata(successesBeforeFailure.last().consumedOffset + 1)
                //    producer.beginTransaction()
                //    producer.sendOffsetsToTransaction(
                //        mapOf(consumedFrom to newOffset),
                //        consumedWithMetadata
                //    )
                //    successesBeforeFailure.forEach {
                //        // TODO produce successes
                //    }
                //}


            }//.toMap()

    }

    private suspend fun renderLetters(
        renderRequests: RenderRequests,
    ): List<RenderResult> =
        coroutineScope {
            renderRequests.requests.map { record ->
                async {
                    RenderResult(
                        key = record.key(),
                        renderRequest = record.value(),
                        renderResult = compile(record.value()),
                        consumedTopic = record.topic(),
                        consumedPartiton = record.partition(),
                        consumedOffset = record.offset(),
                    )
                }
            }.awaitAll()
        }

    private suspend fun compile(request: PDFRequest): PDFCompilationResponse {
        parallelismSemaphore.acquire()
        val result = LatexDocumentRenderer.render(request)
            .let { latexCompileService.createLetter(it.files) }
        parallelismSemaphore.release()
        println("Compiled pdf successfully")
        return result
    }

    private fun getOrCreateRetryProducer(
        consumedTopic: String,
        consumedPartition: Int
    ): KafkaProducer<String, PDFRequest> {
        val key = "$consumedTopic-$consumedPartition"
        return retryProducers.getOrPut(key) {
            KafkaProducer<String, PDFRequest>(
                properties
                    .plus("transactional.id" to key)
                    .plus("enable.idempotence" to "true"),
                StringSerializer(),
                PDFRequestSerializer(),
            ).also { it.initTransactions() }
        }
    }

    private fun shouldPollFromRetryQueue() =


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

data class RenderRequests(
    val requests: ConsumerRecords<String, PDFRequest>,
    val isFromRetryQueue: Boolean,
)


private data class RenderResult(
    val key: String,
    val renderRequest: PDFRequest,
    val renderResult: PDFCompilationResponse,
    val consumedTopic: String,
    val consumedPartiton: Int,
    val consumedOffset: Long,
)

private enum class Topic {
    RETRY,
    RENDER,
}