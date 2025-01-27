package no.nav.pensjon.brev.pdfbygger

import io.ktor.server.application.*
import io.ktor.server.netty.*
import no.nav.pensjon.brev.pdfbygger.api.restModule
import no.nav.pensjon.brev.pdfbygger.kafka.kafkaModule
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.getProperty(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

@Suppress("unused")
fun Application.module() {
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    val latexCompileService = LatexCompileService(
        compileTimeout = getProperty("pdfBygger.latex.compileTimeout")?.let { Duration.parse(it) } ?: 300.seconds,
        latexCommand = getProperty("pdfBygger.latex.latexCommand") ?: "xelatex --interaction=nonstopmode -halt-on-error",
        tmpBaseDir = Path.of(environment.config.property("pdfBygger.latex.compileTmpDir").getString()),
    )

    if (getProperty("pdfBygger.isAsyncWorker")?.toBoolean() == true) {
        kafkaModule(latexCompileService)
    } else {
        restModule(latexCompileService)
    }

}

