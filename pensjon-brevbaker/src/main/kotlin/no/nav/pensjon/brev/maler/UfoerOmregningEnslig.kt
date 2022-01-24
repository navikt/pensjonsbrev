package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.vedlegg.OrienteringOmRettigheterParam
import no.nav.pensjon.brev.maler.vedlegg.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
        lang = languages(Bokmal, Nynorsk),
        title = newText(
            Bokmal to "NAV har regnet om uføretrygden din",
            Nynorsk to "NAV har rekna om uføretrygda di",
            English to "NAV has altered your disability benefit"
        ),
        letterMetadata = LetterMetadata(
            "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true
        )
    ) {

        val avdod_sivilstand = argument().select(UfoerOmregningEnsligDto::avdod_sivilstand)
        val barnetillegg_er_redusert_mot_tak =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_er_redusert_mot_tak)
        val barnetillegg_ikke_utbetalt_pga_tak =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_ikke_utbetalt_pga_tak)
        val barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
        val bor_i_avtaleland = argument().select(UfoerOmregningEnsligDto::bor_i_avtaleland)
        val bor_i_norge = argument().select(UfoerOmregningEnsligDto::bor_i_norge)
        val ektefelletillegg_opphoert = argument().select(UfoerOmregningEnsligDto::ektefelletillegg_opphoert)
        val gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt =
            argument().select(UfoerOmregningEnsligDto::gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt)
        val gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet =
            argument().select(UfoerOmregningEnsligDto::gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet)
        val har_barn_overfoert_til_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barn_overfoert_til_saerkullsbarn)
        val har_barn_som_tidligere_var_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barn_som_tidligere_var_saerkullsbarn)
        val har_barnetillegg_for_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_for_saerkullsbarn)
        val har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk)
        val har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
        val har_barnetillegg_vedvirk = argument().select(UfoerOmregningEnsligDto::har_barnetillegg_vedvirk)
        val har_felles_barn_uten_barnetillegg_med_avdod =
            argument().select(UfoerOmregningEnsligDto::har_felles_barn_uten_barnetillegg_med_avdod)
        val har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd =
            argument().select(UfoerOmregningEnsligDto::har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd)
        val har_flere_ufoeretrygd_perioder = argument().select(UfoerOmregningEnsligDto::har_flere_ufoeretrygd_perioder)
        val har_minste_inntektsnivaa_foer_ufoeretrygd =
            argument().select(UfoerOmregningEnsligDto::har_minste_inntektsnivaa_foer_ufoeretrygd)
        val har_minsteytelse_vedvirk = argument().select(UfoerOmregningEnsligDto::har_minsteytelse_vedvirk)
        val har_ufoeremaaned_vedvirk = argument().select(UfoerOmregningEnsligDto::har_ufoeremaaned_vedvirk)
        val inntekt_ufoere_endret = argument().select(UfoerOmregningEnsligDto::inntekt_ufoere_endret)
        val institusjon_vedvirk = argument().select(UfoerOmregningEnsligDto::institusjon_vedvirk)
        val ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet =
            argument().select(UfoerOmregningEnsligDto::ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
        val ufoeretrygd_vedvirk_er_inntektsavkortet =
            argument().select(UfoerOmregningEnsligDto::ufoeretrygd_vedvirk_er_inntektsavkortet)

        outline {
            includePhrase(VedtakOverskriftPesys_001)
            showIf(
                (har_minsteytelse_vedvirk
                        or inntekt_ufoere_endret
                        or ektefelletillegg_opphoert)
                        and not(har_barnetillegg_for_saerkullsbarn)
            ) {
                includePhrase(
                    argument().map { OmregnUTDodEPSInnledn1_001.Param(it.avdod_navn, it.krav_virkedato_fom) },
                    OmregnUTDodEPSInnledn1_001
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(har_barnetillegg_for_saerkullsbarn)
            ) {
                includePhrase(
                    argument().map { OmregnUTDodEPSInnledn2_001.Param(it.avdod_navn) },
                    OmregnUTDodEPSInnledn2_001
                )
            }

            showIf(
                (har_minsteytelse_vedvirk
                        or inntekt_ufoere_endret
                        or ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn
            ) {
                includePhrase(
                    argument().map { OmregnUTBTDodEPSInnledn_001.Param(it.avdod_navn, it.krav_virkedato_fom) },
                    OmregnUTBTDodEPSInnledn_001
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn
                        and not(har_barn_overfoert_til_saerkullsbarn)
            ) {
                includePhrase(
                    argument().map { OmregnUTBTSBDodEPSInnledn_001.Param(it.avdod_navn) },
                    OmregnUTBTSBDodEPSInnledn_001
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barn_overfoert_til_saerkullsbarn
            ) {
                includePhrase(
                    argument().map { OmregnBTDodEPSInnledn_001.Param(it.avdod_navn, it.krav_virkedato_fom) },
                    OmregnBTDodEPSInnledn_001
                )
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                includePhrase(argument().map { BelopUT_001.Param(it.total_ufoeremaaneder) }, BelopUT_001)
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
            ) {
                includePhrase(argument().map { BelopUTVedlegg_001.Param(it.total_ufoeremaaneder) }, BelopUTVedlegg_001)
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                includePhrase(argument().map { BelopUTBT_001.Param(it.total_ufoeremaaneder) }, BelopUTBT_001)
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn
                        and har_flere_ufoeretrygd_perioder
            ) {
                includePhrase(
                    argument().map { BelopUTBTVedlegg_001.Param(it.total_ufoeremaaneder) },
                    BelopUTBTVedlegg_001
                )
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetaling_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
                        and not(institusjon_vedvirk.isOneOf(Institusjon.FENGSEL))
            ) {
                includePhrase(BelopUTIngenUtbetalingVedlegg_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                includePhrase(BelopUTBTIngenUtbetaling_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                        and har_flere_ufoeretrygd_perioder
            ) {
                includePhrase(BelopUTBTIngenUtbetalingVedlegg_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengsel_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengselVedlegg_001)
            }

            showIf(har_minsteytelse_vedvirk or inntekt_ufoere_endret) {
                includePhrase(BegrunnOverskrift_001)
            }

            showIf(har_minsteytelse_vedvirk and not(inntekt_ufoere_endret)) {
                includePhrase(argument().map {
                    EndrMYDodEPS2_001.Param(
                        it.minsteytelse_sats_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                }, EndrMYDodEPS2_001)
            }

            showIf(har_minsteytelse_vedvirk and inntekt_ufoere_endret) {
                includePhrase(argument().map {
                    EndrMYOgMinstIFUDodEPS2_001.Param(
                        it.minsteytelse_sats_vedvirk,
                        it.inntekt_foer_ufoerhet_vedvirk,
                        it.oppjustert_inntekt_foer_ufoerhet_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                }, EndrMYOgMinstIFUDodEPS2_001)
            }

            showIf(not(har_minsteytelse_vedvirk) and inntekt_ufoere_endret) {
                includePhrase(argument().map {
                    EndrMinstIFUDodEPS2_001.Param(
                        it.inntekt_foer_ufoerhet_vedvirk,
                        it.oppjustert_inntekt_foer_ufoerhet_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                }, EndrMinstIFUDodEPS2_001)
            }


            showIf(
                not(har_minste_inntektsnivaa_foer_ufoeretrygd)
                        and not(ufoeretrygd_vedvirk_er_inntektsavkortet)
            ) {
                includePhrase(HjemmelSivilstandUT_001)
            }

            showIf(
                har_minste_inntektsnivaa_foer_ufoeretrygd
                        and not(ufoeretrygd_vedvirk_er_inntektsavkortet)
            ) {
                includePhrase(HjemmelSivilstandUTMinsteIFU_001)
            }

            showIf(
                not(har_minste_inntektsnivaa_foer_ufoeretrygd)
                        and ufoeretrygd_vedvirk_er_inntektsavkortet
            ) {
                includePhrase(HjemmelSivilstandUTAvkortet_001)
            }

            showIf(
                har_minste_inntektsnivaa_foer_ufoeretrygd
                        and ufoeretrygd_vedvirk_er_inntektsavkortet
            ) {
                includePhrase(HjemmelSivilstandUTMinsteIFUAvkortet_001)
            }

            showIf(institusjon_vedvirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(HjemmelEPSDodUTInstitusjon_001)
            }

            showIf(institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(HjemmelEPSDodUTFengsel_001)
            }

            showIf(ektefelletillegg_opphoert) {
                includePhrase(OpphorETOverskrift_001)
                includePhrase(OpphorET_001)
                includePhrase(HjemmelET_001)
            }

            showIf(har_barn_overfoert_til_saerkullsbarn) {
                includePhrase(OmregningFBOverskrift_001)
                includePhrase(
                    argument().map { InfoFBTilSB_001.Param(it.barn_overfoert_til_saerkullsbarn) },
                    InfoFBTilSB_001
                )
                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and har_barn_som_tidligere_var_saerkullsbarn
                            and not(inntekt_ufoere_endret and har_minsteytelse_vedvirk)
                ) {
                    includePhrase(
                        argument().map { InfoTidligereSB_001.Param(it.tidligere_saerkullsbarn) },
                        InfoTidligereSB_001
                    )
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and (inntekt_ufoere_endret or har_minsteytelse_vedvirk)
                            and (barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk or barnetillegg_er_redusert_mot_tak)
                ) {
                    includePhrase(
                        argument().map { InfoTidligereSBOgEndretUT_001.Param(it.tidligere_saerkullsbarn) },
                        InfoTidligereSBOgEndretUT_001
                    )
                }
            }


            showIf(
                har_barnetillegg_for_saerkullsbarn
                        and (
                        har_minsteytelse_vedvirk
                                or inntekt_ufoere_endret
                                or ektefelletillegg_opphoert
                        )
            ) {
                includePhrase(EndringUTpavirkerBTOverskrift_001)

                showIf(not(barnetillegg_er_redusert_mot_tak)) {
                    includePhrase(argument().map {
                        IkkeRedusBTPgaTak_001.Param(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                        )
                    }, IkkeRedusBTPgaTak_001)
                }

                showIf(barnetillegg_er_redusert_mot_tak and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    includePhrase(argument().map {
                        RedusBTPgaTak_001.Param(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_beloep_foer_reduksjon_vedvirk,
                            it.barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk,
                        )
                    }, RedusBTPgaTak_001)
                }

                showIf(barnetillegg_ikke_utbetalt_pga_tak) {
                    includePhrase(argument().map {
                        IkkeUtbetaltBTPgaTak_001.Param(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                        )
                    }, IkkeUtbetaltBTPgaTak_001)
                }

                showIf(not(har_barn_overfoert_til_saerkullsbarn) and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    includePhrase(InfoBTSBInntekt_001)
                }

                showIf(har_barn_overfoert_til_saerkullsbarn and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    includePhrase(InfoBTOverfortTilSBInntekt_001)
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and not(barnetillegg_ikke_utbetalt_pga_tak)
                ) {
                    includePhrase(argument().map {
                        IkkeRedusBTSBPgaInntekt_001.Param(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                        )
                    }, IkkeRedusBTSBPgaInntekt_001)
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and (har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                            or (not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk) and har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk))
                ) {
                    includePhrase(argument().map {
                        RedusBTSBPgaInntekt_001.Param(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk
                        )
                    }, RedusBTSBPgaInntekt_001)
                }


                showIf(
                    har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk
                            and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                ) {
                    includePhrase(JusterBelopRedusBTPgaInntekt_001)
                }

                showIf(
                    har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk
                            and not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
                ) {
                    includePhrase(JusterBelopIkkeUtbetaltBTPgaInntekt_001)
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
                            and not(har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk)
                ) {
                    includePhrase(argument().map {
                        IkkeUtbetaltBTSBPgaInntekt_001.Param(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntektstak_vedvirk
                        )
                    }, IkkeUtbetaltBTSBPgaInntekt_001)
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and not(ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
                ) {
                    includePhrase(HjemmelBT_001)
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet
                ) {
                    includePhrase(HjemmelBTOvergangsregler_001)
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and not(ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
                ) {
                    includePhrase(HjemmelBTRedus_001)
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet
                ) {
                    includePhrase(HjemmelBTRedusOvergangsregler_001)
                }

                showIf(gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt) {
                    includePhrase(MerInfoBT_001)
                }
            }


            showIf(avdod_sivilstand.isOneOf(SAMBOER3_2)) {
                includePhrase(argument().map { GjRettSamboerOverskrift.Param(it.avdod_navn) }, GjRettSamboerOverskrift)
                includePhrase(GjRettUTSamboer_001)
            }
            showIf(avdod_sivilstand.isOneOf(GIFT, PARTNER, SAMBOER1_5)) {
                includePhrase(RettTilUTGJTOverskrift_001)
                includePhrase(HvemUTGJTVilkar_001)
                includePhrase(HvordanSoekerDuOverskrift_001)
                includePhrase(SoekUTGJT_001)

                showIf(bor_i_avtaleland) {
                    includePhrase(SoekAvtaleLandUT_001)
                }

                includePhrase(AvdodBoddArbUtlandOverskrift_001)
                includePhrase(AvdodBoddArbUtland2_001)
                includePhrase(PensjonFraAndreOverskrift_001)
                includePhrase(InfoAvdodPenFraAndre_001)
            }

            showIf(har_felles_barn_uten_barnetillegg_med_avdod) {
                includePhrase(HarBarnUnder18Overskrift_001)
                includePhrase(HarBarnUtenBT_001)
                includePhrase(HarBarnUnder18_001)
            }

            includePhrase(VirknTdsPktOverskrift_001)

            showIf(
                har_ufoeremaaned_vedvirk
                        and (har_minsteytelse_vedvirk or inntekt_ufoere_endret or ektefelletillegg_opphoert)
            ) {
                includePhrase(argument().map { VirkTdsPktUT_001.Param(it.krav_virkedato_fom) }, VirkTdsPktUT_001)
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(har_barn_overfoert_til_saerkullsbarn)
            ) {
                includePhrase(
                    argument().map { VirkTdsPktUTIkkeEndring_001.Param(it.krav_virkedato_fom) },
                    VirkTdsPktUTIkkeEndring_001
                )
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barn_overfoert_til_saerkullsbarn
            ) {
                includePhrase(
                    argument().map { VirkTdsPktUTBTOmregn_001.Param(it.krav_virkedato_fom) },
                    VirkTdsPktUTBTOmregn_001
                )
            }

            showIf(not(har_ufoeremaaned_vedvirk)) {
                includePhrase(
                    argument().map { VirkTdsPktUTAvkortetTil0_001.Param(it.krav_virkedato_fom) },
                    VirkTdsPktUTAvkortetTil0_001
                )
            }

            includePhrase(MeldInntektUTOverskrift_001)

            showIf(not(har_barnetillegg_vedvirk)) {
                includePhrase(MeldInntektUT_001)
            }

            showIf(har_barnetillegg_vedvirk) {
                includePhrase(MeldInntektUTBT_001)
            }

            includePhrase(MeldEndringerPesys_001)
            includePhrase(RettTilKlagePesys_001)
            includePhrase(RettTilInnsynPesys_001)
            includePhrase(SjekkUtbetalingeneOverskrift_001)
            includePhrase(SjekkUtbetalingeneUT_001)
            includePhrase(SkattekortOverskrift_001)
            includePhrase(SkattekortUT_001)

            showIf(not(bor_i_norge)) {
                includePhrase(SkattBorIUtlandPesys_001)
            }
        }

        includeAttachment(orienteringOmRettigheterOgPlikter, argument().map {
            OrienteringOmRettigheterParam(
                it.bor_i_norge,
                it.eps_bor_sammen_med_bruker_gjeldende,
                it.eps_institusjon_gjeldende,
                it.har_barnetillegg_felles_barn_vedvirk,
                it.har_barnetillegg_saerkullsbarn_vedvirk,
                it.institusjon_gjeldende,
                it.saktype,
                it.sivilstand,
            )
        })
    }
}