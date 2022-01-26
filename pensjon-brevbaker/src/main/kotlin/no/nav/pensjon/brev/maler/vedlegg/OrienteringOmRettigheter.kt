package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*

data class OrienteringOmRettigheterParamDto(
    val kontaktTelefonnummer: Telefonnummer,
    val kontaktinformasjonNettsted: String
    )

val orienteringOmRettigheterOgPlikter = createAttachment<LangBokmalNynorskEnglish,OrienteringOmRettigheterParamDto>(
        title = newText(
                lang1 = Bokmal to "Dine rettigheter og plikter",
                lang2 = Nynorsk to "Dine rettar og plikter",
                lang3 = English to "Your rights and obligations"
        ),
        includeSakspart = true,
) {
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
        includePhrase(vedleggPlikterAFP1_001)
        includePhrase(vedleggPlikterAFP2_001)
        includePhrase(vedleggPlikterAFP3_001)
        includePhrase(vedleggPlikterAFP4_001)
    }
    includePhrase(infoAPBeskjed_001)
    includePhrase(vedleggVeiledning_001)
    includePhrase(argument().map { vedleggInnsynSakPensjon_001Dto(
            it.kontaktTelefonnummer,
            it.kontaktinformasjonNettsted) },vedleggInnsynSakPensjon_001)
    includePhrase(argument().map { vedleggInnsynSakUTPesys_001Dto(it.kontaktTelefonnummer) },vedleggInnsynSakUTPesys_001)
    includePhrase(vedleggHjelpFraAndre_001)
    includePhrase(argument().map { vedleggKlagePensjon_001Dto(it.kontaktTelefonnummer) },vedleggKlagePensjon_001)
    includePhrase(vedleggKlagePesys_001)
}

