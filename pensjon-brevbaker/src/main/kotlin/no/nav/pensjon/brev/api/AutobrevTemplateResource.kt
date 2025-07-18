package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.ContentTypes
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestAsync
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillBrevRequestAsync
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerGrpcService
import no.nav.pensjon.brev.latex.LaTeXCompilerHttpService
import no.nav.pensjon.brev.latex.LatexAsyncCompilerService
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    name: String,
    templates: Set<T>,
    laTeXCompilerService: LaTeXCompilerHttpService,
    private val laTeXAsyncCompilerService: LatexAsyncCompilerService?,
    private val latexCompilerGrpcService: LaTeXCompilerGrpcService,
) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>(name, templates, laTeXCompilerService) {

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

    suspend fun renderPDFGrpc(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        brevbestilling.run {
            val letter = createLetter(kode, letterData, language, felles)
            val markup = brevbaker.renderLetterWithAttachmentsMarkup(letter)

            latexCompilerGrpcService.producePDF(
                PDFRequest(
                    letterMarkup = markup.letterMarkup,
                    attachments = markup.attachments,
                    language = letter.language.toCode(),
                    felles = letter.felles,
                    brevtype = letter.template.letterMetadata.brevtype
                )
            ).let { pdf ->
                LetterResponse(
                    file = pdf.bytes,
                    contentType = ContentTypes.PDF,
                    letterMetadata = letter.template.letterMetadata
                )
            }
        }

    fun renderPdfAsync(brevbestillingAsync: BestillBrevRequestAsync<Kode>) {
        val letter = with(brevbestillingAsync) {
            createLetter(kode, letterData, language, felles)
        }
        brevbaker.renderLetterWithAttachmentsMarkup(letter)
            .let {
                laTeXAsyncCompilerService!!.renderAsync(
                    PDFRequestAsync(
                        request = PDFRequest(
                            letterMarkup = it.letterMarkup,
                            attachments = it.attachments,
                            language = letter.language.toCode(),
                            felles = letter.felles,
                            brevtype = letter.template.letterMetadata.brevtype
                        ),
                        messageId = brevbestillingAsync.messageId,
                        replyTopic = brevbestillingAsync.replyTopic,
                    )
                )
            }
    }

}
