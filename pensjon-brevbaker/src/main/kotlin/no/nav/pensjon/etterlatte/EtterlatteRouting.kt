package no.nav.pensjon.etterlatte

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer

private val letterResource = LetterResource()

fun Route.etterlatteRouting(latexCompilerService: LaTeXCompilerService) {

    post("/hvaennderevilha") {
        val letterRequest = call.receive<EtterlatteBrevRequest>()

        val letter = letterResource.create(letterRequest)
        val pdfBase64 = PensjonLatexRenderer.render(letter)
            .let { latexCompilerService.producePDF(it, call.callId) }

        call.respond(LetterResponse(pdfBase64.base64PDF, letter.template.letterMetadata))

        // Om dere vil ha det
        Metrics.prometheusRegistry.counter(
            "pensjon_brevbaker_etterlatte_request_count",
            listOf(Tag.of("brevkode", letterRequest.kode.name))
        ).increment()
    }
}