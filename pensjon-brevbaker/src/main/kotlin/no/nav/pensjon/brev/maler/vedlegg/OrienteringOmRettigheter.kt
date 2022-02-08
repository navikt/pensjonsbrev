package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*

data class OrienteringOmRettigheterParamDto(
        val sivilstand: Sivilstand,
        val bor_i_norge: Boolean,
        val institusjon_gjeldende: Institusjon,
        val epsBorSammenMedBruker_epsGjeldende: Boolean,
        val epsPaaInstitusjon_epsGjeldende: Boolean,
        val eps_institusjon_gjeldende: Institusjon,
        val har_barnetillegg_felles_barn_vedvirk: Boolean,
        val har_barnetillegg_for_saerkullsbarn_vedvirk: Boolean,
        val har_ektefelletillegg_vedvirk: Boolean,
        val saktype: Sakstype,

        )


val orienteringOmRettigheterOgPlikter = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterParamDto>(
        title = newText(
                Bokmal to "Dine rettigheter og plikter",
                Nynorsk to "Dine rettar og plikter",
                English to "Your rights and obligations"
        ),
        includeSakspart = true,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterParamDto::bor_i_norge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterParamDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterParamDto::sivilstand)
    val epsBorSammenMedBruker_epsGjeldende = argument().select(OrienteringOmRettigheterParamDto::epsBorSammenMedBruker_epsGjeldende)
    val epsPaaInstitusjon_epsGjeldende = argument().select(OrienteringOmRettigheterParamDto::epsPaaInstitusjon_epsGjeldende)
    val eps_institusjon_gjeldende = argument().select(OrienteringOmRettigheterParamDto::eps_institusjon_gjeldende)
    val har_barnetillegg_felles_barn_vedvirk = argument().select(OrienteringOmRettigheterParamDto::har_barnetillegg_felles_barn_vedvirk)
    val har_barnetillegg_for_saerkullsbarn_vedvirk = argument().select(OrienteringOmRettigheterParamDto::har_barnetillegg_for_saerkullsbarn_vedvirk)
    val har_ektefelletillegg_vedvirk = argument().select(OrienteringOmRettigheterParamDto::har_ektefelletillegg_vedvirk)
    val saktype = argument().select(OrienteringOmRettigheterParamDto::saktype)


    // START val saktype = ALDER, include phrase
    // Mandatory phrase: vedleggPlikter_001
    showIf(
            saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikter_001)
    }
    list {
        showIf(
                bor_i_norge
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP2_001)
        }
        showIf(
                not(bor_i_norge)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP3_001)
        }
        showIf(
                sivilstand.isOneOf(ENSLIG, ENKE, Sivilstand.INGEN)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP1_001)
        }
        showIf(
                sivilstand.isOneOf(GIFT)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP4_002)
        }
        showIf(
                sivilstand.isOneOf(PARTNER)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP13_002)
        }
        showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP15_002)
        }
        showIf(
                sivilstand.isOneOf(GIFT)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP6_002)
        }
        showIf(
                sivilstand.isOneOf(PARTNER)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP14_002)
        }
        showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP18_001)
        }
        showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP16_001)
        }
        showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP17_001)
        }
        showIf(
                sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)
                        and epsBorSammenMedBruker_epsGjeldende
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP19_001)
        }
        showIf(
                sivilstand.isOneOf(GIFT_LEVER_ADSKILT, GIFT)
                        and not(epsBorSammenMedBruker_epsGjeldende)
                        and not(institusjon_gjeldende.isOneOf(SYKEHJEM))
                        and not(eps_institusjon_gjeldende.isOneOf(SYKEHJEM))
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP8_001)
        }
        showIf(
                sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
                        and not(epsBorSammenMedBruker_epsGjeldende)
                        and not(institusjon_gjeldende.isOneOf(SYKEHJEM))
                        and not(eps_institusjon_gjeldende.isOneOf(SYKEHJEM))
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP11_001)
        }
        showIf(
                sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP9_001)
        }
        showIf(
                sivilstand.isOneOf(GIFT)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP7_001)
        }
        showIf(
                sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP12_001)
        }
        showIf(
                sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
                        and not(epsBorSammenMedBruker_epsGjeldende)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP10_001)
        }
        showIf(
                not(sivilstand.isOneOf(ENSLIG, ENKE, Sivilstand.INGEN))
                        and (epsBorSammenMedBruker_epsGjeldende)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and not(epsPaaInstitusjon_epsGjeldende)
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(argument().map {
                vedleggPlikterAP5_001Dto(it.sivilstand)
            }, vedleggPlikterAP5_001)
        }
        showIf(
                sivilstand.isOneOf(ENSLIG, ENKE, Sivilstand.INGEN)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and bor_i_norge
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP26_001)
        }
        showIf(
                not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and bor_i_norge
                        and saktype.isOneOf(Sakstype.ALDER)
        ) {
            includePhrase(vedleggPlikterAP27_001)
        }
    }
    // Mandatory phrase: vedleggPlikterHvorforMeldeAP_001
    showIf(
            saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikterHvorforMeldeAP_001)
    }
    showIf(
            har_barnetillegg_felles_barn_vedvirk
                    and har_barnetillegg_for_saerkullsbarn_vedvirk
                    and not(har_ektefelletillegg_vedvirk)
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikterRettTilBarnetilleggAP_001)
    }
    showIf(
            har_ektefelletillegg_vedvirk
                    and not(har_barnetillegg_felles_barn_vedvirk)
                    and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(argument().map {
            vedleggPlikterRettTilEktefelletilleggAP_001Dto(it.sivilstand)
        }, vedleggPlikterRettTilEktefelletilleggAP_001)
    }
    showIf(
            har_barnetillegg_felles_barn_vedvirk
                    or har_barnetillegg_for_saerkullsbarn_vedvirk
                    and har_ektefelletillegg_vedvirk
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(argument().map {
            vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001Dto(it.sivilstand)
        }, vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001)
    }
    showIf(
            har_barnetillegg_felles_barn_vedvirk
                    or har_barnetillegg_for_saerkullsbarn_vedvirk
                    and not(har_ektefelletillegg_vedvirk)
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikterinntektsprøvingBTFellesBarnSaerkullsbarnAP_001)
    }
    showIf(
            har_barnetillegg_felles_barn_vedvirk
                    or har_barnetillegg_for_saerkullsbarn_vedvirk
                    and har_ektefelletillegg_vedvirk
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikterinntektsprovingBTOgETAP_001)
    }
    showIf(
            not(har_barnetillegg_felles_barn_vedvirk)
                    and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                    and har_ektefelletillegg_vedvirk
                    and saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(vedleggPlikterinntektsprovingETAP_001)
    }
    /* END val saktype = ALDER
        START val saktype = UFOEREP, include phrase
            Mandatory phrase vedleggPlikterUT_001 */
    showIf(
            saktype.isOneOf(Sakstype.UFOEREP)
    ) {
        includePhrase(vedleggPlikterUT_001)
    }
    list {
        // Mandatory phrase vedleggPlikterUT1_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT1_001)
        }
        // Mandatory phrase vedleggPlikterUT2_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT2_001)
        }
        showIf(
                bor_i_norge
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT3_001)
        }
        showIf(
                bor_i_norge
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT4_001)
        }
        // Mandatory phrase vedleggPlikterUT5_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT5_001)
        }
        showIf(
                sivilstand.isOneOf(ENSLIG, ENKE, Sivilstand.INGEN)
                        and saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT6_001)
        }
        /* If barnetillegg_beloep_gjeldendeBeregnetUTPerManed > 0
       showIf(
               (barnetillegg_beloep_gjeldendeBeregnetUTPerManed > 0)
                and saktype.isOneOf(Sakstype.UFOEREP)
        ) {
               includePhrase(vedleggPlikterUT7_001)
        } */
        // Mandatory phrase vedleggPlikterUT8_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT8_001)
        }
        // Mandatory phrase vedleggPlikterUT9_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT9_001)
        }

        // Mandatory phrase vedleggPlikterUT10_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT10_001)
        }

        // Mandatory phrase vedleggPlikterUT11_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT11_001)
        }

        // Mandatory phrase vedleggPlikterUT12_001
        showIf(
                saktype.isOneOf(Sakstype.UFOEREP)
        ) {
            includePhrase(vedleggPlikterUT12_001)
        }
    }
    /* END val saktype = UFOEREP
        START val saktype = AFP, include phrase
            Mandatory phrase vedleggPlikterAFP_001 */
    showIf(
            saktype.isOneOf(Sakstype.AFP)
    ) {
        includePhrase(vedleggPlikterAFP_001)
    }
    list {
        // Mandatory phrase vedleggPlikterAFP1_001
        showIf(
                saktype.isOneOf(Sakstype.AFP)
        ) {
            includePhrase(vedleggPlikterAFP1_001)
        }
        showIf(
                sivilstand.isOneOf(ENSLIG, ENKE, Sivilstand.INGEN)
                        and saktype.isOneOf(Sakstype.AFP)
        ) {
            includePhrase(vedleggPlikterAFP2_001)
        }
        showIf(
                bor_i_norge
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.AFP)
        ) {
            includePhrase(vedleggPlikterAFP3_001)
        }
        showIf(
                not(bor_i_norge)
                        and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
                        and saktype.isOneOf(Sakstype.AFP)
        ) {
            includePhrase(vedleggPlikterAFP4_001)
        }
    }
    // END val saktype = AFP
    showIf(
            saktype.isOneOf(Sakstype.ALDER)
    ) {
        includePhrase(infoAPBeskjed_001)
    }
    // Mandatory phrase vedleggVeiledning_001
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
    // Mandatory phrase vedleggHjelpFraAndre_001
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

