package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.ufoeretrygdPerioder
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggMaanedligeUfoeretrgdFoerSkatt
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.newText

val vedleggMaanedligUfoeretrygdFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligUfoeretrygdFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige uføretrygd før skatt",
        Nynorsk to "Dette er den månadlege uføretrygda di før skatt",
        English to "This is your monthly disability benefit before tax",
    ),
) {
    includePhrase(VedleggMaanedligeUfoeretrgdFoerSkatt.VedleggBeloepUfoeretrygd)

    forEach(ufoeretrygdPerioder) {
        includePhrase(VedleggMaanedligeUfoeretrgdFoerSkatt.TabellBeregnetUTHele(it))
    }
}
