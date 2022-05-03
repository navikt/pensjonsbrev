package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

val orienteringOmRettigheterOgPlikter = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = true,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterDto::bor_i_norge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterDto::sivilstand)
    val eps_bor_sammen_med_bruker_eps_gjeldende =
        argument().select(OrienteringOmRettigheterDto::eps_bor_sammen_med_bruker_gjeldende)
    val eps_institusjon_gjeldende = argument().select(OrienteringOmRettigheterDto::eps_institusjon_gjeldende)
    val har_barnetillegg_felles_barn_vedvirk =
        argument().select(OrienteringOmRettigheterDto::har_barnetillegg_felles_barn_vedvirk)
    val har_barnetillegg_for_saerkullsbarn_vedvirk =
        argument().select(OrienteringOmRettigheterDto::har_barnetillegg_for_saerkullsbarn_vedvirk)
    val har_ektefelletillegg_vedvirk = argument().select(OrienteringOmRettigheterDto::har_ektefelletillegg_vedvirk)
    val saktype = argument().select(OrienteringOmRettigheterDto::saktype)
    val barnetillegg_beloep_gjeldendeBeregnetUTPerManed =
        argument().select(OrienteringOmRettigheterDto::barnetillegg_beloep_gjeldendeBeregnetUTPerManed)


    // START of Sakstype = ALDER, include phrases
    // Mandatory phrase: vedleggPlikter_001
    showIf(saktype.isOneOf(Sakstype.ALDER)) {
        includePhrase(vedleggPlikter_001)
        list {
            showIf(
                bor_i_norge
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAP2_001) }
            }
            showIf(
                not(bor_i_norge)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAP3_001) }
            }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAP1_001) }
            }
            showIf(
                sivilstand.isOneOf(GIFT)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP4_002) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP13_002) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP15_002) }
            }
            showIf(
                sivilstand.isOneOf(GIFT)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP6_002) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP14_002) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP18_001) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP16_001) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP17_001) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP19_001) }
            }
            showIf(
                sivilstand.isOneOf(GIFT_LEVER_ADSKILT, GIFT)
                    and not(eps_bor_sammen_med_bruker_eps_gjeldende)
                    and not(institusjon_gjeldende.isOneOf(SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAP8_001) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
                    and not(eps_bor_sammen_med_bruker_eps_gjeldende)
                    and not(institusjon_gjeldende.isOneOf(SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAP11_001) }
            }
            showIf(
                sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)

            ) {
                item { includePhrase(vedleggPlikterAP9_001) }
            }
            showIf(
                sivilstand.isOneOf(GIFT)
            ) {
                item { includePhrase(vedleggPlikterAP7_001) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
            ) {
                item { includePhrase(vedleggPlikterAP12_001) }
            }
            showIf(
                sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
                    and not(eps_bor_sammen_med_bruker_eps_gjeldende)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP10_001) }
            }
            showIf(
                not(sivilstand.isOneOf(ENSLIG, ENKE))
                    and (eps_bor_sammen_med_bruker_eps_gjeldende)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(Institusjon.INGEN))
            ) {
                item {
                    includePhrase(vedleggPlikterAP5_001, argument().map {
                        vedleggPlikterAP5_001Dto(it.sivilstand)
                    })
                }
            }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and bor_i_norge
            ) {
                item { includePhrase(vedleggPlikterAP26_001) }
            }
            showIf(
                not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and bor_i_norge
            ) {
                item { includePhrase(vedleggPlikterAP27_001) }
            }
        }
        // END of list
        // Mandatory phrase: vedleggPlikterHvorforMeldeAP_001
        includePhrase(vedleggPlikterHvorforMeldeAP_001)
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                and har_barnetillegg_for_saerkullsbarn_vedvirk
                and not(har_ektefelletillegg_vedvirk)
        ) {
            includePhrase(vedleggPlikterRettTilBarnetilleggAP_001)
        }
        showIf(
            har_ektefelletillegg_vedvirk
                and not(har_barnetillegg_felles_barn_vedvirk)
                and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
        ) {
            includePhrase(vedleggPlikterRettTilEktefelletilleggAP_001, argument().map { it.sivilstand })
        }
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                or har_barnetillegg_for_saerkullsbarn_vedvirk
                and har_ektefelletillegg_vedvirk
        ) {
            includePhrase(vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001, argument().map { it.sivilstand })
        }
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                or har_barnetillegg_for_saerkullsbarn_vedvirk
                and not(har_ektefelletillegg_vedvirk)
        ) {
            includePhrase(vedleggPlikterinntektsprøvingBTFellesBarnSaerkullsbarnAP_001)
        }
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                or har_barnetillegg_for_saerkullsbarn_vedvirk
                and har_ektefelletillegg_vedvirk
        ) {
            includePhrase(vedleggPlikterinntektsprovingBTOgETAP_001)
        }
        showIf(
            not(har_barnetillegg_felles_barn_vedvirk)
                and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                and har_ektefelletillegg_vedvirk
        ) {
            includePhrase(vedleggPlikterinntektsprovingETAP_001)
        }
    }
    // END of Sakstype = ALDER
    // START of Sakstype = UFOEREP, include phrases
    // Mandatory phrase vedleggPlikterUT_001
    showIf(saktype.isOneOf(Sakstype.UFOEREP)
    ) {
        includePhrase(vedleggPlikterUT_001)
        list {
            // Mandatory phraser
            item { includePhrase(vedleggPlikterUT1_001) }
            item { includePhrase(vedleggPlikterUT2_001) }
            showIf(
                bor_i_norge
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterUT3_001) }
            }
            showIf(
                bor_i_norge
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterUT4_001) }
            }
            // Mandatory phrase
            item { includePhrase(vedleggPlikterUT5_001) }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE)
            ) {
                item { includePhrase(vedleggPlikterUT6_001) }
            }
            // If barnetillegg_beloep_gjeldendeBeregnetUTPerManed > 0
            showIf(barnetillegg_beloep_gjeldendeBeregnetUTPerManed.map { it > 0 }
            ) {
                item { includePhrase(vedleggPlikterUT7_001) }
            }
            // Mandatory phraser
            item { includePhrase(vedleggPlikterUT8_001) }
            item { includePhrase(vedleggPlikterUT9_001) }
            item { includePhrase(vedleggPlikterUT10_001) }
            item { includePhrase(vedleggPlikterUT11_001) }
            item { includePhrase(vedleggPlikterUT12_001) }
        }
    }
    //END of Sakstype = UFOEREP
    // START of Sakstype = AFP, include phraser
    // Mandatory phrase vedleggPlikterAFP_001
    showIf(saktype.isOneOf(Sakstype.AFP)
    ) {
        includePhrase(vedleggPlikterAFP_001)
        list {
            // Mandatory phrase
            item { includePhrase(vedleggPlikterAFP1_001) }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE)
            ) {
                item { includePhrase(vedleggPlikterAFP2_001) }
            }
            showIf(
                bor_i_norge
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAFP3_001) }
            }
            showIf(
                not(bor_i_norge)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAFP4_001) }
            }
        }
    }
    // END of Sakstype = AFP
    showIf(
        saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(infoAPBeskjed_001)
    }
    // Mandatory phrase
    includePhrase(vedleggVeiledning_001)
    showIf(
        not(saktype.isOneOf(Sakstype.UFOEREP))
    ) {
        includePhrase(vedleggInnsynSakPensjon_001)
    }
    showIf(
        saktype.isOneOf(Sakstype.UFOEREP)
    ) {
        includePhrase(vedleggInnsynSakUTPesys_001)
    }
    // Mandatory phrase
    includePhrase(vedleggHjelpFraAndre_001)
    showIf(
        not(saktype.isOneOf(Sakstype.ALDER))
    ) {
        includePhrase(vedleggKlagePensjon_001)
    }
    showIf(
        saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggKlagePesys_001)
    }
}
