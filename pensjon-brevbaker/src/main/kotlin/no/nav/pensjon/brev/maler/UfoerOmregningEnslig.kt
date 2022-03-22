package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.maler.vedlegg.OrienteringOmRettigheterParamDto
import no.nav.pensjon.brev.maler.vedlegg.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import java.time.LocalDate

object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
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
        val eps_bor_sammen_med_bruker_gjeldende =
            argument().select(UfoerOmregningEnsligDto::eps_bor_sammen_med_bruker_gjeldende)
        val gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt =
            argument().select(UfoerOmregningEnsligDto::gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt)
        val gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet =
            argument().select(UfoerOmregningEnsligDto::gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet)
        val har_barn_overfoert_til_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barn_overfoert_til_saerkullsbarn)
        val har_barn_som_tidligere_var_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barn_som_tidligere_var_saerkullsbarn)
        val har_barnetillegg_for_saerkullsbarn_vedvirk =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_for_saerkullsbarn_vedvirk)
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
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
            ) {
                includePhrase(
                    OmregnUTDodEPSInnledn1_001,
                    argument().map { OmregnUTDodEPSInnledn1001Dto(it.avdod_navn, it.krav_virkedato_fom) }
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
            ) {
                includePhrase(OmregnUTDodEPSInnledn2_001,
                    argument().map { OmregnUTDodEPSInnledn2_001Dto(it.avdod_navn) }
                )
            }

            showIf(
                (har_minsteytelse_vedvirk
                        or inntekt_ufoere_endret
                        or ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn_vedvirk
            ) {
                includePhrase(
                    OmregnUTBTDodEPSInnledn_001,
                    argument().map { OmregnUTBTDodEPSInnledn_001Dto(it.avdod_navn, it.krav_virkedato_fom) }
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn_vedvirk
                        and not(har_barn_overfoert_til_saerkullsbarn)
            ) {
                includePhrase(
                    OmregnUTBTSBDodEPSInnledn_001, argument().map { OmregnUTBTSBDodEPSInnledn_001Dto(it.avdod_navn) }
                )
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barn_overfoert_til_saerkullsbarn
            ) {
                includePhrase(
                    OmregnBTDodEPSInnledn_001,
                    argument().map { OmregnBTDodEPSInnledn_001Dto(it.avdod_navn, it.krav_virkedato_fom) }
                )
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                includePhrase(BelopUT_001, argument().map { BelopUT_001Dto(it.total_ufoeremaaneder) })
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                        and har_flere_ufoeretrygd_perioder
            ) {
                includePhrase(BelopUTVedlegg_001, argument().map { BelopUTVedlegg_001Dto(it.total_ufoeremaaneder) })
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn_vedvirk
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                includePhrase(BelopUTBT_001, argument().map { BelopUTBT_001Dto(it.total_ufoeremaaneder) })
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn_vedvirk
                        and har_flere_ufoeretrygd_perioder
            ) {
                includePhrase(
                    BelopUTBTVedlegg_001,
                    argument().map { BelopUTBTVedlegg_001Dto(it.total_ufoeremaaneder) }
                )
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                        and not(har_flere_ufoeretrygd_perioder)
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetaling_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
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
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                        and not(har_flere_ufoeretrygd_perioder)
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengsel_001)
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn_vedvirk)
                        and har_flere_ufoeretrygd_perioder
                        and institusjon_vedvirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengselVedlegg_001)
            }

            showIf(har_minsteytelse_vedvirk or inntekt_ufoere_endret) {
                includePhrase(BegrunnOverskrift_001)
            }

            showIf(har_minsteytelse_vedvirk and not(inntekt_ufoere_endret)) {
                includePhrase(EndrMYDodEPS2_001, argument().map {
                    EndrMYDodEPS2_001Dto(
                        it.minsteytelse_sats_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                })
            }

            showIf(har_minsteytelse_vedvirk and inntekt_ufoere_endret) {
                includePhrase(EndrMYOgMinstIFUDodEPS2_001, argument().map {
                    EndrMYOgMinstIFUDodEPS2_001Dto(
                        it.minsteytelse_sats_vedvirk,
                        it.inntekt_foer_ufoerhet_vedvirk,
                        it.oppjustert_inntekt_foer_ufoerhet_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                })
            }

            showIf(not(har_minsteytelse_vedvirk) and inntekt_ufoere_endret) {
                includePhrase(EndrMinstIFUDodEPS2_001, argument().map {
                    EndrMinstIFUDodEPS2_001Dto(
                        it.inntekt_foer_ufoerhet_vedvirk,
                        it.oppjustert_inntekt_foer_ufoerhet_vedvirk,
                        it.kompensasjonsgrad_ufoeretrygd_vedvirk
                    )
                })
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
/*
            showIf(har_barn_overfoert_til_saerkullsbarn) {
                includePhrase(OmregningFBOverskrift_001)
                includePhrase(
                        argument().map { InfoFBTilSB_001Dto(it.barn_overfoert_til_saerkullsbarn) },
                        InfoFBTilSB_001
                )
                showIf(
                        barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                                and har_barn_som_tidligere_var_saerkullsbarn
                                and not(inntekt_ufoere_endret and har_minsteytelse_vedvirk)
                ) {
                    includePhrase(
                            argument().map { InfoTidligereSB_001Dto(it.tidligere_saerkullsbarn) },
                            InfoTidligereSB_001
                    )
                }

                showIf(
                        barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                                and (inntekt_ufoere_endret or har_minsteytelse_vedvirk)
                                and (barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk or barnetillegg_er_redusert_mot_tak)
                ) {
                    includePhrase(
                            argument().map { InfoTidligereSBOgEndretUT_001Dto(it.tidligere_saerkullsbarn) },
                            InfoTidligereSBOgEndretUT_001
                    )
                }
            }
*/

            showIf(
                har_barnetillegg_for_saerkullsbarn_vedvirk
                        and (
                        har_minsteytelse_vedvirk
                                or inntekt_ufoere_endret
                                or ektefelletillegg_opphoert
                        )
            ) {
                includePhrase(EndringUTpavirkerBTOverskrift_001)

                showIf(not(barnetillegg_er_redusert_mot_tak)) {
                    includePhrase(IkkeRedusBTPgaTak_001, argument().map {
                        IkkeRedusBTPgaTak_001Dto(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                        )
                    })
                }

                showIf(barnetillegg_er_redusert_mot_tak and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    includePhrase(RedusBTPgaTak_001, argument().map {
                        RedusBTPgaTak_001Dto(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_beloep_foer_reduksjon_vedvirk,
                            it.barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk,
                        )
                    })
                }

                showIf(barnetillegg_ikke_utbetalt_pga_tak) {
                    includePhrase(IkkeUtbetaltBTPgaTak_001, argument().map {
                        IkkeUtbetaltBTPgaTak_001Dto(
                            it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk,
                            it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk,
                        )
                    })
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
                    includePhrase(IkkeRedusBTSBPgaInntekt_001, argument().map {
                        IkkeRedusBTSBPgaInntekt_001Dto(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                        )
                    })
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and (har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                            or (not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk) and har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk))
                ) {
                    includePhrase(RedusBTSBPgaInntekt_001, argument().map {
                        RedusBTSBPgaInntekt_001Dto(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk
                        )
                    })
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
                    includePhrase(IkkeUtbetaltBTSBPgaInntekt_001, argument().map {
                        IkkeUtbetaltBTSBPgaInntekt_001Dto(
                            it.barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk,
                            it.barnetillegg_saerkullsbarn_inntektstak_vedvirk
                        )
                    })
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
                includePhrase(GjRettSamboerOverskrift, argument().map { GjRettSamboerOverskriftDto(it.avdod_navn) })
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
                includePhrase(VirkTdsPktUT_001, argument().map { VirkTdsPktUT_001Dto(it.krav_virkedato_fom) })
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(har_barn_overfoert_til_saerkullsbarn)
            ) {
                includePhrase(
                    VirkTdsPktUTIkkeEndring_001,
                    argument().map { VirkTdsPktUTIkkeEndring_001Dto(it.krav_virkedato_fom) }
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
                    VirkTdsPktUTBTOmregn_001, argument().map { VirkTdsPktUTBTOmregn_001Dto(it.krav_virkedato_fom) }
                )
            }

            showIf(not(har_ufoeremaaned_vedvirk)) {
                includePhrase(
                    VirkTdsPktUTAvkortetTil0_001,
                    argument().map { VirkTdsPktUTAvkortetTil0_001Dto(it.krav_virkedato_fom) }
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
            it.orienteringOmRettigheterOgPlikter
        }
        )
        includeAttachment(opplysningerBruktIBeregningUT, argument().map {
            it.opplysningerBruktIBeregningUT
        }
        )
    }
}

data class UfoerOmregningEnsligDto(
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterParamDto,
    val avdod_navn: String,
    val avdod_sivilstand: Sivilstand,
    val barn_overfoert_til_saerkullsbarn: List<String>,
    val barnetillegg_beloep_foer_reduksjon_vedvirk: Number,
    val barnetillegg_er_redusert_mot_tak: Boolean,
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
    val barnetillegg_ikke_utbetalt_pga_tak: Boolean,
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
    val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk: Number,
    val barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk: Boolean,
    val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Number,
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Number,
    val barnetillegg_saerkullsbarn_inntektstak_vedvirk: Number,
    val bor_i_avtaleland: Boolean,
    val bor_i_norge: Boolean,
    val ektefelletillegg_opphoert: Boolean,
    val eps_bor_sammen_med_bruker_gjeldende: Boolean,
    val eps_institusjon_gjeldende: Institusjon,
    val gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt: Boolean,
    val gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet: Boolean,
    val har_barn_overfoert_til_saerkullsbarn: Boolean,
    val har_barn_som_tidligere_var_saerkullsbarn: Boolean,
    val har_barnetillegg_felles_barn_vedvirk: Boolean,
    val har_barnetillegg_for_saerkullsbarn: Boolean,
    val har_barnetillegg_for_saerkullsbarn_vedvirk: Boolean,
    val har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk: Boolean,
    val har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk: Boolean,
    val har_barnetillegg_saerkullsbarn_vedvirk: Boolean,
    val har_barnetillegg_vedvirk: Boolean,
    val har_ektefelletillegg_vedvirk: Boolean,
    val har_felles_barn_uten_barnetillegg_med_avdod: Boolean,
    val har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd: Boolean,
    val har_flere_ufoeretrygd_perioder: Boolean,
    val har_minste_inntektsnivaa_foer_ufoeretrygd: Boolean,
    val har_minsteytelse_vedvirk: Boolean,
    val har_ufoeremaaned_vedvirk: Boolean,
    val inntekt_foer_ufoerhet_vedvirk: Number,
    val inntekt_ufoere_endret: Boolean,
    val institusjon_gjeldende: Institusjon,
    val institusjon_vedvirk: Institusjon,
    val kompensasjonsgrad_ufoeretrygd_vedvirk: Number,
    val krav_virkedato_fom: LocalDate,
    val minsteytelse_sats_vedvirk: Number,
    val oppjustert_inntekt_foer_ufoerhet_vedvirk: Number,
    val saktype: Sakstype,
    val sivilstand: Sivilstand,
    val tidligere_saerkullsbarn: List<String>,
    val total_ufoeremaaneder: Number,
    val ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet: Boolean,
    val ufoeretrygd_vedvirk_er_inntektsavkortet: Boolean,
) {
    constructor() : this(
        opplysningerBruktIBeregningUT = OpplysningerBruktIBeregningUTDto(
            anvendtTT_trygdetidsdetaljerGjeldende = 0,
            avkortningsbelopAr_barnetilleggSBGjeldende = 0,
            belop_barnetilleggSBGjeldende = 0,
            belopAr_barnetilleggSBGjeldende = 0,
            belopArForAvkort_barnetilleggSBGjeldende = 0,
            belopIEU_inntektEtterUforeGjeldende = 0,
            belopsgrense_uforetrygdGjeldende = 0,
            beregningsgrunnlagBelopAr_uforetrygdGjeldende = 0,
            beregningsgrunnlagBelopAr_yrkesskadeGjeldende = 0,
            brukersSivilstand_gjeldendeBeregnetUTPerManed = "",
            faktiskTTBilateral_trygdetidsdetaljerGjeldende = 0,
            faktiskTTEOS_trygdetidsdetaljerGjeldende = 0,
            faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
            faktiskTTNorge_trygdetidsdetaljerGjeldende = 0,
            forventetInntektAr_inntektsAvkortingGjeldende = 0,
            framtidigTTNorsk_trygdetidsdetaljerGjeldende = 0,
            fribelop_barnetilleggSBGjeldende = 0,
            gradertOIFU_barnetilleggGrunnlagGjeldende = 0,
            grunnbelop_gjeldendeBeregnetUTPerManed = 0,
            ifuInntekt_inntektForUforeGjeldende = 0,
            inntektBruktIAvkortning_barnetilleggSBGjeldende = 0,
            inntektstak_barnetilleggSBGjeldende = 0,
            inntektsgrenseAr_inntektsAvkortingGjeldende = 0,
            inntektstak_inntektsAvkortingGjeldende = 0,
            inntektVedSkadetidspunkt_yrkesskadeGjeldende = 0,
            justeringsbelopAr_barnetilleggSBGjeldende = 0,
            kompensasjonsgrad_uforetrygdGjeldende = 0.0,
            nevnerProRata_trygdetidsdetaljerGjeldende = 0,
            nevnerTTEOS_trygdetidsdetaljerGjeldende = 0,
            nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
            prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende = 0,
            samletTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
            skadetidspunkt_yrkesskadeGjeldende = LocalDate.of(2020, 1, 1),
            tellerProRata_trygdetidsdetaljerGjeldende = 0,
            tellerTTEOS_trygdetidsdetaljerGjeldende = 0,
            tellerTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
            totaltAntallBarn_barnetilleggGrunnlagGjeldende = 0,
            uforegrad_uforetrygdGjeldende = 0,
            uforetidspunkt_uforetrygdGjeldende = LocalDate.of(2020, 1, 1),
            virkDatoFom_gjeldendeBeregnetUTPerManed = LocalDate.of(2020, 1, 1),
            yrkesskadegrad_yrkesskadeGjeldende = 0,
        ),
        orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterParamDto(
            sivilstand = ENSLIG,
            bor_i_norge = false,
            institusjon_gjeldende = Institusjon.INGEN,
            eps_bor_sammen_med_bruker_gjeldende = false,
            eps_institusjon_gjeldende = Institusjon.INGEN,
            har_barnetillegg_felles_barn_vedvirk = false,
            har_barnetillegg_for_saerkullsbarn_vedvirk = false,
            har_ektefelletillegg_vedvirk = false,
            saktype = Sakstype.UFOEREP,
            barnetillegg_beloep_gjeldendeBeregnetUTPerManed = 0,
        ),
        avdod_navn = "Avdød Navn",
        avdod_sivilstand = ENSLIG,
        barn_overfoert_til_saerkullsbarn = listOf("barn1", "barn2", "barn3"),
        barnetillegg_beloep_foer_reduksjon_vedvirk = 123,
        barnetillegg_er_redusert_mot_tak = false,
        barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk = 123,
        barnetillegg_ikke_utbetalt_pga_tak = false,
        barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk = 123,
        barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk = 123,
        barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk = false,
        barnetillegg_saerkullsbarn_fribeloep_vedvirk = 123,
        barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk = 123,
        barnetillegg_saerkullsbarn_inntektstak_vedvirk = 123,
        bor_i_avtaleland = false,
        bor_i_norge = false,
        ektefelletillegg_opphoert = false,
        eps_bor_sammen_med_bruker_gjeldende = false,
        eps_institusjon_gjeldende = Institusjon.INGEN,
        gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt = false,
        gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet = false,
        har_barn_overfoert_til_saerkullsbarn = false,
        har_barn_som_tidligere_var_saerkullsbarn = false,
        har_barnetillegg_felles_barn_vedvirk = false,
        har_barnetillegg_for_saerkullsbarn = false,
        har_barnetillegg_for_saerkullsbarn_vedvirk = false,
        har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk = false,
        har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk = false,
        har_barnetillegg_saerkullsbarn_vedvirk = false,
        har_barnetillegg_vedvirk = false,
        har_ektefelletillegg_vedvirk = false,
        har_felles_barn_uten_barnetillegg_med_avdod = false,
        har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd = false,
        har_flere_ufoeretrygd_perioder = false,
        har_minste_inntektsnivaa_foer_ufoeretrygd = false,
        har_minsteytelse_vedvirk = false,
        har_ufoeremaaned_vedvirk = false,
        inntekt_foer_ufoerhet_vedvirk = 123,
        inntekt_ufoere_endret = false,
        institusjon_gjeldende = Institusjon.INGEN,
        institusjon_vedvirk = Institusjon.INGEN,
        kompensasjonsgrad_ufoeretrygd_vedvirk = 123,
        krav_virkedato_fom = LocalDate.of(2020, 1, 1),
        minsteytelse_sats_vedvirk = 123,
        oppjustert_inntekt_foer_ufoerhet_vedvirk = 123,
        saktype = Sakstype.UFOEREP,
        sivilstand = ENSLIG,
        tidligere_saerkullsbarn = listOf("saerkullsbarn1", "saerkullsbarn2", "saerkullsbarn3"),
        total_ufoeremaaneder = 123,
        ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet = false,
        ufoeretrygd_vedvirk_er_inntektsavkortet = false
    )
}
