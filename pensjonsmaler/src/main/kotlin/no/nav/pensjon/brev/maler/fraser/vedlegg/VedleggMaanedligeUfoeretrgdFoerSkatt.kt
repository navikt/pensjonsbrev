package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.annetBelop
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.barnetilleggFellesbarnBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.barnetilleggFellesbarnNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.barnetilleggSaerkullsbarnBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.barnetilleggSaerkullsbarnNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.dekningFasteUtgifter
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.ektefelletilleggBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.ektefelletilleggNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.erAvkortet
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.garantitilleggNordisk27Brutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.garantitilleggNordisk27Netto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.gjenlevendetilleggBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.gjenlevendetilleggNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.ordinaerUTBeloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.ordinaerUTBeloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.totalUTBeloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.totalUTBeloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.virkningFraOgMed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.UfoeretrygdPerMaanedSelectors.virkningTilOgMed
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object VedleggMaanedligeUfoeretrgdFoerSkatt {
    //VedleggBelopUT_001
    object VedleggBeloepUfoeretrygd : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    bokmal { + "Nedenfor ser du den månedlige uføretrygden din." },
                    nynorsk { + "Nedanfor ser du den månadlege uføretrygda di." },
                    english { + "Below is a presentation of your monthly disability benefit." },
                )
            }
    }

    data class TabellBeregnetUTHele(
        val ufoeretrygd: Expression<MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            includePhrase(TabellUfoeretrygtTittel(ufoeretrygd.virkningFraOgMed, ufoeretrygd.virkningTilOgMed))

            paragraph {
                includePhrase(TabellUfoeretrygdTittel_broedtekst(ufoeretrygd.grunnbeloep))

                showIf(ufoeretrygd.erAvkortet) {
                    includePhrase(
                        TabellBeregnetUTAvkortet(
                            barnetilleggFellesbarnBrutto = ufoeretrygd.barnetilleggFellesbarnBrutto,
                            barnetilleggFellesbarnNetto = ufoeretrygd.barnetilleggFellesbarnNetto,
                            barnetilleggSaerkullsbarnBrutto = ufoeretrygd.barnetilleggSaerkullsbarnBrutto,
                            barnetilleggSaerkullsbarnNetto = ufoeretrygd.barnetilleggSaerkullsbarnNetto,
                            ektefelletilleggBrutto = ufoeretrygd.ektefelletilleggBrutto,
                            ektefelletilleggNetto = ufoeretrygd.ektefelletilleggNetto,
                            garantitilleggNordisk27Brutto = ufoeretrygd.garantitilleggNordisk27Brutto,
                            garantitilleggNordisk27Netto = ufoeretrygd.garantitilleggNordisk27Netto,
                            gjenlevendetilleggBrutto = ufoeretrygd.gjenlevendetilleggBrutto,
                            gjenlevendetilleggNetto = ufoeretrygd.gjenlevendetilleggNetto,
                            ordinaerUTBeloepBrutto = ufoeretrygd.ordinaerUTBeloepBrutto,
                            ordinaerUTBeloepNetto = ufoeretrygd.ordinaerUTBeloepNetto,
                            totalUTBeloepBrutto = ufoeretrygd.totalUTBeloepBrutto,
                            totalUTBeloepNetto = ufoeretrygd.totalUTBeloepNetto,
                        )
                    )
                }.orShow {
                    includePhrase(
                        TabellBeregnetUT(
                            annetBelop = ufoeretrygd.annetBelop,
                            barnetilleggFellesbarn = ufoeretrygd.barnetilleggFellesbarnNetto,
                            barnetilleggSaerkullsbarn = ufoeretrygd.barnetilleggSaerkullsbarnNetto,
                            dekningFasteUtgifter = ufoeretrygd.dekningFasteUtgifter,
                            ektefelletillegg = ufoeretrygd.ektefelletilleggNetto,
                            garantitilleggNordisk27 = ufoeretrygd.garantitilleggNordisk27Netto,
                            gjenlevendetillegg = ufoeretrygd.gjenlevendetilleggNetto,
                            ordinaerUTBeloep = ufoeretrygd.ordinaerUTBeloepNetto,
                            totalUTBeloep = ufoeretrygd.totalUTBeloepNetto,
                        )
                    )
                }
            }
        }
    }

    data class TabellUfoeretrygtTittel(
        val virkningsDatoFraOgMed: Expression<LocalDate>,
        val virkningsDatoTilOgMed: Expression<LocalDate?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                val virkningsDatoFraOgMed = virkningsDatoFraOgMed.format(short = true)
                text(
                    bokmal { + "Den månedlige uføretrygden fra " + virkningsDatoFraOgMed },
                    nynorsk { + "Den månadlege uføretrygda frå " + virkningsDatoFraOgMed },
                    english { + "Your monthly disability benefit from " + virkningsDatoFraOgMed },
                )

                ifNotNull(virkningsDatoTilOgMed) {
                    val virkningsDatoTilOgMed = it.format(short = true)
                    text(
                        bokmal { + " til " + virkningsDatoTilOgMed },
                        nynorsk { + " til " + virkningsDatoTilOgMed },
                        english { + " to " + virkningsDatoTilOgMed },
                    )
                }
            }
    }


    data class TabellUfoeretrygdTittel_broedtekst(
        val grunnbeloep: Expression<Kroner>,
    ) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            text(
                bokmal { + "Folketrygdens grunnbeløp (G) benyttet i beregningen er " + grunnbeloep.format() + "." },
                nynorsk { + "Grunnbeløpet i folketrygda (G) nytta i utrekninga er " + grunnbeloep.format() + "." },
                english { + "The National Insurance basic amount (G) applied in the calculation is " + grunnbeloep.format() + "." },
            )
    }

    data class TabellBeregnetUT(
        val annetBelop: Expression<Kroner>,
        val barnetilleggFellesbarn: Expression<Kroner?>,
        val barnetilleggSaerkullsbarn: Expression<Kroner?>,
        val dekningFasteUtgifter: Expression<Kroner?>,
        val ektefelletillegg: Expression<Kroner?>,
        val garantitilleggNordisk27: Expression<Kroner?>,
        val gjenlevendetillegg: Expression<Kroner?>,
        val ordinaerUTBeloep: Expression<Kroner>,
        val totalUTBeloep: Expression<Kroner>,
    ) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            table(
                header = {
                    column(1) {}
                    column(1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Bruttobeløp per måned" },
                            nynorsk { + "Bruttobeløp per månad" },
                            english { + "Gross monthly amount" },
                        )
                    }
                }
            ) {
                row {
                    cell {
                        includePhrase(TabellTekstUfoeretrygd())
                    }
                    cell {
                        includePhrase(KronerText(ordinaerUTBeloep))
                    }
                }

                ifNotNull(ektefelletillegg) {
                    row {
                        cell {
                            includePhrase(TabellTekstEktefelletillegg())
                        }
                        cell {
                            includePhrase(KronerText(it))
                        }
                    }
                }

                ifNotNull(barnetilleggSaerkullsbarn) {
                    row {
                        cell {
                            includePhrase(TabellTekstBarnetilleggSaerkullsbarn())
                        }
                        cell {
                            includePhrase(KronerText(it))
                        }
                    }
                }

                ifNotNull(barnetilleggFellesbarn) {
                    row {
                        cell {
                            includePhrase(TabellTekstBarnetilleggFellesbarn())
                        }
                        cell {
                            includePhrase(KronerText(it))
                        }
                    }
                }

                ifNotNull(gjenlevendetillegg) {
                    row {
                        cell {
                            includePhrase(TabellTekstGjenlevendetillegg())
                        }
                        cell {
                            includePhrase(KronerText(it))
                        }
                    }
                }

                ifNotNull(garantitilleggNordisk27) {
                    row {
                        cell {
                            includePhrase(TabellTekstNordiskGarantitillegg())
                        }
                        cell {
                            includePhrase(KronerText(it))
                        }
                    }
                }

                showIf(annetBelop.greaterThan(0)) {
                    showIf(dekningFasteUtgifter.ifNull(Kroner(0)).greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Fratrukket faste og nødvendige utgifter" },
                                    nynorsk { + "Fråtrekt faste og nødvendige utgifter" },
                                    english { + "Deducted amount for fixed and necessary housing expenses" },
                                )
                            }
                            cell {
                                includePhrase(KronerText(annetBelop))
                            }
                        }
                    }.orShow {
                        row {
                            cell {
                                text(
                                    bokmal { + "Fratrukket beløp" },
                                    nynorsk { + "Fråtrekt beløp" },
                                    english { + "Deducted amount" },
                                )
                            }
                            cell {
                                includePhrase(KronerText(annetBelop))
                            }
                        }
                    }
                }

                row {
                    cell {
                        includePhrase(TabellTekstSumFoerSkatt())
                    }
                    cell {
                        includePhrase(KronerText(totalUTBeloep, FontType.BOLD))
                    }
                }
            }
        }
    }

    data class TabellBeregnetUTAvkortet(
        val barnetilleggFellesbarnBrutto: Expression<Kroner?>,
        val barnetilleggFellesbarnNetto: Expression<Kroner?>,
        val barnetilleggSaerkullsbarnBrutto: Expression<Kroner?>,
        val barnetilleggSaerkullsbarnNetto: Expression<Kroner?>,
        val ektefelletilleggBrutto: Expression<Kroner?>,
        val ektefelletilleggNetto: Expression<Kroner?>,
        val garantitilleggNordisk27Brutto: Expression<Kroner?>,
        val garantitilleggNordisk27Netto: Expression<Kroner?>,
        val gjenlevendetilleggBrutto: Expression<Kroner?>,
        val gjenlevendetilleggNetto: Expression<Kroner?>,
        val ordinaerUTBeloepBrutto: Expression<Kroner>,
        val ordinaerUTBeloepNetto: Expression<Kroner>,
        val totalUTBeloepBrutto: Expression<Kroner>,
        val totalUTBeloepNetto: Expression<Kroner>,
    ) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            table(
                header = {
                    column {}
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Før fradrag for inntekt" },
                            nynorsk { + "Før frådrag for inntekt" },
                            english { + "Before deductions for income" },
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Etter fradrag for inntekt" },
                            nynorsk { + "Etter frådrag for inntekt" },
                            english { + "After deductions for income" },
                        )
                    }
                }
            ) {
                row {
                    cell {
                        includePhrase(TabellTekstUfoeretrygd())
                    }
                    cell {
                        includePhrase(KronerText(ordinaerUTBeloepBrutto))
                    }
                    cell {
                        includePhrase(KronerText(ordinaerUTBeloepNetto))
                    }
                }

                ifNotNull(ektefelletilleggBrutto, ektefelletilleggNetto) { brutto, netto ->
                    row {
                        cell {
                            includePhrase(TabellTekstEktefelletillegg())
                        }

                        cell {
                            includePhrase(KronerText(brutto))
                        }

                        cell {
                            includePhrase(KronerText(netto))
                        }
                    }
                }

                ifNotNull(barnetilleggSaerkullsbarnBrutto, barnetilleggSaerkullsbarnNetto) { brutto, netto ->
                    row {
                        cell {
                            includePhrase(TabellTekstBarnetilleggSaerkullsbarn())
                        }

                        cell {
                            includePhrase(KronerText(brutto))
                        }

                        cell {
                            includePhrase(KronerText(netto))
                        }
                    }
                }

                ifNotNull(barnetilleggFellesbarnBrutto, barnetilleggFellesbarnNetto) { brutto, netto ->
                    row {
                        cell {
                            includePhrase(TabellTekstBarnetilleggFellesbarn())
                        }

                        cell {
                            includePhrase(KronerText(brutto))
                        }

                        cell {
                            includePhrase(KronerText(netto))
                        }
                    }
                }

                ifNotNull(gjenlevendetilleggBrutto, gjenlevendetilleggNetto) { brutto, netto ->
                    row {
                        cell {
                            includePhrase(TabellTekstGjenlevendetillegg())
                        }

                        cell {
                            includePhrase(KronerText(brutto))
                        }

                        cell {
                            includePhrase(KronerText(netto))
                        }
                    }
                }

                ifNotNull(garantitilleggNordisk27Brutto, garantitilleggNordisk27Netto) { brutto, netto ->
                    row {
                        cell {
                            includePhrase(TabellTekstNordiskGarantitillegg())
                        }

                        cell {
                            includePhrase(KronerText(brutto))
                        }

                        cell {
                            includePhrase(KronerText(netto))
                        }
                    }
                }

                row {
                    cell {
                        includePhrase(TabellTekstSumFoerSkatt())
                    }

                    cell {
                        includePhrase(KronerText(totalUTBeloepBrutto, FontType.BOLD))
                    }

                    cell {
                        includePhrase(KronerText(totalUTBeloepNetto, FontType.BOLD))
                    }
                }
            }
        }
    }

    class TabellTekstUfoeretrygd : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Uføretrygd" },
                nynorsk { + "Uføretrygd" },
                english { + "Disability benefit" },
            )
        }
    }

    class TabellTekstEktefelletillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Ektefelletillegg" },
                nynorsk { + "Ektefelletillegg" },
                english { + "Spouse supplement" },
            )
        }
    }

    class TabellTekstBarnetilleggFellesbarn : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Barnetillegg for fellesbarn" },
                nynorsk { + "Barnetillegg for fellesbarn" },
                english { + "Child supplement for joint child(ren)" },
            )
        }
    }

    class TabellTekstBarnetilleggSaerkullsbarn : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Barnetillegg for særkullsbarn" },
                nynorsk { + "Barnetillegg for særkullsbarn" },
                english { + "Child supplement for child(ren) by a previous marriage/relationship" },
            )
        }
    }

    class TabellTekstGjenlevendetillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Gjenlevendetillegg" },
                nynorsk { + "Attlevandetillegg" },
                english { + "Survivor's supplement" },
            )
        }
    }

    class TabellTekstNordiskGarantitillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Nordisk garantitillegg" },
                nynorsk { + "Nordisk garantitillegg" },
                english { + "Nordic guarantee supplement" },
            )
        }
    }

    class TabellTekstSumFoerSkatt : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Sum før skatt" },
                nynorsk { + "Sum før skatt" },
                english { + "Total before tax" },
                FontType.BOLD
            )
        }
    }
}