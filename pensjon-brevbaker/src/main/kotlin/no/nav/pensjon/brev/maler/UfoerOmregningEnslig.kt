package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.vedlegg.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.orienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            "Vedtak – omregning til enslig uføretrygdet (automatisk)", isSensitiv = true
        )
    ) {
        val harMinsteytelseVedVirk = argument().map { it.minsteytelseVedvirk_sats != null }
        val inntektFoerUfoereErSannsynligEndret = argument().map { it.inntektFoerUfoerhetVedVirk.erSannsynligEndret }

        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har rekna om uføretrygda di",
                English to "NAV has altered your disability benefit"
            )
        }

        outline {

            val ektefelleTilleggOpphoert = argument().map { it.avdoed.ektefelletilleggOpphoert }

            val harBarnetilleggVedVirk = argument().map { it.barnetilleggVedVirk != null }
            val harBarnetilleggForSaerkullsbarnVedVirk =
                argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk != null }

            val harBarnOverfoertTilSaerkullsbarn =
                argument().map {
                    it.barnetilleggVedVirk
                        ?.barnetilleggSaerkullsbarnVedVirk
                        ?.barnOverfoertTilSaerkullsbarn
                        ?.isNotEmpty() ?: false
                }
            val harbarnSomTidligerVarSaerkullsbarn = argument().map {
                it.barnetilleggVedVirk
                    ?.barnetilleggSaerkullsbarnVedVirk
                    ?.barnTidligereSaerkullsbarn
                    ?.isNotEmpty() ?: false
            }
            val harUfoereMaanedligBeloepVedvirk =
                argument().map { it.ufoeretrygdVedVirk.totalUfoereMaanedligBeloep.value > 0 }
            val harFlereUfoeretrygdPerioder =
                argument().map { it.beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak > 1 }
            val institusjonsoppholdVedVirk = argument().select(UfoerOmregningEnsligDto::institusjonsoppholdVedVirk)
            val barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt =
                argument().map { it.barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt }
            val brukerBorINorge = not(argument().map { it.bruker.borINorge })


            includePhrase(vedtakOverskriftPesys_001)
            showIf(
                (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
            ) {
                includePhrase(omregnUTDodEPSInnledn1_001,
                    argument().map { OmregnUTDodEPSInnledn1001Dto(it.avdoed.navn, it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
            ) {
                includePhrase(omregnUTDodEPSInnledn2_001, argument().map { it.avdoed.navn })
            }

            showIf(
                (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                        and harBarnetilleggForSaerkullsbarnVedVirk
            ) {
                includePhrase(omregnUTBTDodEPSInnledn_001,
                    argument().map { OmregnUTBTDodEPSInnledn_001Dto(it.avdoed.navn, it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnetilleggForSaerkullsbarnVedVirk
                        and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(omregnUTBTSBDodEPSInnledn_001, argument().map { it.avdoed.navn })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(omregnBTDodEPSInnledn_001,
                    argument().map { OmregnBTDodEPSInnledn_001Dto(it.avdoed.navn, it.krav_virkningsDatoFraOgMed) })
            }

            includePhrase(beloepUT, argument().map {
                BeloepUTDto(
                    totalUfoereMaanedligBeloep = it.ufoeretrygdVedVirk.totalUfoereMaanedligBeloep,
                    harBarnetilleggForSaerkullsbarnVedVirk = it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk != null,
                    harFlereUfoeretrygdPerioder = it.beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak > 1,
                )
            })

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and not(harFlereUfoeretrygdPerioder)
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(belopUTIngenUtbetaling_001)
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and harFlereUfoeretrygdPerioder
                        and not(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL))
            ) {
                includePhrase(belopUTIngenUtbetalingVedlegg_001)
            }

            ifNotNull(argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk }) { barnetillegg ->
                showIf(not(harUfoereMaanedligBeloepVedvirk) and barnetillegg.map { it.beloep.value == 0 }) {
                    showIf(harFlereUfoeretrygdPerioder) {
                        includePhrase(belopUTBTIngenUtbetalingVedlegg_001)
                    }.orShow {
                        includePhrase(belopUTBTIngenUtbetaling_001)
                    }
                }
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and not(harFlereUfoeretrygdPerioder)
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(belopUTIngenUtbetalingFengsel_001)
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and harFlereUfoeretrygdPerioder
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(belopUTIngenUtbetalingFengselVedlegg_001)
            }

            showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret) {
                includePhrase(begrunnOverskrift_001)
            }

            ifNotNull(argument().map { arg ->
                arg.minsteytelseVedvirk_sats?.let { EndrMYDodEPS2_001Dto(it, arg.ufoeretrygdVedVirk.kompensasjonsgrad) }
            }) {
                showIf(not(inntektFoerUfoereErSannsynligEndret)) {
                    includePhrase(endrMYDodEPS2_001, it)
                }
            }

            ifNotNull(argument().map { arg ->
                arg.minsteytelseVedvirk_sats?.let {
                    EndrMYOgMinstIFUDodEPS2_001Dto(
                        it,
                        arg.inntektFoerUfoerhetVedVirk.beloep,
                        arg.inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                        arg.ufoeretrygdVedVirk.kompensasjonsgrad
                    )
                }
            }) {
                showIf(inntektFoerUfoereErSannsynligEndret) {
                    includePhrase(endrMYOgMinstIFUDodEPS2_001, it)
                }
            }


            showIf(not(harMinsteytelseVedVirk) and inntektFoerUfoereErSannsynligEndret) {
                includePhrase(endrMinstIFUDodEPS2_001, argument().map {
                    EndrMinstIFUDodEPS2_001Dto(
                        it.inntektFoerUfoerhetVedVirk.beloep,
                        it.inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                        it.ufoeretrygdVedVirk.kompensasjonsgrad
                    )
                })
            }


            includePhrase(hjemmelSivilstandUfoeretrygd, argument().map {
                HjemmelSivilstandUfoeretrygdDto(
                    it.inntektFoerUfoerhetVedVirk.erMinsteinntekt, it.ufoeretrygdVedVirk.erInntektsavkortet
                )
            })

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(hjemmelEPSDodUTInstitusjon_001)
            }.orShowIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(hjemmelEPSDodUTFengsel_001)
            }

            showIf(ektefelleTilleggOpphoert) {
                includePhrase(opphorETOverskrift_001)
                includePhrase(opphorET_001)
                includePhrase(hjemmelET_001)
            }

            ifNotNull(
                argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk },
                argument().map { it.barnetilleggVedVirk?.barnetilleggGrunnlag },
            ) { tillegg, grunnlag ->

                val barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt = tillegg.map { it.erRedusertMotInntekt }
                val barnetilleggErRedusertMotTak = grunnlag.map { it.erRedusertMotTak }
                val barnetilleggErIkkeUtbetPgaTak = grunnlag.map { it.erIkkeUtbetaltPgaTak }
                val harNettoBeloep = tillegg.map { it.beloep.value > 0 }
                val barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr =
                    tillegg.map { it.justeringsbeloepAar.value != 0 }

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(omregningFBOverskrift_001)
                    includePhrase(infoFBTilSB_001, tillegg.map { it.barnOverfoertTilSaerkullsbarn })

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and harbarnSomTidligerVarSaerkullsbarn
                                and not(inntektFoerUfoereErSannsynligEndret)
                                and not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(
                            infoTidligereSB_001,
                            tillegg.map { it.barnTidligereSaerkullsbarn }
                        )
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn
                                and (inntektFoerUfoereErSannsynligEndret or harMinsteytelseVedVirk)
                                and (barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt or barnetilleggErRedusertMotTak)
                    ) {
                        includePhrase(
                            infoTidligereSBOgEndretUT_001,
                            tillegg.map { it.barnTidligereSaerkullsbarn }
                        )
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk
                            and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                ) {
                    includePhrase(endringUTpavirkerBTOverskrift_001)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (
                            harBarnetilleggForSaerkullsbarnVedVirk
                                    and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert))
                ) {

                    showIf(not(barnetilleggErRedusertMotTak)) {
                        includePhrase(ikkeRedusBTPgaTak_001, grunnlag.map {
                            IkkeRedusBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                            )
                        })
                    }

                    showIf(barnetilleggErRedusertMotTak and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(redusBTPgaTak_001, grunnlag.map {
                            RedusBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                                it.beloepFoerReduksjon,
                                it.beloepEtterReduksjon,
                            )
                        })
                    }

                    showIf(barnetilleggErIkkeUtbetPgaTak) {
                        includePhrase(ikkeUtbetaltBTPgaTak_001, grunnlag.map {
                            IkkeUtbetaltBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                            )
                        })
                    }


                    showIf(not(harBarnOverfoertTilSaerkullsbarn) and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(infoBTSBInntekt_001)
                    }

                    showIf(harBarnOverfoertTilSaerkullsbarn and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(infoBTOverfortTilSBInntekt_001)
                    }

                    showIf(
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt)
                                and not(barnetilleggErIkkeUtbetPgaTak)
                    ) {
                        includePhrase(ikkeRedusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            IkkeRedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning,
                                tillegg.fribeloepVedvirk
                            )
                        })
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and harNettoBeloep
                                or (not(harNettoBeloep) and barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(redusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            RedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning,
                                tillegg.fribeloepVedvirk
                            )
                        })
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and harNettoBeloep) {
                        includePhrase(justerBelopRedusBTPgaInntekt_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and not(harNettoBeloep)) {
                        includePhrase(justerBelopIkkeUtbetaltBTPgaInntekt_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and not(harNettoBeloep)
                                and not(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(ikkeUtbetaltBTSBPgaInntekt_001, tillegg.map {
                            IkkeUtbetaltBTSBPgaInntekt_001Dto(
                                it.inntektBruktIAvkortning,
                                it.inntektstak,
                            )
                        })
                    }

                    showIf(
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt)
                                and not(grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }
                        )
                    ) {
                        includePhrase(hjemmelBT_001)
                    }


                    showIf(
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt)
                                and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }
                    ) {
                        includePhrase(hjemmelBTOvergangsregler_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and not(grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 })
                    ) {
                        includePhrase(hjemmelBTRedus_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }
                    ) {
                        includePhrase(hjemmelBTRedusOvergangsregler_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt) {
                        includePhrase(merInfoBT_001)
                    }
                }
            }


            val avdod_sivilstand = argument().map { it.avdoed.sivilstand }
            showIf(avdod_sivilstand.isOneOf(Sivilstand.SAMBOER3_2)) {
                includePhrase(gjRettSamboerOverskrift, argument().map { it.avdoed.navn })
                includePhrase(gjRettUTSamboer_001)
            }
            showIf(avdod_sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.PARTNER, Sivilstand.SAMBOER1_5)) {
                includePhrase(rettTilUTGJTOverskrift_001)
                includePhrase(hvemUTGJTVilkar_001)
                includePhrase(hvordanSoekerDuOverskrift_001)
                includePhrase(soekUTGJT_001)

                showIf(argument().map { it.bruker.borIAvtaleLand }) {
                    includePhrase(soekAvtaleLandUT_001)
                }

                includePhrase(avdodBoddArbUtlandOverskrift_001)
                includePhrase(avdodBoddArbUtland2_001)
                includePhrase(pensjonFraAndreOverskrift_001)
                includePhrase(infoAvdodPenFraAndre_001)
            }

            showIf(argument().map { it.avdoed.harFellesBarnUtenBarnetillegg }) {
                includePhrase(harBarnUnder18Overskrift_001)
                includePhrase(harBarnUtenBT_001)
                includePhrase(harBarnUnder18_001)
            }

            includePhrase(virknTdsPktOverskrift_001)

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
            ) {
                includePhrase(virkTdsPktUT_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(virkTdsPktUTIkkeEndring_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(virkTdsPktUTBTOmregn_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            showIf(not(harUfoereMaanedligBeloepVedvirk)) {
                includePhrase(virkTdsPktUTAvkortetTil0_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            includePhrase(meldInntektUTOverskrift_001)

            showIf(harBarnetilleggVedVirk) {
                includePhrase(meldInntektUTBT_001)
            }.orShow {
                includePhrase(meldInntektUT_001)
            }

            includePhrase(meldEndringerPesys_001)
            includePhrase(rettTilKlagePesys_001)
            includePhrase(rettTilInnsynPesys_001)
            includePhrase(sjekkUtbetalingeneOverskrift_001)
            includePhrase(sjekkUtbetalingeneUT_001)
            includePhrase(skattekortOverskrift_001)
            includePhrase(skattekortUT_001)

            showIf(brukerBorINorge) {
                includePhrase(skattBorIUtlandPesys_001)
            }
        }

        includeAttachment(maanedligUfoeretrygdFoerSkatt, argument().map { it.maanedligUfoeretrygdFoerSkatt })

        includeAttachment(opplysningerBruktIBeregningUT, argument().map { it.opplysningerBruktIBeregningUT },
            argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk?.erRedusertMotInntekt == true }
                    or harMinsteytelseVedVirk
                    or inntektFoerUfoereErSannsynligEndret
        )

        includeAttachment(
            orienteringOmRettigheterOgPlikterUfoere,
            argument().map { it.orienteringOmRettigheterOgPlikter })

    }
}