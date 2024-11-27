package no.nav.pensjon.etterlatte

import io.ktor.http.ContentType
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

private val letterResource = LetterResource(EtterlatteMaler)

data class LetterResponse(val file: String, val contentType: String, val letterMetadata: LetterMetadata)

fun Route.etterlatteRouting(latexCompilerService: LaTeXCompilerService) {

    post("/pdf") {
        val letterRequest = call.receive<BestillBrevRequest<*>>()

        val letter = letterResource.create(letterRequest)
        val pdfBase64 = Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { latexCompilerService.producePDF(it) }

        call.respond(LetterResponse(
            file = pdfBase64.base64PDF,
            contentType = ContentType.Application.Pdf.toString(),
            letterMetadata = letter.template.letterMetadata
        ))

        // Om dere vil ha det
        Metrics.prometheusRegistry.counter(
            "pensjon_brevbaker_etterlatte_request_count",
            listOf(Tag.of("brevkode", letterRequest.kode.kode()))
        ).increment()
    }

    post("/json") {
        val letterRequest = call.receive<BestillBrevRequest<*>>()
        val letter = letterResource.create(letterRequest)

        call.respond(Letter2Markup.render(letter).letterMarkup)
    }
}

