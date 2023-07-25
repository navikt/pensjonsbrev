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
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter

private val letterResource = LetterResource()

fun Route.etterlatteRouting(latexCompilerService: LaTeXCompilerService) {

    post("/pdf") {
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

    post("/html") {
        val letterRequest = call.receive<EtterlatteBrevRequest>()
        val letter = letterResource.create(letterRequest)
        val html = PensjonHTMLRenderer.render(letter)

        // egen response eller noe sånnt. Html brev går veldig fort å rendre.
        call.respond(HTMLResponse(html.base64EncodedFiles(), letter.template.letterMetadata))
    }

    post("/json") {
        val letterRequest = call.receive<EtterlatteBrevRequest>()
        val letter = letterResource.create(letterRequest)

        call.respond(JSONResponse(PensjonJsonRenderer.render(letter), letter.template.letterMetadata))
    }
}

data class HTMLResponse(val html: Map<String, String>, val letterMetadata: LetterMetadata)

data class JSONResponse(val json: RenderedJsonLetter, val letterMetadata: LetterMetadata)