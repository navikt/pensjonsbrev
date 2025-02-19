package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService

fun Application.kafkaModule(latexCompileService: LatexCompileService) {
    val config = environment.config.config("pdfBygger.kafka")
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
        kafkaConfig = config,
        renderTopic = config.property("topic").getString(),
        retryTopic = config.property("retryTopic").getString(),
    )

    pdfRequestConsumer.start()
    monitor.subscribe(ApplicationStopPreparing) {
        log.info("Shutting down async worker")
        pdfRequestConsumer.stop()
    }

}