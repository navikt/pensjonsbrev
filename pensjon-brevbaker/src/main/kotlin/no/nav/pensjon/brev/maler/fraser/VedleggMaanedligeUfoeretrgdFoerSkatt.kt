package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
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

val tabellBeregnetUTHele = OutlinePhrase<LangBokmalNynorskEnglish, UfoeretrygdPerMaaned> { ufoeretrygd ->
    //tekst101 tittel gjeldende (obligatorisk)
    includePhrase(tabellUfoeretrygtTittel, ufoeretrygd.map {
        TabellUTTittelGjeldende_001Dto(it.virkningFraOgMed, it.virkningTilOgMed)
    })

    paragraph {

        includePhrase(tabellUfoeretrygdTittel_broedtekst, ufoeretrygd.map {
            TabellUTTittelGjeldende_001_broedtekst_Dto(it.grunnbeloep)
        })

        showIf(
            ufoeretrygd.map { it.avkortning == null }
        ) {
            includePhrase(tabellBeregnetUT, ufoeretrygd.map {
                TabellBeregnetUTDto(
                    it.annetBelop,
                    it.barnetillegg,
                    it.dekningFasteUtgifter,
                    it.garantitilleggNordisk27,
                    it.ordinaerUTBeloep,
                    it.totalUTBeloep,
                )
            })
        }

        ifNotNull(ufoeretrygd.map {
            it.avkortning?.let { avkortning ->
                TabellBeregnetUTAvkortetDto(
                    it.barnetillegg,
                    avkortning.barnetilleggFoerAvkort,
                    it.garantitilleggNordisk27,
                    avkortning.garantitilleggNordisk27FoerAvkort,
                    it.ordinaerUTBeloep,
                    avkortning.ordinaerUTBeloepFoerAvkort,
                    it.totalUTBeloep,
                    avkortning.totalUTBeloepFoerAvkort,
                )
            }
        }) {
            includePhrase(tabellBeregnetUTAvkortet, it)
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
            column(1) {
                //TODO: kan ikke lenger ha tom kollonne?
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }
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
                val ordinaerUfoeretrygd = beregnetUT.select(TabellBeregnetUTDto::ordinaerUTBeloep).format()
                textExpr(
                    Bokmal to ordinaerUfoeretrygd + " kr",
                    Nynorsk to ordinaerUfoeretrygd + " kr",
                    English to ordinaerUfoeretrygd + " NOK",
                )
            }
        }

        //if barnetillegg != "" tekst104
        ifNotNull(beregnetUT.map { it.barnetillegg }) {
            row {
                val barnetilegg = it.format()
                cell {
                    text(
                        Bokmal to "Barnetillegg for særkullsbarn",
                        Nynorsk to "Barnetillegg for særkullsbarn",
                        English to "Child supplement for child(ren) by a previous marriage/relationship",
                    )
                }
                cell {
                    textExpr(
                        Bokmal to barnetilegg + " kr",
                        Nynorsk to barnetilegg + " kr",
                        English to barnetilegg + " NOK",
                    )
                }
            }
        }

        //if garatitilleggNordisk27 !="" tekst105
        ifNotNull(beregnetUT.map { it.garantitilleggNordisk27 }) {
            val garantitilegg = it.format()
            row {
                cell {
                    text(
                        Bokmal to "Nordisk garantitillegg",
                        Nynorsk to "Nordisk garantitillegg",
                        English to "Nordic guarantee supplement",
                    )
                }
                cell {
                    textExpr(
                        Bokmal to garantitilegg + " kr",
                        Nynorsk to garantitilegg + " kr",
                        English to garantitilegg + " NOK",
                    )
                }
            }
        }

        val annetBelop = beregnetUT.map { it.annetBelop }.format()

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
                    textExpr(
                        Bokmal to annetBelop + " kr",
                        Nynorsk to annetBelop + " kr",
                        English to annetBelop + " NOK",
                    )
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
                    textExpr(
                        Bokmal to annetBelop + " kr",
                        Nynorsk to annetBelop + " kr",
                        English to annetBelop + " NOK",
                    )
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
                val totalBeloep = beregnetUT.map { it.totalUTBeloep }.format()
                textExpr(
                    Bokmal to totalBeloep + " kr",
                    Nynorsk to totalBeloep + " kr",
                    English to totalBeloep + " NOK",
                )
            }
        }
    }
}

data class TabellBeregnetUTAvkortetDto(
    val barnetillegg: Int?,
    val barnetilleggFoerAvkort: Int,
    val garantitilleggNordisk27: Int?,
    val garantitilleggNordisk27FoerAvkorting: Int,
    val ordinaerUTBeloep: Int,
    val ordinaerUTBeloepFoerAvkort: Int,
    val totalUTBeloep: Int,
    val totalUTBeloepFoerAvkort: Int,
)

val tabellBeregnetUTAvkortet = ParagraphPhrase<LangBokmalNynorskEnglish, TabellBeregnetUTAvkortetDto> { beregnetUT ->
    table(
        //tekst109 ufoeretrygd foer og etter fradrag (obligatorisk)
        header = {
            column {
                //TODO: kan ikke lenger ha tom kollonne?
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }
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
                val ordinaerUfoeretrygdFoerAvkorting =
                    beregnetUT.map { it.ordinaerUTBeloepFoerAvkort }
                        .format()
                textExpr(
                    Bokmal to ordinaerUfoeretrygdFoerAvkorting + " kr",
                    Nynorsk to ordinaerUfoeretrygdFoerAvkorting + " kr",
                    English to ordinaerUfoeretrygdFoerAvkorting + " NOK",
                )
            }
            cell {
                val ordinaerUfoeretrygd =
                    beregnetUT.map { it.ordinaerUTBeloep }
                        .format()
                textExpr(
                    Bokmal to ordinaerUfoeretrygd + " kr",
                    Nynorsk to ordinaerUfoeretrygd + " kr",
                    English to ordinaerUfoeretrygd + " NOK",
                )
            }
        }

        //if barnetillegg != "" tekst111
        ifNotNull(beregnetUT.map { it.barnetillegg }) {
            row {
                val barnetilegg = it.format()
                val barnetileggFoerAvkorting =
                    beregnetUT.map { it.barnetilleggFoerAvkort }
                        .format()
                cell {
                    text(
                        Bokmal to "Barnetillegg for særkullsbarn",
                        Nynorsk to "Barnetillegg for særkullsbarn",
                        English to "Child supplement for child(ren) by a previous marriage/relationship",
                    )
                }

                cell {
                    textExpr(
                        Bokmal to barnetileggFoerAvkorting + " kr",
                        Nynorsk to barnetileggFoerAvkorting + " kr",
                        English to barnetileggFoerAvkorting + " NOK",
                    )
                }

                cell {
                    textExpr(
                        Bokmal to barnetilegg + " kr",
                        Nynorsk to barnetilegg + " kr",
                        English to barnetilegg + " NOK",
                    )
                }
            }
        }

        //if garatitilleggNordisk27 !="" tekst112

        ifNotNull(beregnetUT.map { it.garantitilleggNordisk27 }) {
            val garantitillegg = it.format()
            row {
                cell {
                    text(
                        Bokmal to "Nordisk garantitillegg",
                        Nynorsk to "Nordisk garantitillegg",
                        English to "Nordic guarantee supplement",
                    )
                }
                cell {
                    val garantitileggFoerAvkorting =
                        beregnetUT.map { it.garantitilleggNordisk27FoerAvkorting }
                            .format()
                    textExpr(
                        Bokmal to garantitileggFoerAvkorting + " kr",
                        Nynorsk to garantitileggFoerAvkorting + " kr",
                        English to garantitileggFoerAvkorting + " NOK",
                    )
                }
                cell {
                    textExpr(
                        Bokmal to garantitillegg + " kr",
                        Nynorsk to garantitillegg + " kr",
                        English to garantitillegg + " NOK",
                    )
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
                val totalBeloepFoerAvkorting =
                    beregnetUT.map { it.totalUTBeloepFoerAvkort }
                        .format()
                textExpr(
                    Bokmal to totalBeloepFoerAvkorting + " kr",
                    Nynorsk to totalBeloepFoerAvkorting + " kr",
                    English to totalBeloepFoerAvkorting + " NOK",
                )
            }
            cell {
                val totalBeloep =
                    beregnetUT.map { it.totalUTBeloep }
                        .format()
                textExpr(
                    Bokmal to totalBeloep + " kr",
                    Nynorsk to totalBeloep + " kr",
                    English to totalBeloep + " NOK",
                )
            }
        }
    }
}