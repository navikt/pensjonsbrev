package no.nav.pensjon.brev.latex

import io.ktor.server.config.*
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer

class LatexAsyncCompilerService(
    kafkaConfig: ApplicationConfig,
) {
    private val topic = kafkaConfig.property("topic").getString()
    private val producer =
        KafkaProducer(
            createKafkaConfig(kafkaConfig),
            StringSerializer(),
            PDFRequestSerializer()
        )

    fun renderAsync(asyncPdfRequest: PDFRequestAsync) {
        producer.send(ProducerRecord(topic, asyncPdfRequest))
        producer.flush();
    }

}

private class PDFRequestSerializer : Serializer<PDFRequestAsync> {
    private val mapper = brevbakerJacksonObjectMapper()

    override fun serialize(topic: String, data: PDFRequestAsync): ByteArray {
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