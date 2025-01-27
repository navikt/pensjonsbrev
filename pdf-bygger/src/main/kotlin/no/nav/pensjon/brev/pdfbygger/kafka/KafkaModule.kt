package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService


fun Application.kafkaModule(latexCompileService: LatexCompileService) {
    val kafkaConfig = environment.config.config("pdfBygger.kafka")
    val pdfRequestConsumer = PdfRequestConsumer(kafkaConfig, latexCompileService)

    @OptIn(DelicateCoroutinesApi::class)
    pdfRequestConsumer.flow().launchIn(GlobalScope)

}