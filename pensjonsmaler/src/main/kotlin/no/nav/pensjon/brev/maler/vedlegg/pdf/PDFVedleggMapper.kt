package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.InformasjonOmP1Dto
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.api.model.vedlegg.Vedleggtyper
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

fun PDFVedleggData.tilPDFVedlegg(): PDFVedlegg<out LanguageSupport> = when (this) {
    is P1Dto -> somDSL()
    is InformasjonOmP1Dto -> informasjonOmP1DtoSomDSL()
    else -> throw NotImplementedError("Ikke implementert støtte for ${javaClass.simpleName}")
}

fun informasjonOmP1DtoSomDSL() = PDFVedlegg.create<LangBokmalNynorskEnglish>(
    title = Vedleggtyper.InformasjonOmP1.tittel
) {
    side("InformasjonOmP1.pdf") {

    }
}
