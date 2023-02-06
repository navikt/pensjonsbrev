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
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.justeringsbeloepAar_safe
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
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
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
                ifNotNull(minsteytelseVedvirk_sats) { minsteytelseVedVirkSats ->
                    showIf(inntektFoerUfoerhetVedVirk.erSannsynligEndret) {
                        includePhrase(
                            EndringMinsteytelseOgMinstInntektFoerUfoerhetDoedEPS(
                                minsteytelseSats = minsteytelseVedVirkSats,
                                inntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.beloep,
                                oppjustertInntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                                kompensasjonsgradUfoeretrygd = ufoeretrygdVedVirk.kompensasjonsgrad,
                            )
                        )
                    } orShow {
                        includePhrase(
                            EndretMinsteytelseDoedEPS(
                                minsteytelseSatsVedvirk = minsteytelseVedVirkSats,
                                kompensasjonsgradUfoeretrygdVedvirk = ufoeretrygdVedVirk.kompensasjonsgrad
                            )
                        )
                    }
                } orShow {
                    includePhrase(
                        EndretMinstInntektFoerUfoerhetDoedEPS(
                            inntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.beloep,
                            oppjustertInntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                            kompensasjonsgradUfoeretrygd = ufoeretrygdVedVirk.kompensasjonsgrad,
                        )
                    )
                }
            }

            includePhrase(
                HjemmelSivilstandUfoeretrygd(
                    harMinsteinntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.erMinsteinntekt,
                    ufoeretrygdErInntektsavkortet = ufoeretrygdVedVirk.erInntektsavkortet,
                )
            )

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(HjemmelEPSDoedInstitusjonUfoeretrygd)
            }.orShowIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(HjemmelEPSDoedFengselUfoerUfoeretrygd)
            }

            showIf(avdoed.ektefelletilleggOpphoert) {
                includePhrase(OpphoerEktefelletilleggOverskrift)
                includePhrase(OpphoerEktefelletillegg)
                includePhrase(HjemmelEktefelletillegg)
            }

            ifNotNull(barnetilleggSaerkullsbarnVedVirk) { barnetilleggSaerkullsbarnVedVirk ->

                val harNettoBeloep = barnetilleggSaerkullsbarnVedVirk.beloep.greaterThan(0)
                val barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr = barnetilleggSaerkullsbarnVedVirk.justeringsbeloepAar.notEqualTo(0)

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(OmregningFellesbarnOverskrift)
                    includePhrase(InfoFellesbarnTilSaerkullsbarn(barnetilleggSaerkullsbarnVedVirk.barnOverfoertTilSaerkullsbarn))

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and harbarnSomTidligerVarSaerkullsbarn and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(InfoTidligereSaerkullsbarn(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn and (inntektFoerUfoerhetVedVirk.erSannsynligEndret or harMinsteytelseVedVirk) and barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt
                    ) {
                        includePhrase(InfoTidligereSaerkullsbarnOgEndretUfoeretrygd(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
                ) {
                    includePhrase(EndringUfoeretrygdPaavirkerBarnetilleggOverskrift)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert))
                ) {

                    showIf(not(harBarnOverfoertTilSaerkullsbarn)) {
                        includePhrase(InfoBarnetilleggSaerkullsbarnInntekt)
                    }

                    showIf(harBarnOverfoertTilSaerkullsbarn) {
                        includePhrase(InfoBarnetilleggOverfortTilSaerkullsbarnInntekt)
                    }

                    showIf(
                        not(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt)
                    ) {
                        includePhrase(
                            IkkeRedusertBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnFribeloep = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and harNettoBeloep or (not(harNettoBeloep) and barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(
                            RedusertBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnFribeloep = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and harNettoBeloep) {
                        includePhrase(JusterBeloepRedusertBarnetilleggPgaInntekt)
                    }

                    showIf(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr and not(harNettoBeloep)) {
                        includePhrase(IkkeUtbetaltBarnetilleggPgaInntektOgJusteringsbelop )
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and not(harNettoBeloep) and not(barnetilleggForSaerkullsbarnVedvirk_HarjusteringsBeloepAr)
                    ) {
                        includePhrase(
                            IkkeUtbetaltBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnInntektstak = barnetilleggSaerkullsbarnVedVirk.inntektstak,
                            )
                        )
                    }

                    showIf(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt) {
                        includePhrase(HjemmelBarnetilleggRedusert)
                        includePhrase(MerInfoBarnetillegg)
                    } orShow {
                        includePhrase(HjemmelBarnetillegg)
                    }
                }
            }

            showIf(avdoed.sivilstand.isOneOf(Sivilstand.SAMBOER3_2)) {
                includePhrase(GjenlevenderettSamboerOverskrift(avdoed.navn))
                includePhrase(GjenlevenderettUfoeretrygdSamboer)
            }
            showIf(avdoed.sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.PARTNER, Sivilstand.SAMBOER1_5)) {
                includePhrase(RettTilGjenlevendetilleggOverskrift)
                includePhrase(HvemHarRettTilGjenlevendetilleggVilkaar)
                includePhrase(HvordanSoekerDuOverskrift)
                includePhrase(SoekGjenlevendetillegg)

                showIf(bruker.borIAvtaleLand) {
                    includePhrase(SoekGjenlevendetilleggAvtaleland)
                }

                includePhrase(AvdoedBoddArbeidetIUtlandOverskrift)
                includePhrase(AvdoedBoddEllerArbeidetIUtland)
                includePhrase(PensjonFraAndreOverskrift)
                includePhrase(InfoAvdoedPenFraAndre)
            }

            showIf(avdoed.harFellesBarnUtenBarnetillegg) {
                includePhrase(HarBarnUnder18Overskrift)
                includePhrase(HarBarnUtenBarnetillegg)
                includePhrase(HarBarnUnder18)
            }

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)

            showIf(
                harUfoereMaanedligBeloepVedvirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
            ) {
                includePhrase(VirkningstidspunktUfoeretrygd(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(avdoed.ektefelletilleggOpphoert) and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(VirkningstidspunktUfoeretrygdIngenEndring(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk and not(harMinsteytelseVedVirk) and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret) and not(avdoed.ektefelletilleggOpphoert) and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(VirkningstidspunktOmregningBarnetillegg(krav_virkningsDatoFraOgMed))
            }

            showIf(not(harUfoereMaanedligBeloepVedvirk)) {
                includePhrase(VirkningstidspunktUfoeretrygdAvkortetTil0(krav_virkningsDatoFraOgMed))
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)

            showIf(harBarnetillegg) {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektBarnetillegg)
            }.orShow {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(bruker.borINorge))
        }

        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)


    /*    includeAttachment(
            opplysningerBruktIBeregningUT,
            barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt_safe.ifNull(false) or harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret
        ) */

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterOgPlikter)
    }
}