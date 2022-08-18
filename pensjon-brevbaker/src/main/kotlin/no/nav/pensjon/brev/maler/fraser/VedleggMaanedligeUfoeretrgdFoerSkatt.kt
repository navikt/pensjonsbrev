package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.annetBelop
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.barnetilleggBrutto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.barnetilleggNetto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.dekningFasteUtgifter
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.erAvkortet
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.garantitilleggNordisk27Brutto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.garantitilleggNordisk27Netto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.ordinaerUTBeloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.ordinaerUTBeloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.totalUTBeloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.totalUTBeloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.virkningFraOgMed
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.virkningTilOgMed
import no.nav.pensjon.brev.maler.fraser.common.Felles.kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate


object VedleggBelopUT_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() =
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

    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        includePhrase(TabellUfoeretrygtTittel(ufoeretrygd.virkningFraOgMed, ufoeretrygd.virkningTilOgMed))

        paragraph {
            includePhrase(TabellUfoeretrygdTittel_broedtekst(ufoeretrygd.grunnbeloep))

            showIf(ufoeretrygd.erAvkortet) {
                includePhrase(
                    TabellBeregnetUTAvkortet(
                        ufoeretrygd.barnetilleggNetto,
                        ufoeretrygd.barnetilleggBrutto,
                        ufoeretrygd.garantitilleggNordisk27Netto,
                        ufoeretrygd.garantitilleggNordisk27Brutto,
                        ufoeretrygd.ordinaerUTBeloepNetto,
                        ufoeretrygd.ordinaerUTBeloepBrutto,
                        ufoeretrygd.totalUTBeloepNetto,
                        ufoeretrygd.totalUTBeloepBrutto,
                    )
                )
            }.orShow {
                includePhrase(
                    TabellBeregnetUT(
                        ufoeretrygd.annetBelop,
                        ufoeretrygd.barnetilleggNetto,
                        ufoeretrygd.dekningFasteUtgifter,
                        ufoeretrygd.garantitilleggNordisk27Netto,
                        ufoeretrygd.ordinaerUTBeloepNetto,
                        ufoeretrygd.totalUTBeloepNetto,
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

    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() =
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
    override fun ParagraphScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep.format() + " kroner.",
            Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep.format() + " kroner.",
            English to "The National Insurance basic amount (G) applied in the calculation is NOK ".expr() + grunnbeloep.format() + ".",
        )
}

data class TabellBeregnetUT(
    val annetBelop: Expression<Kroner>,
    val barnetillegg: Expression<Kroner?>,
    val dekningFasteUtgifter: Expression<Kroner?>,
    val garantitilleggNordisk27: Expression<Kroner?>,
    val ordinaerUTBeloep: Expression<Kroner>,
    val totalUTBeloep: Expression<Kroner>,
) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphScope<LangBokmalNynorskEnglish, Unit>.template() {
        table(
            header = {
                column(1) {}
                column(1, alignment = Element.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Bruttobeløp per måned",
                        Nynorsk to "Bruttobeløp per månad",
                        English to "Gross monthly amount",
                        Element.Text.FontType.BOLD
                    )
                }
            }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Uføretrygd",
                        Nynorsk to "Uføretrygd",
                        English to "Disability benefit",
                    )
                }
                cell {
                    includePhrase(kroner, ordinaerUTBeloep)
                }
            }

            ifNotNull(barnetillegg) {
                row {
                    cell {
                        text(
                            Bokmal to "Barnetillegg for særkullsbarn",
                            Nynorsk to "Barnetillegg for særkullsbarn",
                            English to "Child supplement for child(ren) by a previous marriage/relationship",
                        )
                    }
                    cell {
                        includePhrase(kroner, it)
                    }
                }
            }

            ifNotNull(garantitilleggNordisk27) {
                row {
                    cell {
                        text(
                            Bokmal to "Nordisk garantitillegg",
                            Nynorsk to "Nordisk garantitillegg",
                            English to "Nordic guarantee supplement",
                        )
                    }
                    cell {
                        includePhrase(kroner, it)
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
                            includePhrase(kroner, annetBelop)
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
                            includePhrase(kroner, annetBelop)
                        }
                    }
                }
            }

            row {
                cell {
                    text(
                        Bokmal to "Sum før skatt",
                        Nynorsk to "Sum før skatt",
                        English to "Total before tax",
                    )
                }
                cell {
                    includePhrase(kroner, totalUTBeloep)
                }
            }
        }
    }
}

data class TabellBeregnetUTAvkortet(
    val barnetilleggNetto: Expression<Kroner?>,
    val barnetilleggBrutto: Expression<Kroner?>,
    val garantitilleggNordisk27Netto: Expression<Kroner?>,
    val garantitilleggNordisk27Brutto: Expression<Kroner?>,
    val ordinaerUTBeloepNetto: Expression<Kroner>,
    val ordinaerUTBeloepBrutto: Expression<Kroner>,
    val totalUTBeloepNetto: Expression<Kroner>,
    val totalUTBeloepBrutto: Expression<Kroner>,
) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphScope<LangBokmalNynorskEnglish, Unit>.template() {
        table(
            header = {
                column {}
                column(alignment = Element.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Uføretrygd per måned før fradrag for inntekt",
                        Nynorsk to "Uføretrygd per månad før frådrag for inntekt",
                        English to "Monthly disability benefit before deductions for income",
                        Element.Text.FontType.BOLD
                    )
                }
                column(alignment = Element.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Uføretrygd per måned etter fradrag for inntekt",
                        Nynorsk to "Uføretrygd per månad etter frådrag for inntekt",
                        English to "Monthly disability benefit after deductions for income",
                        Element.Text.FontType.BOLD
                    )
                }
            }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Uføretrygd",
                        Nynorsk to "Uføretrygd",
                        English to "Disability benefit",
                    )
                }
                cell {
                    includePhrase(kroner, ordinaerUTBeloepBrutto)
                }
                cell {
                    includePhrase(kroner, ordinaerUTBeloepNetto)
                }
            }

            ifNotNull(
                barnetilleggBrutto,
                barnetilleggNetto,
            ) { brutto, netto ->
                row {
                    cell {
                        text(
                            Bokmal to "Barnetillegg for særkullsbarn",
                            Nynorsk to "Barnetillegg for særkullsbarn",
                            English to "Child supplement for child(ren) by a previous marriage/relationship",
                        )
                    }

                    cell {
                        includePhrase(kroner, brutto)
                    }

                    cell {
                        includePhrase(kroner, netto)
                    }
                }
            }

            ifNotNull(
                garantitilleggNordisk27Brutto,
                garantitilleggNordisk27Netto,
            ) { brutto, netto ->
                row {
                    cell {
                        text(
                            Bokmal to "Nordisk garantitillegg",
                            Nynorsk to "Nordisk garantitillegg",
                            English to "Nordic guarantee supplement",
                        )
                    }

                    cell {
                        includePhrase(kroner, brutto)
                    }

                    cell {
                        includePhrase(kroner, netto)
                    }
                }
            }

            row {
                cell {
                    text(
                        Bokmal to "Sum før skatt",
                        Nynorsk to "Sum før skatt",
                        English to "Total before tax",
                    )
                }

                cell {
                    includePhrase(kroner, totalUTBeloepBrutto)
                }

                cell {
                    includePhrase(kroner, totalUTBeloepNetto)
                }
            }
        }
    }
}