package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*


data class OrienteringOmRettigheterParam(
    val bor_i_norge: Boolean,
    val eps_bor_sammen_med_bruker_gjeldende: Boolean,
    val eps_institusjon_gjeldende: Institusjon,
    val har_barnetillegg_felles_barn_vedvirk: Boolean,
    val har_barnetillegg_saerkullsbarn_vedvirk: Boolean,
    val institusjon_gjeldende: Institusjon,
    val saktype: Sakstype,
    val sivilstand: Sivilstand,
)
val orienteringOmRettigheterOgPlikter = createAttachment<OrienteringOmRettigheterParam>(
    title = newText(
        Bokmal to "Orientering om rettigheter og plikter",
        Language.Nynorsk to "",
        Language.English to "",
    ),
    includeSakspart = true,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterParam::bor_i_norge)
    val sivilstand = argument().select(OrienteringOmRettigheterParam::sivilstand)
    val eps_bor_sammen_med_bruker_gjeldende = argument().select(OrienteringOmRettigheterParam::eps_bor_sammen_med_bruker_gjeldende)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterParam::institusjon_gjeldende)
    val eps_institusjon_gjeldende = argument().select(OrienteringOmRettigheterParam::eps_institusjon_gjeldende)
    val saktype = argument().select(OrienteringOmRettigheterParam::saktype)
    val har_barnetillegg_felles_barn_vedvirk = argument().select(OrienteringOmRettigheterParam::har_barnetillegg_felles_barn_vedvirk)
    val har_barnetillegg_saerkullsbarn_vedvirk = argument().select(OrienteringOmRettigheterParam::har_barnetillegg_saerkullsbarn_vedvirk)


    includePhrase(VedleggPlikterOgRettigheterOverskriftPesys_001)
    includePhrase(VedleggPlikter_001)
    paragraph {
        list {
            showIf(bor_i_norge and institusjon_gjeldende.isOneOf(Institusjon.INGEN)) {
                includePhrase(VedleggPlikterAP2_001)
            }
            showIf(not(bor_i_norge) and institusjon_gjeldende.isOneOf(Institusjon.INGEN)) {
                includePhrase(VedleggPlikterAP3_001)
            }
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
        VedleggPlikterinntektsproevingBTFellesBarnSaerkullsbarnAP_001.Param(it.sivilstand)
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