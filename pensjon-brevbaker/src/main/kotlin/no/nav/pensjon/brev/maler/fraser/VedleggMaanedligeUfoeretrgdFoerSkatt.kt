package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.Beloep
import no.nav.pensjon.brev.maler.fraser.common.Felles.kroner
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
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

data class TabellUTTittelGjeldende_001Dto(
    val virkningsDatoFraOgMed: LocalDate,
    val virkningsDatoTilOgMed: LocalDate?,
)

val tabellBeregnetUTHele =
    OutlinePhrase<LangBokmalNynorskEnglish, MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned> { ufoeretrygd ->
        //tekst101 tittel gjeldende (obligatorisk)
        includePhrase(tabellUfoeretrygtTittel, ufoeretrygd.map {
            TabellUTTittelGjeldende_001Dto(it.virkningFraOgMed, it.virkningTilOgMed)
        })

        paragraph {

            includePhrase(tabellUfoeretrygdTittel_broedtekst, ufoeretrygd.map {
                TabellUTTittelGjeldende_001_broedtekst_Dto(it.grunnbeloep)
            })

            showIf(
                ufoeretrygd.map { !it.erAvkortet }
            ) {
                includePhrase(tabellBeregnetUT, ufoeretrygd.map {
                    TabellBeregnetUTDto(
                        it.annetBelop,
                        it.barnetillegg?.netto,
                        it.dekningFasteUtgifter,
                        it.garantitilleggNordisk27?.netto,
                        it.ordinaerUTBeloep.netto,
                        it.totalUTBeloep.netto,
                    )
                })
            }

            showIf(ufoeretrygd.map { it.erAvkortet }) {
                includePhrase(tabellBeregnetUTAvkortet,
                    ufoeretrygd.map {
                        TabellBeregnetUTAvkortetDto(
                            it.barnetillegg,
                            it.garantitilleggNordisk27,
                            it.ordinaerUTBeloep,
                            it.totalUTBeloep,
                        )
                    }
                )
            }
        }
    }

val tabellUfoeretrygtTittel = OutlinePhrase<LangBokmalNynorskEnglish, TabellUTTittelGjeldende_001Dto> {
    title1 {
        val virkningsDatoFraOgMed =
            it.select(TabellUTTittelGjeldende_001Dto::virkningsDatoFraOgMed).format(short = true)
        textExpr(
            Bokmal to "Den månedlige uføretrygden fra ".expr() + virkningsDatoFraOgMed,
            Nynorsk to "Den månadlege uføretrygda frå ".expr() + virkningsDatoFraOgMed,
            English to "Your monthly disability benefit from ".expr() + virkningsDatoFraOgMed,
        )

        ifNotNull(it.select(TabellUTTittelGjeldende_001Dto::virkningsDatoTilOgMed)) {
            val virkningsDatoTilOgMed = it.format(short = true)
            textExpr(
                Bokmal to " til ".expr() + virkningsDatoTilOgMed,
                Nynorsk to " til ".expr() + virkningsDatoTilOgMed,
                English to " to ".expr() + virkningsDatoTilOgMed,
            )
        }
    }
}

data class TabellUTTittelGjeldende_001_broedtekst_Dto(
    val grunnbeloep: Int,
)

val tabellUfoeretrygdTittel_broedtekst =
    ParagraphPhrase<LangBokmalNynorskEnglish, TabellUTTittelGjeldende_001_broedtekst_Dto> {
        val grunnbeloep = it.select(TabellUTTittelGjeldende_001_broedtekst_Dto::grunnbeloep).format()

        textExpr(
            Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner.",
            Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner.",
            English to "The National Insurance basic amount (G) applied in the calculation is NOK ".expr() + grunnbeloep + ".",
        )
    }

data class TabellBeregnetUTDto(
    val annetBelop: Int,
    val barnetillegg: Int?,
    val dekningFasteUtgifter: Int,
    val garantitilleggNordisk27: Int?,
    val ordinaerUTBeloep: Int,
    val totalUTBeloep: Int,
)

val tabellBeregnetUT = ParagraphPhrase<LangBokmalNynorskEnglish, TabellBeregnetUTDto> { beregnetUT ->
    table(
        header = {
            column(1) {} //TODO skal det stå noe her?
            column(1, alignment = Element.Table.ColumnAlignment.RIGHT) {
                //tekst102 BruttoPerManed obligatorisk
                text(
                    Bokmal to "Bruttobeløp per måned",
                    Nynorsk to "Bruttobeløp per månad",
                    English to "Gross monthly amount",
                    Element.Text.FontType.BOLD
                )
            }
        }
    ) {
        //tekst103 Uføretrygd obligatorisk
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

        //if barnetillegg != "" tekst104
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

        //if garatitilleggNordisk27 !="" tekst105
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

        //if dekningFasteUtgifter =0 and annetBeloep >0 tekst106
        showIf(beregnetUT.map {
            it.dekningFasteUtgifter == 0 && it.annetBelop > 0
        }) {
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

        //if dekningFasteUtgifter >0 and annetBeloep >0 tekst 107
        showIf(beregnetUT.map {
            it.dekningFasteUtgifter > 0 && it.annetBelop > 0
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
        }

        //tekst108 sum skatt (obligatorisk)
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
    val barnetillegg: Beloep?,
    val garantitilleggNordisk27: Beloep?,
    val ordinaerUTBeloep: Beloep,
    val totalUTBeloep: Beloep,
)

val tabellBeregnetUTAvkortet = ParagraphPhrase<LangBokmalNynorskEnglish, TabellBeregnetUTAvkortetDto> { beregnetUT ->
    table(
        //tekst109 ufoeretrygd foer og etter fradrag (obligatorisk)
        header = {
            column {} //TODO skal det stå noe her?
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
        //tektst110 ufoeretrygd foer og etter fradrag (obligatorisk)
        row {
            cell {
                text(
                    Bokmal to "Uføretrygd",
                    Nynorsk to "Uføretrygd",
                    English to "Disability benefit",
                )
            }
            cell {
                includePhrase(kroner, beregnetUT.map { it.ordinaerUTBeloep.brutto })
            }
            cell {
                includePhrase(kroner, beregnetUT.map { it.ordinaerUTBeloep.netto })
            }
        }

        //if barnetillegg != "" tekst111
        ifNotNull(beregnetUT.map { it.barnetillegg }) { barnetillegg ->
            row {
                cell {
                    text(
                        Bokmal to "Barnetillegg for særkullsbarn",
                        Nynorsk to "Barnetillegg for særkullsbarn",
                        English to "Child supplement for child(ren) by a previous marriage/relationship",
                    )
                }

                cell {
                    includePhrase(kroner, barnetillegg.map { it.brutto })
                }

                cell {
                    includePhrase(kroner, barnetillegg.map { it.netto })
                }
            }
        }

        //if garatitilleggNordisk27 !="" tekst112

        ifNotNull(beregnetUT.map { it.garantitilleggNordisk27 }) { garantitillegg ->
            row {
                cell {
                    text(
                        Bokmal to "Nordisk garantitillegg",
                        Nynorsk to "Nordisk garantitillegg",
                        English to "Nordic guarantee supplement",
                    )
                }

                cell {
                    includePhrase(kroner, garantitillegg.map { it.brutto })
                }

                cell {
                    includePhrase(kroner, garantitillegg.map { it.netto })
                }
            }
        }

        //tekst113 sum skatt (obligatorisk)
        row {
            cell {
                text(
                    Bokmal to "Sum før skatt",
                    Nynorsk to "Sum før skatt",
                    English to "Total before tax",
                )
            }

            cell {
                includePhrase(kroner, beregnetUT.map { it.totalUTBeloep.brutto })
            }

            cell {
                includePhrase(kroner, beregnetUT.map { it.totalUTBeloep.netto })
            }
        }
    }
}