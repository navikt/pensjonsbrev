package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import no.nav.pensjon.brev.pdfbygger.getProperty
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService

fun Application.kafkaModule(latexCompileService: LatexCompileService) {
    val config = environment.config.config("pdfBygger.kafka")
    val kafkaConfig = createKafkaConfig(config)

    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }
    }

    val pdfRequestConsumer = PdfRequestConsumer(
        latexCompileService = latexCompileService,
        properties = kafkaConfig,
        topic = config.property("topic").getString(),
        retryTopic = config.property("retryTopic").getString(),
    )

    @OptIn(DelicateCoroutinesApi::class)
    pdfRequestConsumer.flow().launchIn(GlobalScope)
}

private fun createKafkaConfig(kafkaConfig: ApplicationConfig): Map<String, String?> = mapOf(
    "bootstrap.servers" to kafkaConfig.getProperty("bootstrap.servers"),
    "security.protocol" to "SSL",
    "ssl.keystore.type" to "PKCS12",
    "ssl.keystore.location" to kafkaConfig.getProperty("ssl.keystore.location"),
    "ssl.keystore.password" to kafkaConfig.getProperty("ssl.keystore.password"),
    "ssl.key.password" to kafkaConfig.getProperty("ssl.key.password"),
    "ssl.truststore.type" to "JKS",
    "ssl.truststore.location" to kafkaConfig.getProperty("ssl.truststore.location"),
    "ssl.truststore.password" to kafkaConfig.getProperty("ssl.truststore.password"),
    "group.id" to "pdf-bygger-async",
    "enable.auto.commit" to "false",
)