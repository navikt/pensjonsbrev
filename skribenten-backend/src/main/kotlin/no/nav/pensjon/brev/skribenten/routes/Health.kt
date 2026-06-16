package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.healthRoute() {
    val logger = LoggerFactory.getLogger(javaClass)

    get("/isAlive") {
        call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
    }

    get("/isReady") {
        call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
    }

    get("/isStarted") {
        logger.info("Kaller isStarted")
        call.respondText("Started!", ContentType.Text.Plain, HttpStatusCode.OK)
    }
}