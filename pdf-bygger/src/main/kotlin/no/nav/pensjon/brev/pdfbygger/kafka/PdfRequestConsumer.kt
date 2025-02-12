package no.nav.pensjon.brev.pdfbygger.kafka

import kotlinx.coroutines.*
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

// Consumer will be timed out after 5 minutes if no polls occur
private val FAILURE_RETRY_INTERVAL_SECONDS = 120.seconds.toJavaDuration()


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
    private var shuttingDown = false

    // TODO should i close producers when repartitioning occurs? It should in theory just be a waste of threads/ a tiny bit of RAM
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
                val (successes, failures) = results.partition { it.renderResult is PDFCompilationResponse.Base64PDF }

                if (renderRequests.isFromRetryQueue) {
                    println("Test: got ${successes.size} successes and ${failures.size} failures from retry queue")
                    if (failures.isEmpty()) {
                        println("Test: rendered ${successes.size} from retry queue")
                    } else {
                        println("Test: rendered ${successes.size} from retry queue")
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
                        val topicPartition = TopicPartition(it.consumedTopic, it.consumedPartiton)

                        try {
                            producer.beginTransaction()
                            producer.sendOffsetsToTransaction(mapOf(topicPartition to offsetAndMetadata), groupMetadata)
                            producer.send(ProducerRecord(retryTopic, it.key, it.renderRequest))
                            println("Test: sendt a request to failure queue successfully.")
                            producer.commitTransaction()
                        } catch (e: Exception) {
                            // TODO pass only certain exceptions along to crash the application.
                            producer.abortTransaction()
                            throw QueueCommitFailure("Failed to commit result to retry queue", e)
                        }
                    }
                }
            }
    }

    private fun processRetryQueueResults(results: List<RenderResult>, groupMetadata: ConsumerGroupMetadata) {
        // send all to
        var anySucceess = false
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
                //        groupMetadata
                //    )
                //    successesBeforeFailure.forEach {
                //        anySucceess = true
                //        // TODO produce successes to topic
                //    }
                //    producer.commitTransaction()
                //}


            }//.toMap()

        if (!anySucceess) {
            nextRetryPoll = Instant.now().plus(FAILURE_RETRY_INTERVAL_SECONDS)
        }

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

    fun stop() {
        shuttingDown = true
        consumer.close()
        retryConsumer.close()
        retryProducers.forEach { (_, producer) -> producer.close() }
    }
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

private class QueueCommitFailure(message: String, e: Throwable) : Exception(message, e)