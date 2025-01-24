package no.nav.pensjon.brev.pdfbygger

import io.ktor.server.application.*
import io.ktor.server.netty.*
import no.nav.pensjon.brev.pdfbygger.api.restModule
import no.nav.pensjon.brev.pdfbygger.kafka.kafkaModule


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.getProperty(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

@Suppress("unused")
fun Application.module() {
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    if (getProperty("pdfBygger.isAsyncWorker")?.toBoolean() == true) {
        kafkaModule()
    } else {
        restModule()
    }

}

