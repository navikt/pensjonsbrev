package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.maler.fraser.common.Felles.kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import java.time.LocalDate


val vedleggBelopUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Nedenfor ser du den månedlige uføretrygden din.",
            Nynorsk to "Nedanfor ser du den månadlege uføretrygda di.",
            English to "Below is a presentation of your monthly disability benefit.",
        )
    }
}

data class TabellBeregnetUTHeleDto(
    val virkningsDatoFraOgMed: LocalDate,
    val virkningsDatoTilOgMed: LocalDate?,
)

@TemplateModelHelpers
val tabellBeregnetUTHele =
    OutlinePhrase<LangBokmalNynorskEnglish, MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned> { ufoeretrygd ->
        includePhrase(tabellUfoeretrygtTittel, ufoeretrygd.map {
            TabellBeregnetUTHeleDto(it.virkningFraOgMed, it.virkningTilOgMed)
        })

        paragraph {
            includePhrase(
                tabellUfoeretrygdTittel_broedtekst,
                ufoeretrygd.map { it.grunnbeloep }
            )

            showIf(ufoeretrygd.map { it.erAvkortet }) {
                includePhrase(tabellBeregnetUTAvkortet,
                    ufoeretrygd.map {
                        TabellBeregnetUTAvkortetDto(
                            it.barnetilleggNetto,
                            it.barnetilleggBrutto,
                            it.garantitilleggNordisk27Netto,
                            it.garantitilleggNordisk27Brutto,
                            it.ordinaerUTBeloepNetto,
                            it.ordinaerUTBeloepBrutto,
                            it.totalUTBeloepNetto,
                            it.totalUTBeloepBrutto,
                        )
                    }
                )
            }.orShow {
                includePhrase(tabellBeregnetUT, ufoeretrygd.map {
                    TabellBeregnetUTDto(
                        it.annetBelop,
                        it.barnetilleggNetto,
                        it.dekningFasteUtgifter,
                        it.garantitilleggNordisk27Netto,
                        it.ordinaerUTBeloepNetto,
                        it.totalUTBeloepNetto,
                    )
                })
            }
        }
    }

val tabellUfoeretrygtTittel = OutlinePhrase<LangBokmalNynorskEnglish, TabellBeregnetUTHeleDto> {
    title1 {
        val virkningsDatoFraOgMed = it.select(TabellBeregnetUTHeleDto::virkningsDatoFraOgMed).format(short = true)
        textExpr(
            Bokmal to "Den månedlige uføretrygden fra ".expr() + virkningsDatoFraOgMed,
            Nynorsk to "Den månadlege uføretrygda frå ".expr() + virkningsDatoFraOgMed,
            English to "Your monthly disability benefit from ".expr() + virkningsDatoFraOgMed,
        )

        ifNotNull(it.select(TabellBeregnetUTHeleDto::virkningsDatoTilOgMed)) {
            val virkningsDatoTilOgMed = it.format(short = true)
            textExpr(
                Bokmal to " til ".expr() + virkningsDatoTilOgMed,
                Nynorsk to " til ".expr() + virkningsDatoTilOgMed,
                English to " to ".expr() + virkningsDatoTilOgMed,
            )
        }
    }
}


val tabellUfoeretrygdTittel_broedtekst = ParagraphPhrase<LangBokmalNynorskEnglish, Kroner> { grunnbeloep ->
    textExpr(
        Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep.format() + " kroner.",
        Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep.format() + " kroner.",
        English to "The National Insurance basic amount (G) applied in the calculation is NOK ".expr() + grunnbeloep.format() + ".",
    )
}

data class TabellBeregnetUTDto(
    val annetBelop: Kroner,
    val barnetillegg: Kroner?,
    val dekningFasteUtgifter: Kroner?,
    val garantitilleggNordisk27: Kroner?,
    val ordinaerUTBeloep: Kroner,
    val totalUTBeloep: Kroner,
)

val tabellBeregnetUT = ParagraphPhrase<LangBokmalNynorskEnglish, TabellBeregnetUTDto> { beregnetUT ->
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
                includePhrase(kroner, beregnetUT.select(TabellBeregnetUTDto::ordinaerUTBeloep))
            }
        }

        ifNotNull(beregnetUT.map { it.barnetillegg }) {
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

        ifNotNull(beregnetUT.map { it.garantitilleggNordisk27 }) {
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

        showIf(beregnetUT.map { it.annetBelop.value > 0 }) {
            showIf(beregnetUT.map { beregnetUT ->
                beregnetUT.dekningFasteUtgifter?.let { it.value > 0 } ?: false
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "Fratrukket faste og nødvendige utgifter",
                            Nynorsk to "Fråtrekt faste og nødvendige utgifter",
                            English to "Deducted amount for fixed and necessary housing expenses",
                        )
                    }
                    cell {
                        includePhrase(kroner, beregnetUT.map { it.annetBelop })
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
                        includePhrase(kroner, beregnetUT.map { it.annetBelop })
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
                includePhrase(kroner, beregnetUT.map { it.totalUTBeloep })
            }
        }
    }
}

data class TabellBeregnetUTAvkortetDto(
    val barnetilleggNetto: Kroner?,
    val barnetilleggBrutto: Kroner?,
    val garantitilleggNordisk27Netto: Kroner?,
    val garantitilleggNordisk27Brutto: Kroner?,
    val ordinaerUTBeloepNetto: Kroner,
    val ordinaerUTBeloepBrutto: Kroner,
    val totalUTBeloepNetto: Kroner,
    val totalUTBeloepBrutto: Kroner,
)

val tabellBeregnetUTAvkortet = ParagraphPhrase<LangBokmalNynorskEnglish, TabellBeregnetUTAvkortetDto> { beregnetUT ->
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
                includePhrase(kroner, beregnetUT.select(TabellBeregnetUTAvkortetDto::ordinaerUTBeloepBrutto))
            }
            cell {
                includePhrase(kroner, beregnetUT.select(TabellBeregnetUTAvkortetDto::ordinaerUTBeloepNetto))
            }
        }

        ifNotNull(
            beregnetUT.select(TabellBeregnetUTAvkortetDto::barnetilleggBrutto),
            beregnetUT.select(TabellBeregnetUTAvkortetDto::barnetilleggNetto),
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
            beregnetUT.select(TabellBeregnetUTAvkortetDto::garantitilleggNordisk27Brutto),
            beregnetUT.select(TabellBeregnetUTAvkortetDto::garantitilleggNordisk27Netto),
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
                includePhrase(kroner, beregnetUT.select(TabellBeregnetUTAvkortetDto::totalUTBeloepBrutto))
            }

            cell {
                includePhrase(kroner, beregnetUT.select(TabellBeregnetUTAvkortetDto::totalUTBeloepNetto))
            }
        }
    }
}