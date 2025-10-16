package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.vedlegg.createAttachmentPDF
import no.nav.pensjon.brevbaker.api.model.EmptyPDFVedleggData

val informasjonOmP1Vedlegg = createAttachmentPDF<LangBokmalEnglish, EmptyPDFVedleggData>(
    title = listOf(
        newText(
            Language.Bokmal to "Informasjon om skjemaet P1 og hvordan det brukes",
            Language.English to "Information about the P1 form and its use"
        )
    )
) { data, felles ->
    side("InformasjonOmP1") {}
}