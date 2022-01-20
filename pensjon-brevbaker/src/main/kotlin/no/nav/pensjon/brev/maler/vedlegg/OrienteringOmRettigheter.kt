package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*


data class OrienteringOmRettigheterParam(
    val test: String
)
val orienteringOmRettigheterOgPlikter = createAttachment<OrienteringOmRettigheterParam>(
    title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Language.Nynorsk to "Dine rettar og plikter",
            Language.English to "Your rights and obligations"
    ),
    includeSakspart = true,
) {
    includePhrase(vedleggPlikterOgRettigheterOverskriftPesys_001)
    includePhrase(vedleggPlikter_001)
    list {
        includePhrase(vedleggPlikterAP2_001)
        includePhrase(vedleggPlikterAP3_001)
        includePhrase(vedleggPlikterAP1_001)
        includePhrase(vedleggPlikterAP4_002)
        includePhrase(vedleggPlikterAP13_002)
        includePhrase(vedleggPlikterAP15_002)
        includePhrase(vedleggPlikterAP6_002)
        includePhrase(vedleggPlikterAP14_002)
        includePhrase(vedleggPlikterAP18_001)
        includePhrase(vedleggPlikterAP16_001)
        includePhrase(vedleggPlikterAP17_001)
        includePhrase(vedleggPlikterAP19_001)
        includePhrase(vedleggPlikterAP8_001)
        includePhrase(vedleggPlikterAP11_001)
        includePhrase(vedleggPlikterAP9_001)
        includePhrase(vedleggPlikterAP7_001)
        includePhrase(vedleggPlikterAP12_001)
        includePhrase(vedleggPlikterAP10_001)
        includePhrase(vedleggPlikterAP5_001)
        includePhrase(vedleggPlikterAP26_001)
        includePhrase(vedleggPlikterAP27_001)
    }
    includePhrase(vedleggPlikterHvorforMeldeAP_001)
    includePhrase(vedleggPlikterRettTilEktefelletilleggAP_001)
    includePhrase(vedleggPlikterRettTilBarnetilleggAP_001)
    includePhrase(vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001)
    includePhrase(vedleggPlikterinntektsprøvingBTFellesBarnSaerkullsbarnAP_001)
    includePhrase(vedleggPlikterinntektsprovingBTOgETAP_001)
    includePhrase(vedleggPlikterinntektsprovingETAP_001)
    includePhrase(vedleggPlikterUT_001)
    list {
        includePhrase(vedleggPlikterUT1_001)
        includePhrase(vedleggPlikterUT2_001)
        includePhrase(vedleggPlikterUT3_001)
        includePhrase(vedleggPlikterUT4_001)
        includePhrase(vedleggPlikterUT5_001)
        includePhrase(vedleggPlikterUT6_001)
        includePhrase(vedleggPlikterUT7_001)
        includePhrase(vedleggPlikterUT8_001)
        includePhrase(vedleggPlikterUT9_001)
        includePhrase(vedleggPlikterUT10_001)
        includePhrase(vedleggPlikterUT11_001)
        includePhrase(vedleggPlikterUT12_001)
    }
    includePhrase(vedleggPlikterAFP_001)
    list {
        includePhrase(vedleggPlikterAFP2_001)
        includePhrase(vedleggPlikterAFP3_001)
        includePhrase(vedleggPlikterAFP4_001)
    }
    includePhrase(infoAPBeskjed_001)
    includePhrase(vedleggVeiledning_001)
    includePhrase(vedleggInnsynSakPensjon_001)
    includePhrase(vedleggInnsynSakUTPesys_001)
    includePhrase(vedleggHjelpFraAndre_001)
    includePhrase(vedleggKlagePensjon_001)
    includePhrase(vedleggKlagePesys_001)
}

