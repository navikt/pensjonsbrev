package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.vedlegg.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.orienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.dineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

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
            "Vedtak – omregning til enslig uføretrygdet (automatisk)", isSensitiv = true
        )
    ) {
        outline {

            val harMinsteytelseVedVirk = argument().map { it.minsteytelseVedvirk_sats != null }
            val inntektFoerUfoereErSannsynligEndret =
                argument().map { it.inntektFoerUfoerhetVedVirk.erSannsynligEndret }
            val ektefelleTilleggOpphoert = argument().map { it.avdod.ektefelletilleggOpphoert }

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
            val harUfoereMaanedligBeloepVedvirk = argument().map { it.ufoeretrygdVedVirk.totalUforeMaanedligBeloep > 0 }
            val harFlereUfoeretrygdPerioder =
                argument().map { it.beregnetUTPerManed_antallBeregningsperioderPaVedtak > 1 }
            val institusjonsoppholdVedVirk = argument().select(UfoerOmregningEnsligDto::institusjonsoppholdVedVirk)
            val barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt =
                argument().map { it.barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt }
            val brukerBorINorge = not(argument().map { it.bruker.borINorge })


            includePhrase(VedtakOverskriftPesys_001)
            showIf(
                (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
            ) {
                includePhrase(OmregnUTDodEPSInnledn1_001,
                    argument().map { OmregnUTDodEPSInnledn1001Dto(it.avdod.navn, it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
            ) {
                includePhrase(OmregnUTDodEPSInnledn2_001, argument().map { it.avdod.navn })
            }

            showIf(
                (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                        and harBarnetilleggForSaerkullsbarnVedVirk
            ) {
                includePhrase(OmregnUTBTDodEPSInnledn_001,
                    argument().map { OmregnUTBTDodEPSInnledn_001Dto(it.avdod.navn, it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnetilleggForSaerkullsbarnVedVirk
                        and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(OmregnUTBTSBDodEPSInnledn_001, argument().map { it.avdod.navn })
            }

            showIf(
                not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(OmregnBTDodEPSInnledn_001,
                    argument().map { OmregnBTDodEPSInnledn_001Dto(it.avdod.navn, it.krav_virkningsDatoFraOgMed) })
            }

            includePhrase(BeloepUT, argument().map {
                BeloepUTDto(
                    totalUforeMaanedligBeloep = it.ufoeretrygdVedVirk.totalUforeMaanedligBeloep,
                    harBarnetilleggForSaerkullsbarnVedVirk = it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk != null,
                    harFlereUfoeretrygdPerioder = it.beregnetUTPerManed_antallBeregningsperioderPaVedtak > 1,
                )
            })

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and not(harFlereUfoeretrygdPerioder)
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetaling_001)
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and harFlereUfoeretrygdPerioder
                        and not(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL))
            ) {
                includePhrase(BelopUTIngenUtbetalingVedlegg_001)
            }

            ifNotNull(argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk }) { barnetillegg ->
                showIf(not(harUfoereMaanedligBeloepVedvirk) and barnetillegg.map { it.belop == 0 }) {
                    showIf(harFlereUfoeretrygdPerioder) {
                        includePhrase(BelopUTBTIngenUtbetalingVedlegg_001)
                    }.orShow {
                        includePhrase(BelopUTBTIngenUtbetaling_001)
                    }
                }
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and not(harFlereUfoeretrygdPerioder)
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengsel_001)
            }

            showIf(
                not(harUfoereMaanedligBeloepVedvirk)
                        and not(harBarnetilleggForSaerkullsbarnVedVirk)
                        and harFlereUfoeretrygdPerioder
                        and institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)
            ) {
                includePhrase(BelopUTIngenUtbetalingFengselVedlegg_001)
            }

            showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret) {
                includePhrase(BegrunnOverskrift_001)
            }

            ifNotNull(argument().map { arg ->
                arg.minsteytelseVedvirk_sats?.let { EndrMYDodEPS2_001Dto(it, arg.ufoeretrygdVedVirk.kompensasjonsgrad) }
            }) {
                showIf(not(inntektFoerUfoereErSannsynligEndret)) {
                    includePhrase(EndrMYDodEPS2_001, it)
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
                    includePhrase(EndrMYOgMinstIFUDodEPS2_001, it)
                }
            }


            showIf(not(harMinsteytelseVedVirk) and inntektFoerUfoereErSannsynligEndret) {
                includePhrase(EndrMinstIFUDodEPS2_001, argument().map {
                    EndrMinstIFUDodEPS2_001Dto(
                        it.inntektFoerUfoerhetVedVirk.beloep,
                        it.inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                        it.ufoeretrygdVedVirk.kompensasjonsgrad
                    )
                })
            }


            includePhrase(HjemmelSivilstandUfoeretrygd, argument().map {
                HjemmelSivilstandUfoeretrygdDto(
                    it.inntektFoerUfoerhetVedVirk.erMinsteinntekt, it.ufoeretrygdVedVirk.erInntektsavkortet
                )
            })

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(HjemmelEPSDodUTInstitusjon_001)
            }.orShowIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(HjemmelEPSDodUTFengsel_001)
            }

            showIf(ektefelleTilleggOpphoert) {
                includePhrase(OpphorETOverskrift_001)
                includePhrase(OpphorET_001)
                includePhrase(HjemmelET_001)
            }

            ifNotNull(
                argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk },
                argument().map { it.barnetilleggVedVirk?.barnetilleggGrunnlag },
            ) { tillegg, grunnlag ->

                val barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt = tillegg.map { it.erRedusertMotInntekt }
                val barnetilleggErRedusertMotTak = grunnlag.map { it.erRedusertMotTak }
                val barnetilleggErIkkeUtbetPgaTak = grunnlag.map { it.erIkkeUtbetPgaTak }
                val harNettoBeloep = tillegg.map { it.belop > 0 }
                val barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr =
                    tillegg.map { it.justeringsbeloepAr != 0 }

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(OmregningFBOverskrift_001)
                    includePhrase(InfoFBTilSB_001, tillegg.map { it.barnOverfoertTilSaerkullsbarn })

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and harbarnSomTidligerVarSaerkullsbarn
                                and not(inntektFoerUfoereErSannsynligEndret)
                                and/*TODO Denne bør gjøres om til and i metaforce også*/ not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(
                            InfoTidligereSB_001,
                            tillegg.map { it.barnTidligereSaerkullsbarn }
                        )
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn
                                and (inntektFoerUfoereErSannsynligEndret or harMinsteytelseVedVirk)
                                and (barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt or barnetilleggErRedusertMotTak)
                    ) {
                        includePhrase(
                            InfoTidligereSBOgEndretUT_001,
                            tillegg.map { it.barnTidligereSaerkullsbarn }
                        )
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk
                            and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                ) {
                    includePhrase(EndringUTpavirkerBTOverskrift_001)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (
                            harBarnetilleggForSaerkullsbarnVedVirk
                                    and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert))
                ) {

                    showIf(not(barnetilleggErRedusertMotTak)) {
                        includePhrase(IkkeRedusBTPgaTak_001, grunnlag.map {
                            IkkeRedusBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                            )
                        })
                    }

                    showIf(barnetilleggErRedusertMotTak and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(RedusBTPgaTak_001, grunnlag.map {
                            RedusBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                                it.belopFoerReduksjon,
                                it.belopEtterReduksjon,
                            )
                        })
                    }

                    showIf(barnetilleggErIkkeUtbetPgaTak) {
                        includePhrase(IkkeUtbetaltBTPgaTak_001, grunnlag.map {
                            IkkeUtbetaltBTPgaTak_001Dto(
                                it.prosentsatsGradertOverInntektFoerUfoer,
                                it.gradertOverInntektFoerUfoer,
                            )
                        })
                    }


                    showIf(not(harBarnOverfoertTilSaerkullsbarn) and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(InfoBTSBInntekt_001)
                    }

                    showIf(harBarnOverfoertTilSaerkullsbarn and not(barnetilleggErIkkeUtbetPgaTak)) {
                        includePhrase(InfoBTOverfortTilSBInntekt_001)
                    }

                    showIf(
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt)
                                and not(barnetilleggErIkkeUtbetPgaTak)
                    ) {
                        includePhrase(IkkeRedusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            IkkeRedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning, tillegg.fribeloepVedvirk
                            )
                        })
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and harNettoBeloep
                                or (not(harNettoBeloep) and barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(RedusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            RedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning, tillegg.fribeloepVedvirk
                            )
                        })
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and harNettoBeloep) {
                        includePhrase(JusterBelopRedusBTPgaInntekt_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and not(harNettoBeloep)) {
                        includePhrase(JusterBelopIkkeUtbetaltBTPgaInntekt_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and not(harNettoBeloep)
                                and not(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(IkkeUtbetaltBTSBPgaInntekt_001, tillegg.map {
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
                        includePhrase(HjemmelBT_001)
                    }


                    showIf(
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt)
                                and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }
                    ) {
                        includePhrase(HjemmelBTOvergangsregler_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and not(grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 })
                    ) {
                        includePhrase(HjemmelBTRedus_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt
                                and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }
                    ) {
                        includePhrase(HjemmelBTRedusOvergangsregler_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt) {
                        includePhrase(MerInfoBT_001)
                    }
                }
            }


            val avdod_sivilstand = argument().map { it.avdod.sivilstand }
            showIf(avdod_sivilstand.isOneOf(Sivilstand.SAMBOER3_2)) {
                includePhrase(GjRettSamboerOverskrift, argument().map { it.avdod.navn })
                includePhrase(GjRettUTSamboer_001)
            }
            showIf(avdod_sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.PARTNER, Sivilstand.SAMBOER1_5)) {
                includePhrase(RettTilUTGJTOverskrift_001)
                includePhrase(HvemUTGJTVilkar_001)
                includePhrase(HvordanSoekerDuOverskrift_001)
                includePhrase(SoekUTGJT_001)

                showIf(argument().map { it.bruker.borIAvtaleLand }) {
                    includePhrase(SoekAvtaleLandUT_001)
                }

                includePhrase(AvdodBoddArbUtlandOverskrift_001)
                includePhrase(AvdodBoddArbUtland2_001)
                includePhrase(PensjonFraAndreOverskrift_001)
                includePhrase(InfoAvdodPenFraAndre_001)
            }

            showIf(argument().map { it.avdod.harFellesBarnUtenBarnetillegg }) {
                includePhrase(HarBarnUnder18Overskrift_001)
                includePhrase(HarBarnUtenBT_001)
                includePhrase(HarBarnUnder18_001)
            }

            includePhrase(VirknTdsPktOverskrift_001)

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
            ) {
                includePhrase(VirkTdsPktUT_001, argument().map { VirkTdsPktUT_001Dto(it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(VirkTdsPktUTIkkeEndring_001,
                    argument().map { VirkTdsPktUTIkkeEndring_001Dto(it.krav_virkningsDatoFraOgMed) })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoereErSannsynligEndret)
                        and not(ektefelleTilleggOpphoert)
                        and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(VirkTdsPktUTBTOmregn_001,
                    argument().map { VirkTdsPktUTBTOmregn_001Dto(it.krav_virkningsDatoFraOgMed) })
            }

            showIf(not(harUfoereMaanedligBeloepVedvirk)) {
                includePhrase(VirkTdsPktUTAvkortetTil0_001,
                    argument().map { VirkTdsPktUTAvkortetTil0_001Dto(it.krav_virkningsDatoFraOgMed) })
            }

            includePhrase(MeldInntektUTOverskrift_001)

            showIf(harBarnetilleggVedVirk) {
                includePhrase(MeldInntektUTBT_001)
            }.orShow {
                includePhrase(MeldInntektUT_001)
            }

            includePhrase(MeldEndringerPesys_001)
            includePhrase(RettTilKlagePesys_001)
            includePhrase(RettTilInnsynPesys_001)
            includePhrase(SjekkUtbetalingeneOverskrift_001)
            includePhrase(SjekkUtbetalingeneUT_001)
            includePhrase(SkattekortOverskrift_001)
            includePhrase(SkattekortUT_001)

            showIf(brukerBorINorge) {
                includePhrase(SkattBorIUtlandPesys_001)
            }
        }

        includeAttachment(
            orienteringOmRettigheterOgPlikterUfoere,
            argument().map { it.orienteringOmRettigheterOgPlikter })
        includeAttachment(opplysningerBruktIBeregningUT, argument().map { it.opplysningerBruktIBeregningUT })
        includeAttachment(maanedligUfoeretrygdFoerSkatt, argument().map { it.maanedligUfoeretrygdFoerSkatt })
    }
}