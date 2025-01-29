package no.nav.pensjon.brev.latex

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.config.*
import no.nav.pensjon.brev.template.render.LatexDocument
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer

class LatexAsyncCompilerService(
    kafkaConfig: ApplicationConfig,
) {
    private val topic = kafkaConfig.property("topic").getString()
    private val producer =
        KafkaProducer<String, PdfCompilationInput>(
            createKafkaConfig(kafkaConfig),
            StringSerializer(),
            PdfCompilationInputSerializer()
        )

    fun renderAsync(orderId: String, document: LatexDocument) {
        producer.send(
            ProducerRecord<String, PdfCompilationInput>(
                topic,
                orderId,
                PdfCompilationInput(document.base64EncodedFiles())
            )
        )
        producer.flush();
    }

}

private class PdfCompilationInputSerializer : Serializer<PdfCompilationInput> {
    private val mapper = jacksonObjectMapper()

    override fun serialize(topic: String, data: PdfCompilationInput): ByteArray {
        return mapper.writeValueAsBytes(data)
    }
}

private fun createKafkaConfig(kafkaConfig: ApplicationConfig): Map<String, String?> = mapOf(
    "bootstrap.servers" to kafkaConfig.property("bootstrap.servers").getString(),
    "security.protocol" to "SSL",
    "ssl.keystore.type" to "PKCS12",
    "ssl.keystore.location" to kafkaConfig.property("ssl.keystore.location").getString(),
    "ssl.keystore.password" to kafkaConfig.property("ssl.keystore.password").getString(),
    "ssl.key.password" to kafkaConfig.property("ssl.key.password").getString(),
    "ssl.truststore.type" to "JKS",
    "ssl.truststore.location" to kafkaConfig.property("ssl.truststore.location").getString(),
    "ssl.truststore.password" to kafkaConfig.property("ssl.truststore.password").getString(),
    "group.id" to "pdf-bygger-async",
)