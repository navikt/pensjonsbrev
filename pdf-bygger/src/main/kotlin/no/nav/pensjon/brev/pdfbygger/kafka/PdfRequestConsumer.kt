package no.nav.pensjon.brev.pdfbygger.kafka

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.model.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.pdfByggerObjectMapper
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import kotlin.jvm.java
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService, properties: Map<String, String?>,
    topic: String,
) {

    private val consumer =
        KafkaConsumer<String, PDFRequest>(properties, StringDeserializer(), PDFRequestDeserializer())

    private val parallelism = Runtime.getRuntime().availableProcessors()
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")

    init {
        consumer.subscribe(listOf(topic))
    }

    private fun poll() =
        consumer.poll(5.seconds.toJavaDuration())
            .map { it.value() }

    fun flow() =
        flow {
            while (true) {
                val workInQueue =  parallelism - parallelismSemaphore.availablePermits
                if(workInQueue < parallelism / 2) {
                    emit(poll())
                    println("Polled message")
                } else {
                    delay(500.milliseconds)
                    println("Still working on ${parallelismSemaphore.availablePermits} requests. No need to poll.")
                }
            }
        }.onEach { input ->
            coroutineScope {
                input.map { async { compile(it) } }.awaitAll()
            }
        }.onEach {
            consumer.commitAsync()
        }

    private suspend fun compile(request: PDFRequest): PDFCompilationResponse {
        parallelismSemaphore.acquire()
        val result = LatexDocumentRenderer.render(request)
            .let { latexCompileService.createLetter(it.base64EncodedFiles()) }
        parallelismSemaphore.release()
        println("Compiled pdf successfully")
        return result
    }

}

private class PDFRequestDeserializer :
    org.apache.kafka.common.serialization.Deserializer<PDFRequest> {
    private val mapper = pdfByggerObjectMapper()

    override fun deserialize(topic: String?, data: ByteArray): PDFRequest {
        return mapper.readValue(data, PDFRequest::class.java)
    }
}