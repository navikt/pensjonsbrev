package no.nav.pensjon.brev.pdfbygger.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.config.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.PdfCompilationInput
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class PdfRequestConsumer(kafkaConfig: ApplicationConfig, private val latexCompileService: LatexCompileService) {
    val properties = mapOf(
        "bootstrap.servers" to kafkaConfig.property("bootstrap.servers"),
        "security.protocol" to kafkaConfig.property("security.protocol"),
        "ssl.keystore.type" to kafkaConfig.property("ssl.keystore.type"),
        "ssl.keystore.location" to kafkaConfig.property("ssl.keystore.location"),
        "ssl.keystore.password" to kafkaConfig.property("ssl.keystore.password"),
        "ssl.key.password" to kafkaConfig.property("ssl.key.password"),
        "ssl.truststore.type" to kafkaConfig.property("ssl.truststore.type"),
        "ssl.truststore.location" to kafkaConfig.property("ssl.truststore.location"),
        "ssl.truststore.password" to kafkaConfig.property("ssl.truststore.password"),
        "max.poll.records" to kafkaConfig.property("ssl.truststore.password"),
    )

    private val consumer =
        KafkaConsumer<String, PdfCompilationInput>(properties, StringDeserializer(), PdfCompilationInputDeserializer())
    private val topic = kafkaConfig.property("topic").getString()
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