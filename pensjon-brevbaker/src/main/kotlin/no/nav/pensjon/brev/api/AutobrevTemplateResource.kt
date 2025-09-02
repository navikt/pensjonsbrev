package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFVedleggAppender
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillBrevRequestAsync
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.PDFByggerAsync
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    name: String,
    templates: Set<T>,
    pdfByggerService: PDFByggerService,
    pdfVedleggAppender: PDFVedleggAppender,
    private val pDFByggerAsync: PDFByggerAsync?,

    ) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>(name, templates, pdfByggerService, pdfVedleggAppender) {

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderPDF(createLetter(kode, letterData, language, felles))
        }

    override fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbaker.renderHTML(createLetter(kode, letterData, language, felles))
        }

    fun renderJSON(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            brevbaker.renderLetterMarkup(createLetter(kode, letterData, language, felles))
        }

    fun renderPdfAsync(brevbestillingAsync: BestillBrevRequestAsync<Kode>) {
        val letter = with(brevbestillingAsync) {
            createLetter(kode, letterData, language, felles)
        }
        brevbaker.renderLetterWithAttachmentsMarkup(letter)
            .let {
                pDFByggerAsync!!.renderAsync(
                    PDFRequestAsync(
                        request = PDFRequest(
                            letterMarkup = it.letterMarkup,
                            attachments = it.attachments,
                            language = letter.language.toCode(),
                            brevtype = letter.template.letterMetadata.brevtype
                        ),
                        messageId = brevbestillingAsync.messageId,
                        replyTopic = brevbestillingAsync.replyTopic,
                    )
                )
            }
    }

}
