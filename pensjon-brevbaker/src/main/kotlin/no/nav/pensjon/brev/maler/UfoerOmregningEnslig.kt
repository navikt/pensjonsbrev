package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
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

// 000073
object UfoerOmregningEnslig : StaticTemplate {
    private fun harMinstytelseVedVirk(dto: UfoerOmregningEnsligDto) = dto.minsteytelseVedvirk_sats != null
    private fun harBarnetilleggForSaerkullsbarnVedVirk(dto: UfoerOmregningEnsligDto) = dto.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk != null
    private fun harBarnOverfoertTilSaerkullsbarn(dto: UfoerOmregningEnsligDto) = dto.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk?.barnOverfoertTilSaerkullsbarn?.isNotEmpty() ?: false

    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true,
        ),
    ) {
        val harMinsteytelseVedVirk = argument().select(UfoerOmregningEnslig::harMinstytelseVedVirk)
        val inntektFoerUfoereErSannsynligEndret = argument().map { it.inntektFoerUfoerhetVedVirk.erSannsynligEndret }

        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har rekna om uføretrygda di",
                English to "NAV has altered your disability benefit",
            )
        }

        outline {
            val ektefelleTilleggOpphoert = argument().map { it.avdoed.ektefelletilleggOpphoert }
            val harBarnetilleggVedVirk = argument().map { it.barnetilleggVedVirk != null }
            val harBarnetilleggForSaerkullsbarnVedVirk = argument().select(UfoerOmregningEnslig::harBarnetilleggForSaerkullsbarnVedVirk)
            val harBarnOverfoertTilSaerkullsbarn = argument().select(UfoerOmregningEnslig::harBarnOverfoertTilSaerkullsbarn)
            val harbarnSomTidligerVarSaerkullsbarn = argument().map {
                it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk?.barnTidligereSaerkullsbarn?.isNotEmpty() ?: false
            }
            val harUfoereMaanedligBeloepVedvirk = argument().map { it.ufoeretrygdVedVirk.totalUfoereMaanedligBeloep.value > 0 }
            val institusjonsoppholdVedVirk = argument().select(UfoerOmregningEnsligDto::institusjonsoppholdVedVirk)
            val barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt = argument().select(UfoerOmregningEnsligDto::barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt)
            val brukerBorINorge = argument().map { it.bruker.borINorge }

            includePhrase(Vedtak.overskrift)

            includePhrase(omregnEPSInnledning, argument().map {
                OmregnEPSInnledningDto(
                    harMinsteytelseVedVirk = harMinstytelseVedVirk(it),
                    inntektFoerUfoereErSannsynligEndret = it.inntektFoerUfoerhetVedVirk.erSannsynligEndret,
                    ektefelletilleggOpphoert = it.avdoed.ektefelletilleggOpphoert,
                    harBarnetilleggForSaerkullsbarnVedVirk = harBarnetilleggForSaerkullsbarnVedVirk(it),
                    harBarnOverfoertTilSaerkullsbarn = harBarnOverfoertTilSaerkullsbarn(it),
                    avdoedNavn = it.avdoed.navn,
                    krav_virkningsDatoFraOgMed = it.krav_virkningsDatoFraOgMed,
                )
            })

            includePhrase(utbetalingUfoeretrygd, argument().map {
                BeloepUTDto(
                    totalUfoereMaanedligBeloep = it.ufoeretrygdVedVirk.totalUfoereMaanedligBeloep,
                    harBarnetilleggForSaerkullsbarnVedVirk = it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk != null,
                    institusjonsoppholdVedVirk = it.institusjonsoppholdVedVirk,
                    harFlereUfoeretrygdPerioder = it.beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak > 1,
                )
            })

            showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret) {
                includePhrase(Vedtak.begrunnelseOverskrift)

                // om minstytelseVedVirk_sats har en verdi så har bruker minsteytelse ved virk.
                ifNotNull(argument().select(UfoerOmregningEnsligDto::minsteytelseVedvirk_sats)) { minsteytelseVedVirkSats ->
                    showIf(inntektFoerUfoereErSannsynligEndret) {
                        includePhrase(endrMYOgMinstIFUDodEPS2_001, map2(minsteytelseVedVirkSats, argument()) { sats, arg ->
                            EndrMYOgMinstIFUDodEPS2_001Dto(
                                minsteytelse_sats_vedvirk = sats,
                                inntekt_foer_ufoerhet_vedvirk = arg.inntektFoerUfoerhetVedVirk.beloep,
                                oppjustert_inntekt_foer_ufoerhet_vedvirk = arg.inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                                kompensasjonsgrad_ufoeretrygd_vedvirk = arg.ufoeretrygdVedVirk.kompensasjonsgrad
                            )
                        })
                    } orShow {
                        includePhrase(endrMYDodEPS2_001, map2(minsteytelseVedVirkSats, argument()) { sats, arg ->
                            EndrMYDodEPS2_001Dto(
                                minsteytelse_sats_vedvirk = sats, kompensasjonsgrad_ufoeretrygd_vedvirk = arg.ufoeretrygdVedVirk.kompensasjonsgrad
                            )
                        })
                    }
                } orShow {
                    // Om bruker ikke har minsteytelse ved virk, så må inntekt før uføre sannsynlig være endret pga. ytre betingelse.

                    includePhrase(endrMinstIFUDodEPS2_001, argument().map {
                        EndrMinstIFUDodEPS2_001Dto(
                            it.inntektFoerUfoerhetVedVirk.beloep, it.inntektFoerUfoerhetVedVirk.oppjustertBeloep, it.ufoeretrygdVedVirk.kompensasjonsgrad
                        )
                    })
                }
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
                val barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr = tillegg.map { it.justeringsbeloepAar.value != 0 }

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(omregningFBOverskrift_001)
                    includePhrase(infoFBTilSB_001, tillegg.map { it.barnOverfoertTilSaerkullsbarn })

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt and harbarnSomTidligerVarSaerkullsbarn and not(inntektFoerUfoereErSannsynligEndret) and not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(infoTidligereSB_001, tillegg.map { it.barnTidligereSaerkullsbarn })
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn and (inntektFoerUfoereErSannsynligEndret or harMinsteytelseVedVirk) and (barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt or barnetilleggErRedusertMotTak)
                    ) {
                        includePhrase(infoTidligereSBOgEndretUT_001, tillegg.map { it.barnTidligereSaerkullsbarn })
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
                ) {
                    includePhrase(endringUTpavirkerBTOverskrift_001)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert))
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
                        not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt) and not(barnetilleggErIkkeUtbetPgaTak)
                    ) {
                        includePhrase(ikkeRedusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            IkkeRedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning, tillegg.fribeloepVedvirk
                            )
                        })
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt and harNettoBeloep or (not(harNettoBeloep) and barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(redusBTSBPgaInntekt_001, tillegg.map { tillegg ->
                            RedusBTSBPgaInntekt_001Dto(
                                tillegg.inntektBruktIAvkortning, tillegg.fribeloepVedvirk
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
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt and not(harNettoBeloep) and not(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(ikkeUtbetaltBTSBPgaInntekt_001, tillegg.map {
                            IkkeUtbetaltBTSBPgaInntekt_001Dto(
                                it.inntektBruktIAvkortning,
                                it.inntektstak,
                            )
                        })
                    }

                    showIf(not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt) and not(grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 })) {
                        includePhrase(hjemmelBT_001)
                    }


                    showIf(not(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt) and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }) {
                        includePhrase(hjemmelBTOvergangsregler_001)
                    }

                    showIf(
                        barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt and not(grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 })
                    ) {
                        includePhrase(hjemmelBTRedus_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirkErRedusertMotInntekt and grunnlag.map { it.prosentsatsGradertOverInntektFoerUfoer <= 95 }) {
                        includePhrase(hjemmelBTRedusOvergangsregler_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnGjeldende_ErRedusertMotInntekt) {
                        includePhrase(merInfoBT_001)
                    }
                }
            }


            val avdoed_sivilstand = argument().map { it.avdoed.sivilstand }
            showIf(avdoed_sivilstand.isOneOf(Sivilstand.SAMBOER3_2)) {
                includePhrase(gjRettSamboerOverskrift, argument().map { it.avdoed.navn })
                includePhrase(gjRettUTSamboer_001)
            }
            showIf(avdoed_sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.PARTNER, Sivilstand.SAMBOER1_5)) {
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
                harUfoereMaanedligBeloepVedvirk and (harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelleTilleggOpphoert)
            ) {
                includePhrase(virkTdsPktUT_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoereErSannsynligEndret) and not(ektefelleTilleggOpphoert) and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(virkTdsPktUTIkkeEndring_001, argument().map { it.krav_virkningsDatoFraOgMed })
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoereErSannsynligEndret) and not(ektefelleTilleggOpphoert) and harBarnOverfoertTilSaerkullsbarn
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

            includePhrase(Felles.meldEndringerPesys_001)
            includePhrase(Felles.rettTilKlagePesys_001)
            includePhrase(Felles.rettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.sjekkUtbetalingene)
            includePhrase(skattekortOverskrift_001)
            includePhrase(skattekortUT_001)

            showIf(not(brukerBorINorge)) {
                includePhrase(skattBorIUtlandPesys_001)
            }
        }

        includeAttachment(maanedligUfoeretrygdFoerSkatt, argument().select(UfoerOmregningEnsligDto::maanedligUfoeretrygdFoerSkatt))

        includeAttachment(
            opplysningerBruktIBeregningUT,
            argument().select(UfoerOmregningEnsligDto::opplysningerBruktIBeregningUT),
            argument().map { it.barnetilleggVedVirk?.barnetilleggSaerkullsbarnVedVirk?.erRedusertMotInntekt == true } or harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret
        )

        includeAttachment(orienteringOmRettigheterOgPlikterUfoere, argument().select(UfoerOmregningEnsligDto::orienteringOmRettigheterOgPlikter))

    }
}