package no.nav.pensjon.brev.maler.ufore.barnetillegg

import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.selectors.barnetilleggFellesbarn.*
import no.nav.pensjon.brev.api.model.maler.selectors.barnetilleggSaerkullsbarn.*
import no.nav.pensjon.brev.api.model.maler.selectors.opphoerBarnetilleggAutoDto.*
import no.nav.pensjon.brev.api.model.maler.selectors.ufoeretrygd.ektefelletilleggUtbeltalt
import no.nav.pensjon.brev.api.model.maler.selectors.ufoeretrygd.gjenlevendetilleggUtbetalt
import no.nav.pensjon.brev.api.model.maler.selectors.ufoeretrygd.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.selectors.ufoeretrygd.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.ufore.fraser.Barnetillegg
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createVedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerType.kroner.value

// BrevKode: PE_UT_07_200
@TemplateModelHelpers
object OpphoerBarnetilleggAuto : AutobrevTemplate<OpphoerBarnetilleggAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_OPPHOER_BT_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget (automatisk)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        val harBarnetillegg = harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn
        val harOpphoertBarnetilleggForFlereBarn =
            foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)
        title {
            showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                text(
                    bokmal { + "Nav har endret barnetillegget ditt" },
                    nynorsk { + "Nav har endra barnetillegget ditt" },
                )
            }.orShow {
                text(
                    bokmal { + "Nav har vedtatt at barnetillegget ditt opphører" },
                    nynorsk { + "Nav har stansa barnetillegget ditt" },
                )
            }
        }
        outline {
            paragraph {
                val virkningsDato = oensketVirkningsDato.format()
                val barnFlertall = foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)

                text(
                    bokmal { + "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " født" },
                    nynorsk { + "Vi har stansa barnetillegget i uføretrygda di frå " + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " fødd" },
                )
                includePhrase(Felles.TextOrList(foedselsdatoPaaBarnMedOpphoertBarnetillegg.map(LocalizedFormatter.DateFormat)))
            }

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = ufoeretrygd.utbetaltPerMaaned,
                    ufoeretrygd = ufoeretrygd.harUtbetalingsgrad,
                    ektefelle = ufoeretrygd.ektefelletilleggUtbeltalt.notNull(),
                    gjenlevende = ufoeretrygd.gjenlevendetilleggUtbetalt.notNull(),
                    fellesbarn = barnetilleggFellesbarn.safe { beloepNetto }.ifNull(Kroner(0)).greaterThan(0),
                    saerkullsbarn = barnetilleggSaerkullsbarn.safe { beloepNetto }.ifNull(Kroner(0)).greaterThan(0),
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(ufoeretrygd.utbetaltPerMaaned.greaterThan(0)))
            includePhrase(Vedtak.BegrunnelseOverskrift())

            paragraph {
                val barnFlertall = foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)
                text(
                    bokmal { + "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi " +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år." },
                    nynorsk { + "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi " +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år." },
                )
            }

            paragraph {
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12" },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12" },
                    )
                }.orShow {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, og 22-12." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Dette er virkningstidspunktet ditt" },
                    nynorsk { +"Dette er verknadstidspunktet ditt" },
                )
            }

            paragraph {
                val dato = oensketVirkningsDato.format()
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    text(
                        bokmal { + "Barnetillegget ditt er blitt endret fra " + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet." },
                        nynorsk { + "Barnetillegget ditt er endra frå " + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet." },
                    )
                } orShow {
                    text(
                        bokmal { + "Barnetillegget ditt har opphørt fra " + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet." },
                        nynorsk { + "Barnetillegget ditt er stansa frå " + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet." },
                    )
                }
            }

            showIf(harBarnetillegg) {
                title1 {
                    text(
                        bokmal { + "Slik påvirker inntekt barnetillegget ditt" },
                        nynorsk { + "Slik verkar inntekt inn på barnetillegget ditt" },
                    )
                }

                ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                    showIf(harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)) {
                        paragraph {
                            text(
                                bokmal { + "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                                nynorsk { + "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                            )
                            showIf(barnetilleggSaerkullsbarn.beloepNetto.greaterThan(0)) {
                                text(
                                    bokmal { + " Denne grensen kaller vi for fribeløp." },
                                    nynorsk { + " Denne grensa kallar vi for fribeløp." },
                                )
                            }
                            ifNotNull(barnetilleggSaerkullsbarn.brukerBorMed) { borMedSivilstand ->
                                text(
                                    bokmal { + " Inntekten til " + borMedSivilstand.bestemtForm() + " din har ikke betydning for størrelsen på barnetillegget." },
                                    nynorsk { + " Inntekta til " + borMedSivilstand.bestemtForm() + " din har ikkje noko å seie for storleiken på barnetillegget." },
                                )
                            }
                        }
                    }
                }

                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesBarn ->
                    includePhrase(
                        Barnetillegg.InntektHarBetydningForFellesbarnTillegg(
                            faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesBarn.beloepNetto.greaterThan(0),
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            borMedSivilstand = barnetilleggFellesBarn.brukerBorMed,
                            barnetilleggSaerkullsbarnGjelderFlereBarn = barnetilleggSaerkullsbarn.safe { gjelderFlereBarn }.ifNull(false),
                        )
                    )
                }


                showIf(barnetilleggFellesbarn.notNull() or harBarnetilleggSaerkullsbarn) {
                    paragraph {
                        ifNotNull(barnetilleggFellesbarn) { fellesTillegg ->
                            val sivilstand = fellesTillegg.brukerBorMed
                            text(
                                bokmal { + "Endringer i inntektene til deg og " + sivilstand.bestemtForm() + " din kan ha betydning for barnetillegget ditt. " },
                                nynorsk { + "Endringar i inntektene til deg og " + sivilstand.bestemtForm() + " di kan ha betydning for barnetillegget ditt. " },
                            )
                        }.orShowIf(harBarnetilleggSaerkullsbarn) {
                            text(
                                bokmal { + "Endringer i inntekten din kan ha betydning for barnetillegget ditt. " },
                                nynorsk { + "Endringar i inntekta di kan ha betydning for barnetillegget ditt. " },
                            )
                        }
                        text(
                            bokmal { + "Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" på ${Constants.NAV_URL}." },
                            nynorsk { + "Du kan enkelt melde frå om inntektsendringar under menyvalet " + quoted("uføretrygd") +" på ${Constants.NAV_URL}." },
                        )
                    }
                }

                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                    includePhrase(
                        Barnetillegg.InntektTilAvkortningFellesbarn(
                            harBeloepFratrukketAnnenForelder = barnetilleggFellesbarn.harFratrukketBeloepFraAnnenForelder,
                            faarUtbetaltBarnetilleggFellesBarn = barnetilleggFellesbarn.beloepNetto.greaterThan(0),
                            harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                            fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                            inntektAnnenForelderFellesbarn = barnetilleggFellesbarn.inntektAnnenForelder,
                            brukersInntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.brukersIntektBruktIAvkortning,
                            harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                            grunnbeloep = grunnbeloep,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            borMedSivilstand = barnetilleggFellesbarn.brukerBorMed,
                        )
                    )
                }

                ifNotNull(
                    barnetilleggFellesbarn,
                    barnetilleggSaerkullsbarn
                ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
                            beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                            beloepBruttoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepBrutto,
                            harFradragSaerkullsbarn = barnetilleggSaerkullsbarn.harFradrag,
                            fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                            harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                            harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                            harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                            beloepBruttoFellesbarn = barnetilleggFellesbarn.beloepBrutto,
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNetto,
                            fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                            harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                            harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                            samletInntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.samletInntektBruktIAvkortning,
                            borMed = barnetilleggFellesbarn.brukerBorMed,
                        )
                    )
                }.orIfNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.InntektTilAvkortningSaerkullsbarn(
                            beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                            fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                            inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                            harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                        )
                    )
                }

                includePhrase(
                    Barnetillegg.BarnetilleggIkkeUtbetalt(
                        fellesInnvilget = barnetilleggFellesbarn.notNull(),
                        fellesUtbetalt = barnetilleggFellesbarn.safe { beloepNetto.value }.ifNull(0).greaterThan(0),
                        harFlereFellesBarn = barnetilleggFellesbarn.safe { gjelderFlereBarn }.ifNull(false),
                        harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.safe { gjelderFlereBarn }.ifNull(false),
                        inntektstakFellesbarn = barnetilleggFellesbarn.safe { inntektstak }.ifNull(Kroner(0)),
                        inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.safe { inntektstak }.ifNull(Kroner(0)),
                        saerkullInnvilget = barnetilleggSaerkullsbarn.notNull(),
                        saerkullUtbetalt = barnetilleggSaerkullsbarn.safe { beloepNetto.value }.ifNull(0)
                            .greaterThan(0),
                    )
                )

                paragraph {
                    showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12" },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12" },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, og 22-12." },
                        )
                    }
                }
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorInorge))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
            ), opplysningerBruktIBeregningUT,
            harBarnetillegg
        )
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}



