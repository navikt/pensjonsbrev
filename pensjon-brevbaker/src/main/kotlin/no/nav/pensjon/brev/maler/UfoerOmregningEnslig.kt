package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.AvdoedSelectors.ektefelletilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.AvdoedSelectors.harFellesBarnUtenBarnetillegg
import no.nav.pensjon.brev.api.model.maler.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.maler.AvdoedSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.barnOverfoertTilSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.barnOverfoertTilSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.barnTidligereSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.barnTidligereSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.beloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.erRedusertMotInntekt
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.erRedusertMotInntekt_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.fribeloepVedvirk
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.BrukerSelectors.borIAvtaleLand
import no.nav.pensjon.brev.api.model.maler.BrukerSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.InntektFoerUfoerhetVedVirkSelectors.beloep
import no.nav.pensjon.brev.api.model.maler.InntektFoerUfoerhetVedVirkSelectors.erMinsteinntekt
import no.nav.pensjon.brev.api.model.maler.InntektFoerUfoerhetVedVirkSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.maler.InntektFoerUfoerhetVedVirkSelectors.oppjustertBeloep
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.barnetilleggSaerkullsbarnVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.harBarnetillegg
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.inntektFoerUfoerhetVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.krav_virkningsDatoFraOgMed
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.minsteytelseVedvirk_sats
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.ufoeretrygdVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdVedVirkSelectors.erInntektsavkortet
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdVedVirkSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdVedVirkSelectors.totalUfoereMaanedligBeloep
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// 000073
@TemplateModelHelpers
object UfoerOmregningEnslig : VedtaksbrevTemplate<UfoerOmregningEnsligDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UFOER_OMREGNING_ENSLIG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = UfoerOmregningEnsligDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK
        ),
    ) {
        val harMinsteytelseVedVirk = minsteytelseVedvirk_sats.notNull()

        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har rekna om uføretrygda di",
                English to "NAV has altered your disability benefit",
            )
        }

        outline {
            val harBarnetilleggForSaerkullsbarnVedVirk = barnetilleggSaerkullsbarnVedVirk.notNull()
            val harBarnOverfoertTilSaerkullsbarn = barnetilleggSaerkullsbarnVedVirk.barnOverfoertTilSaerkullsbarn_safe.ifNull(emptyList()).isNotEmpty()
            val harbarnSomTidligerVarSaerkullsbarn = barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn_safe.ifNull(emptyList()).isNotEmpty()
            val harUfoereMaanedligBeloepVedvirk = ufoeretrygdVedVirk.totalUfoereMaanedligBeloep.greaterThan(0)

            includePhrase(Vedtak.Overskrift)

            includePhrase(
                OmregnEPSInnledning(
                    harMinsteytelseVedVirk = harMinsteytelseVedVirk,
                    inntektFoerUfoereErSannsynligEndret = inntektFoerUfoerhetVedVirk.erSannsynligEndret,
                    ektefelletilleggOpphoert = avdoed.ektefelletilleggOpphoert,
                    harBarnetilleggForSaerkullsbarnVedVirk = harBarnetilleggForSaerkullsbarnVedVirk,
                    harBarnOverfoertTilSaerkullsbarn = harBarnOverfoertTilSaerkullsbarn,
                    avdoedNavn = avdoed.navn,
                    kravVirkningsDatoFraOgMed = krav_virkningsDatoFraOgMed,
                )
            )

            includePhrase(
                UtbetalingUfoeretrygd(
                    totalUfoereMaanedligBeloep = ufoeretrygdVedVirk.totalUfoereMaanedligBeloep,
                    harBarnetilleggForSaerkullsbarnVedVirk = harBarnetilleggForSaerkullsbarnVedVirk,
                    institusjonsoppholdVedVirk = institusjonsoppholdVedVirk,
                    harFlereUfoeretrygdPerioder = beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak.greaterThan(1),
                )
            )

            showIf(harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret) {
                includePhrase(Vedtak.BegrunnelseOverskrift)

                // om minstytelseVedVirk_sats har en verdi så har bruker minsteytelse ved virk.
                ifNotNull(minsteytelseVedvirk_sats) { minsteytelseVedVirkSats ->
                    showIf(inntektFoerUfoerhetVedVirk.erSannsynligEndret) {
                        includePhrase(
                            EndrMYOgMinstIFUDodEPS2_001(
                                minsteytelse_sats_vedvirk = minsteytelseVedVirkSats,
                                inntekt_foer_ufoerhet_vedvirk = inntektFoerUfoerhetVedVirk.beloep,
                                oppjustert_inntekt_foer_ufoerhet_vedvirk = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                                kompensasjonsgrad_ufoeretrygd_vedvirk = ufoeretrygdVedVirk.kompensasjonsgrad,
                            )
                        )
                    } orShow {
                        includePhrase(
                            EndrMYDodEPS2_001(
                                minsteytelse_sats_vedvirk = minsteytelseVedVirkSats,
                                kompensasjonsgrad_ufoeretrygd_vedvirk = ufoeretrygdVedVirk.kompensasjonsgrad
                            )
                        )
                    }
                } orShow {
                    // Om bruker ikke har minsteytelse ved virk, så må inntekt før uføre sannsynlig være endret pga. ytre betingelse.

                    includePhrase(
                        EndrMinstIFUDodEPS2_001(
                            inntekt_foer_ufoerhet_vedvirk = inntektFoerUfoerhetVedVirk.beloep,
                            oppjustert_inntekt_foer_ufoerhet_vedvirk = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                            kompensasjonsgrad_ufoeretrygd_vedvirk = ufoeretrygdVedVirk.kompensasjonsgrad,
                        )
                    )
                }
            }

            includePhrase(
                HjemmelSivilstandUfoeretrygd(
                    harMinsteinntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.erMinsteinntekt,
                    ufoeretrygdVedvirkErInntektsavkortet = ufoeretrygdVedVirk.erInntektsavkortet,
                )
            )

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(HjemmelEPSDodUTInstitusjon_001)
            }.orShowIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(HjemmelEPSDodUTFengsel_001)
            }

            showIf(avdoed.ektefelletilleggOpphoert) {
                includePhrase(OpphorETOverskrift_001)
                includePhrase(OpphorET_001)
                includePhrase(HjemmelET_001)
            }

            ifNotNull(barnetilleggSaerkullsbarnVedVirk) { barnetilleggSaerkullsbarnVedVirk ->

                val harNettoBeloep = barnetilleggSaerkullsbarnVedVirk.beloep.greaterThan(0)
                val barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr = barnetilleggSaerkullsbarnVedVirk.justeringsbeloepAar.notEqualTo(0)

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(OmregningFBOverskrift_001)
                    includePhrase(InfoFBTilSB_001(barnetilleggSaerkullsbarnVedVirk.barnOverfoertTilSaerkullsbarn))

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and harbarnSomTidligerVarSaerkullsbarn and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(InfoTidligereSB_001(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn and (inntektFoerUfoerhetVedVirk.erSannsynligEndret or harMinsteytelseVedVirk) and barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt
                    ) {
                        includePhrase(InfoTidligereSBOgEndretUT_001(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
                ) {
                    includePhrase(EndringUTpavirkerBTOverskrift_001)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert))
                ) {

                    showIf(not(harBarnOverfoertTilSaerkullsbarn)) {
                        includePhrase(InfoBTSBInntekt_001)
                    }

                    showIf(harBarnOverfoertTilSaerkullsbarn) {
                        includePhrase(InfoBTOverfortTilSBInntekt_001)
                    }

                    showIf(
                        not(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt)
                    ) {
                        includePhrase(
                            IkkeRedusBTSBPgaInntekt_001(
                                barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetillegg_saerkullsbarn_fribeloep_vedvirk = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and harNettoBeloep or (not(harNettoBeloep) and barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(
                            RedusBTSBPgaInntekt_001(
                                barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetillegg_saerkullsbarn_fribeloep_vedvirk = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and harNettoBeloep) {
                        includePhrase(JusterBelopRedusBTPgaInntekt_001)
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and not(harNettoBeloep)) {
                        includePhrase(JusterBelopIkkeUtbetaltBTPgaInntekt_001)
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and not(harNettoBeloep) and not(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(
                            IkkeUtbetaltBTSBPgaInntekt_001(
                                barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetillegg_saerkullsbarn_inntektstak_vedvirk = barnetilleggSaerkullsbarnVedVirk.inntektstak,
                            )
                        )
                    }

                    showIf(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt) {
                        includePhrase(HjemmelBTRedus_001)
                        includePhrase(MerInfoBT_001)
                    } orShow {
                        includePhrase(HjemmelBT_001)
                    }
                }
            }

            showIf(avdoed.sivilstand.isOneOf(Sivilstand.SAMBOER3_2)) {
                includePhrase(GjRettSamboerOverskrift(avdoed.navn))
                includePhrase(GjRettUTSamboer_001)
            }
            showIf(avdoed.sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.PARTNER, Sivilstand.SAMBOER1_5)) {
                includePhrase(RettTilUTGJTOverskrift_001)
                includePhrase(HvemUTGJTVilkar_001)
                includePhrase(HvordanSoekerDuOverskrift_001)
                includePhrase(SoekUTGJT_001)

                showIf(bruker.borIAvtaleLand) {
                    includePhrase(SoekAvtaleLandUT_001)
                }

                includePhrase(AvdodBoddArbUtlandOverskrift_001)
                includePhrase(AvdodBoddArbUtland2_001)
                includePhrase(PensjonFraAndreOverskrift_001)
                includePhrase(InfoAvdodPenFraAndre_001)
            }

            showIf(avdoed.harFellesBarnUtenBarnetillegg) {
                includePhrase(HarBarnUnder18Overskrift_001)
                includePhrase(HarBarnUtenBT_001)
                includePhrase(HarBarnUnder18_001)
            }

            includePhrase(VirknTdsPktOverskrift_001)

            showIf(
                harUfoereMaanedligBeloepVedvirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
            ) {
                includePhrase(VirkTdsPktUT_001(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(avdoed.ektefelletilleggOpphoert) and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(VirkTdsPktUTIkkeEndring_001(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(avdoed.ektefelletilleggOpphoert) and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(VirkTdsPktUTBTOmregn_001(krav_virkningsDatoFraOgMed))
            }

            showIf(not(harUfoereMaanedligBeloepVedvirk)) {
                includePhrase(VirkTdsPktUTAvkortetTil0_001(krav_virkningsDatoFraOgMed))
            }

            includePhrase(MeldInntektUTOverskrift_001)

            showIf(harBarnetillegg) {
                includePhrase(MeldInntektUTBT_001)
            }.orShow {
                includePhrase(MeldInntektUT_001)
            }

            includePhrase(Felles.MeldEndringerPesys_001)
            includePhrase(Felles.RettTilKlagePesys_001)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(SkattekortOverskrift_001)
            includePhrase(SkattekortUT_001)

            showIf(not(bruker.borINorge)) {
                includePhrase(SkattBorIUtlandPesys_001)
            }
        }

        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)


        includeAttachment(
            vedleggOpplysningerBruktIBeregningUT,
            opplysningerBruktIBeregningUT,
            barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt_safe.ifNull(false) or harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret
        )

        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterOgPlikter)
    }
}