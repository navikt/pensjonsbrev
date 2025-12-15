package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.ufoeretrygdPerioder
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggMaanedligeUfoeretrgdFoerSkatt
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*

val vedleggMaanedligUfoeretrygdFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligUfoeretrygdFoerSkattDto>(
    title = {
        text(
            bokmal { +"Dette er din månedlige uføretrygd før skatt" },
            nynorsk { +"Dette er den månadlege uføretrygda di før skatt" },
            english { +"This is your monthly disability benefit before tax" },
        )
    },
) {
    includePhrase(VedleggMaanedligeUfoeretrgdFoerSkatt.VedleggBeloepUfoeretrygd)

    forEach(ufoeretrygdPerioder) {
        includePhrase(VedleggMaanedligeUfoeretrgdFoerSkatt.TabellBeregnetUTHele(it))
    }
}
