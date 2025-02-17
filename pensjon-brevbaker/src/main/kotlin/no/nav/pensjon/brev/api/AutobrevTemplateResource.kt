package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.BrevbakerHTML
import no.nav.brev.brevbaker.BrevbakerJSON
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class AutobrevTemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    name: String,
    templates: Set<T>,
    laTeXCompilerService: LaTeXCompilerService,
) : TemplateResource<Kode, T, BestillBrevRequest<Kode>>(name, templates, laTeXCompilerService) {

    override suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbakerPDF.renderPDF(createLetter(kode, letterData, language, felles))
        }

    override fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            BrevbakerHTML.renderHTML(createLetter(kode, letterData, language, felles))
        }

    fun renderJSON(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            BrevbakerJSON.renderJSON(createLetter(kode, letterData, language, felles))
        }

}
