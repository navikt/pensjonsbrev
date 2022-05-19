package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDto
import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = ALDER && vedtakResultat = INNVL

val orienteringOmRettigheterOgPlikterAlder = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterAlderDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = true,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterAlderDto::bruker_borINorge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterAlderDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterAlderDto::bruker_sivilstand)
    val eps_bor_sammen_med_bruker_eps_gjeldende =
        argument().select(OrienteringOmRettigheterAlderDto::eps_borSammenMedBrukerGjeldende)
    val eps_institusjon_gjeldende = argument().select(OrienteringOmRettigheterAlderDto::instutisjon_epsInstitusjonGjeldende)
    val har_barnetillegg_felles_barn_vedvirk =
        argument().select(OrienteringOmRettigheterAlderDto::barnetilleggVedvirk_innvilgetBarnetillegFellesbarn)
    val har_barnetillegg_for_saerkullsbarn_vedvirk =
        argument().select(OrienteringOmRettigheterAlderDto::barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn)
    val har_ektefelletillegg_vedvirk =
        argument().select(OrienteringOmRettigheterAlderDto::ektefelletilleggVedvirk_innvilgetEktefelletillegg)
    val saktype = argument().select(OrienteringOmRettigheterAlderDto::saktype)


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
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP4_002) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP13_002) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP15_002) }
            }
            showIf(
                sivilstand.isOneOf(GIFT)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP6_002) }
            }
            showIf(
                sivilstand.isOneOf(PARTNER)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP14_002) }
            }
            showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                    and eps_bor_sammen_med_bruker_eps_gjeldende
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP18_001) }
                item { includePhrase(vedleggPlikterAP16_001) }
                item { includePhrase(vedleggPlikterAP17_001) }
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
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item { includePhrase(vedleggPlikterAP10_001) }
            }
            showIf(
                not(sivilstand.isOneOf(ENSLIG, ENKE))
                    and (eps_bor_sammen_med_bruker_eps_gjeldende)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                    and not(eps_institusjon_gjeldende.isOneOf(INGEN))
            ) {
                item {
                    includePhrase(vedleggPlikterAP5_001, argument().map {
                        vedleggPlikterAP5_001Dto(it.bruker_sivilstand)
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
            includePhrase(vedleggPlikterRettTilEktefelletilleggAP_001, argument().map { it.bruker_sivilstand })
        }
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                or har_barnetillegg_for_saerkullsbarn_vedvirk
                and har_ektefelletillegg_vedvirk
        ) {
            includePhrase(
                vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001,
                argument().map { it.bruker_sivilstand })
        }
        showIf(
            har_barnetillegg_felles_barn_vedvirk
                or har_barnetillegg_for_saerkullsbarn_vedvirk
                and not(har_ektefelletillegg_vedvirk)
        ) {
            includePhrase(vedleggPlikterinntektsprovingBTFellesBarnSaerkullsbarnAP_001)
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

    showIf(saktype.isOneOf(Sakstype.ALDER)) {
        includePhrase(infoAPBeskjed_001)
        includePhrase(vedleggVeiledning_001)
        includePhrase(vedleggInnsynSakPensjon_001)
        includePhrase(vedleggHjelpFraAndre_001)
        includePhrase(vedleggKlagePesys_001)
        }
    }
