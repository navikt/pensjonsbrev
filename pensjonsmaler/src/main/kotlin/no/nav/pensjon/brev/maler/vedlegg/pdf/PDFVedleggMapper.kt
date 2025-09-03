package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.InformasjonOmP1Dto
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

fun PDFVedleggData.tilPDFVedlegg(): PDFVedlegg<out LanguageSupport> = when (this) {
    is P1Dto -> somDSL()
    is InformasjonOmP1Dto -> informasjonOmP1DtoSomDSL()
    else -> throw NotImplementedError("Ikke implementert støtte for ${javaClass.simpleName}")
}

fun informasjonOmP1DtoSomDSL() = PDFVedlegg.create<LangBokmalNynorskEnglish>(
    title = mapOf(
        Language.Bokmal to "Informasjon om P1",
        Language.Nynorsk to "Informasjon om P1",
        Language.English to "Information about P1",
    )
) {
    side("InformasjonOmP1.pdf") {

    }
}
