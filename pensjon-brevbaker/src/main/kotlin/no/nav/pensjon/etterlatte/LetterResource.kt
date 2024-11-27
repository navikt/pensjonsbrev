package no.nav.pensjon.etterlatte

import io.ktor.http.ContentType
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class LetterResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    private val latexCompilerService: LaTeXCompilerService,
    private val templateResource: TemplateResource<Kode, T>,
) {
    suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            renderPDF(templateResource.createLetter(kode, letterData, language, felles))
        }

    private suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse =
        Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { latexCompilerService.producePDF(it) }
            .let { pdf ->
                LetterResponse(
                    file = pdf.base64PDF,
                    contentType = ContentType.Application.Pdf.toString(),
                    letterMetadata = letter.template.letterMetadata
                )
            }

    fun renderJSON(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            renderJSON(templateResource.createLetter(kode, letterData, language, felles))
        }

    private fun renderJSON(letter: Letter<BrevbakerBrevdata>) = Letter2Markup.render(letter).letterMarkup

    fun countLetter(kode: Brevkode.Automatisk) = Metrics.prometheusRegistry.counter(
        "pensjon_brevbaker_etterlatte_request_count",
        listOf(Tag.of("brevkode", kode.kode()))
    ).increment()
}