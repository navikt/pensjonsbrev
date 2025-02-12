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
    // VedleggBelopUT_001
    object VedleggBeloepUfoeretrygd : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Nedenfor ser du den månedlige uføretrygden din.",
                    Nynorsk to "Nedanfor ser du den månadlege uføretrygda di.",
                    English to "Below is a presentation of your monthly disability benefit.",
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
                        ),
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
                        ),
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
                textExpr(
                    Bokmal to "Den månedlige uføretrygden fra ".expr() + virkningsDatoFraOgMed,
                    Nynorsk to "Den månadlege uføretrygda frå ".expr() + virkningsDatoFraOgMed,
                    English to "Your monthly disability benefit from ".expr() + virkningsDatoFraOgMed,
                )

                ifNotNull(virkningsDatoTilOgMed) {
                    val virkningsDatoTilOgMed = it.format(short = true)
                    textExpr(
                        Bokmal to " til ".expr() + virkningsDatoTilOgMed,
                        Nynorsk to " til ".expr() + virkningsDatoTilOgMed,
                        English to " to ".expr() + virkningsDatoTilOgMed,
                    )
                }
            }
    }

    data class TabellUfoeretrygdTittel_broedtekst(
        val grunnbeloep: Expression<Kroner>,
    ) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            textExpr(
                Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep.format() + " kroner.",
                Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep.format() + " kroner.",
                English to "The National Insurance basic amount (G) applied in the calculation is NOK ".expr() + grunnbeloep.format() + ".",
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
                            Bokmal to "Bruttobeløp per måned",
                            Nynorsk to "Bruttobeløp per månad",
                            English to "Gross monthly amount",
                        )
                    }
                },
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
                                    Bokmal to "Fratrukket faste og nødvendige utgifter",
                                    Nynorsk to "Fråtrekt faste og nødvendige utgifter",
                                    English to "Deducted amount for fixed and necessary housing expenses",
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
                                    Bokmal to "Fratrukket beløp",
                                    Nynorsk to "Fråtrekt beløp",
                                    English to "Deducted amount",
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
                            Bokmal to "Før fradrag for inntekt",
                            Nynorsk to "Før frådrag for inntekt",
                            English to "Before deductions for income",
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Etter fradrag for inntekt",
                            Nynorsk to "Etter frådrag for inntekt",
                            English to "After deductions for income",
                        )
                    }
                },
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
                Bokmal to "Uføretrygd",
                Nynorsk to "Uføretrygd",
                English to "Disability benefit",
            )
        }
    }

    class TabellTekstEktefelletillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Ektefelletillegg",
                Nynorsk to "Ektefelletillegg",
                English to "Spouse supplement",
            )
        }
    }

    class TabellTekstBarnetilleggFellesbarn : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Barnetillegg for fellesbarn",
                Nynorsk to "Barnetillegg for fellesbarn",
                English to "Child supplement for joint child(ren)",
            )
        }
    }

    class TabellTekstBarnetilleggSaerkullsbarn : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Barnetillegg for særkullsbarn",
                Nynorsk to "Barnetillegg for særkullsbarn",
                English to "Child supplement for child(ren) by a previous marriage/relationship",
            )
        }
    }

    class TabellTekstGjenlevendetillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Gjenlevendetillegg",
                Nynorsk to "Attlevandetillegg",
                English to "Survivor's supplement",
            )
        }
    }

    class TabellTekstNordiskGarantitillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Nordisk garantitillegg",
                Nynorsk to "Nordisk garantitillegg",
                English to "Nordic guarantee supplement",
            )
        }
    }

    class TabellTekstSumFoerSkatt : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Sum før skatt",
                Nynorsk to "Sum før skatt",
                English to "Total before tax",
                FontType.BOLD,
            )
        }
    }
}
