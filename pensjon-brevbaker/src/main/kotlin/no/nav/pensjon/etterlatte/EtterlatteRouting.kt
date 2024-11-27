package no.nav.pensjon.etterlatte

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


data class LetterResponse(val base64pdf: String, val contentType: String, val letterMetadata: LetterMetadata)

fun Route.etterlatteRouting(latexCompilerService: LaTeXCompilerService) {
    val letterResource = LetterResource(
        latexCompilerService, TemplateResource("", EtterlatteMaler.hentAutobrevmaler(), latexCompilerService)
    )

    post<BestillBrevRequest<Brevkode.Automatisk>>("/pdf") { letterRequest ->
        call.respond(letterResource.renderPDF(letterRequest))
        letterResource.countLetter(letterRequest.kode)
    }

    post<BestillBrevRequest<Brevkode.Automatisk>>("/json") {
        call.respond(letterResource.renderJSON(it))
    }
}

