package no.nav.pensjon.brev.pdfbygger.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.model.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.model.PdfCompilationInput
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class PdfRequestConsumer(
    private val latexCompileService: LatexCompileService, properties: Map<String, String?>,
    topic: String,
) {

    private val consumer =
        KafkaConsumer<String, PdfCompilationInput>(properties, StringDeserializer(), PdfCompilationInputDeserializer())

    private val parallelism = Runtime.getRuntime().availableProcessors() * 2
    private val parallelismSemaphore = parallelism.takeIf { it > 0 }?.let { Semaphore(it) }
        ?: throw IllegalStateException("Not enough cores to run async worker")

    init {
        consumer.subscribe(listOf(topic))
    }

    private fun poll() =
        consumer.poll(3.seconds.toJavaDuration())
            .map { it.value() }

    fun flow() =
        flow {
            while (true) {
                emit(poll())
            }
        }.onEach { input ->
            val result = input.map { compile(it) }
            // TODO retry logic?
            // TODO remove debug logging
            result.forEach {
                println("PDF compilation finished with result: $it")
            }

            // TODO write to passed return queue
        }.onEach {
            consumer.commitAsync()
        }

    private suspend fun compile(files: PdfCompilationInput): PDFCompilationResponse {
        parallelismSemaphore.acquire()
        val result = latexCompileService.createLetter(files.files)
        parallelismSemaphore.release()
        return result
    }

}

private class PdfCompilationInputDeserializer :
    org.apache.kafka.common.serialization.Deserializer<PdfCompilationInput> {
    private val mapper = jacksonObjectMapper()

    override fun deserialize(topic: String?, data: ByteArray): PdfCompilationInput {
        return mapper.readValue(data, PdfCompilationInput::class.java)
    }
}