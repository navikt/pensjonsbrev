package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.map
import no.nav.pensjon.brev.template.dsl.newText

data class OrienteringOmRettigheterParam(val test: String)
val test = createAttachment<OrienteringOmRettigheterParam>(
    title = newText(
        Bokmal to "Orientering om rettigheter og plikter",
        Language.Nynorsk to "",
        Language.English to "",
    ),
    includeSakspart = true,
) {
    includePhrase(VedleggPlikterOgRettigheterOverskriftPesys_001)
    includePhrase(VedleggPlikter_001)
    paragraph {
        list {
            includePhrase(VedleggPlikterAP2_001)
            includePhrase(VedleggPlikterAP3_001)
            includePhrase(VedleggPlikterAP1_001)
            includePhrase(VedleggPlikterAP4_002)
            includePhrase(VedleggPlikterAP13_002)
            includePhrase(VedleggPlikterAP15_002)
            includePhrase(VedleggPlikterAP6_002)
            includePhrase(VedleggPlikterAP14_002)
            includePhrase(VedleggPlikterAP18_001)
            includePhrase(VedleggPlikterAP16_001)
            includePhrase(VedleggPlikterAP17_001)
            includePhrase(VedleggPlikterAP19_001)
            includePhrase(VedleggPlikterAP8_001)
            includePhrase(VedleggPlikterAP11_001)
            includePhrase(VedleggPlikterAP9_001)
            includePhrase(VedleggPlikterAP7_001)
            includePhrase(VedleggPlikterAP12_001)
            includePhrase(VedleggPlikterAP10_001)
            includePhrase(VedleggPlikterAP5_001)
            includePhrase(VedleggPlikterAP26_001)
            includePhrase(VedleggPlikterAP27_001)
        }
    }

    includePhrase(VedleggPlikterHvorforMeldeAP_001)
    includePhrase(VedleggPlikterRettTilBarnetilleggAP_001)
    includePhrase(VedleggPlikterRettTilEktefelletilleggAP_001)
    includePhrase(VedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001)
    includePhrase(argument().map {
        VedleggPlikterinntektsproevingBTFellesBarnSaerkullsbarnAP_001.Param(
            true // TODO kan vi gjenbruke sivilstatus til å toggle ektefelle her?
        )
    }, VedleggPlikterinntektsproevingBTFellesBarnSaerkullsbarnAP_001)

    includePhrase(VedleggPlikterinntektsprovingBTOgETAP_001)
    includePhrase(VedleggPlikterinntektsprovingETAP_001)
    includePhrase(InfoAPBeskjed_001)
    includePhrase(VedleggVeiledning_001)
    includePhrase(
        felles().map {
            VedleggInnsynSakPensjon_001.Param(
                it.avsenderEnhet.nettside,
                it.avsenderEnhet.telefonnummer,
            )
        },
        VedleggInnsynSakPensjon_001
    )

    includePhrase(VedleggHjelpFraAndre_001)
    includePhrase(
        felles().map { VedleggKlagePensjon_001.Param(it.avsenderEnhet.telefonnummer) }, VedleggKlagePensjon_001
    )

    includePhrase(VedleggKlagePesys_001)
}