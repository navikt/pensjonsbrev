package no.nav.pensjon.brev.maler.fraser.gjenlevende

import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Gjenlevendepensjon-beregningstabell (Bokmål/Engelsk).
 *
 * Genererer "Din månedlige gjenlevendepensjon ..."-blokken med innledende kulepunkter
 * (grunnbeløp + framtidig årlig inntekt) og en tabell med ytelseskomponenter.
 *
 * Bruk [GjenlevendepensjonBeregningTabell.BruttoNetto] når brutto != netto
 * (3 kolonner: komponent, brutto, netto).
 * Bruk [GjenlevendepensjonBeregningTabell.KunNetto] når brutto == netto
 * (2 kolonner: komponent, netto).
 */

// ---- Outline-blokker -------------------------------------------------------

object GjenlevendepensjonBeregningTabell {
    data class BruttoNetto(
        val virkDatoFom: Expression<LocalDate>,
        val grunnbeloep: Expression<Kroner>,
        val framtidigAarligInntekt: Expression<Kroner>,
        val grunnpensjonBrutto: Expression<Kroner>,
        val grunnpensjonNetto: Expression<Kroner>,
        val tilleggspensjonBrutto: Expression<Kroner?>,
        val tilleggspensjonNetto: Expression<Kroner?>,
        val saertilleggBrutto: Expression<Kroner?>,
        val saertilleggNetto: Expression<Kroner?>,
        val fasteUtgifterBrutto: Expression<Kroner?>,
        val fasteUtgifterNetto: Expression<Kroner?>,
        val familietilleggBrutto: Expression<Kroner?>,
        val familietilleggNetto: Expression<Kroner?>,
        val sumBrutto: Expression<Kroner>,
        val sumNetto: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {

            title1 {
                text(
                    bokmal { +"Din månedlige gjenlevendepensjon fra " + virkDatoFom.format() },
                    english { +"Your survivor's pension from " + virkDatoFom.format() },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal {
                                +"Folketrygdens grunnbeløp (G) benyttet i beregningen er " + grunnbeloep.format() + "."
                            },
                            english {
                                +"The national insurance basic amount (G) used in the calculation is " + grunnbeloep.format() + "."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Framtidig årlig inntekt benyttet i beregningen er " + framtidigAarligInntekt.format() + "."
                            },
                            english {
                                +"Expected future earned income used in the calculation is " + framtidigAarligInntekt.format() + "."
                            },
                        )
                    }
                }
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned før fradrag for inntekt" },
                                english { +"Pension per month before deduction for income" },
                            )
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                                english { +"Pension per month after deduction for income" },
                            )
                        }
                    },
                ) {
                    bruttoNettoRad("Grunnpensjon", "Basic pension", grunnpensjonBrutto, grunnpensjonNetto)
                    ifNotNull(tilleggspensjonBrutto) { tpBrutto ->
                        ifNotNull(tilleggspensjonNetto) { tpNetto ->
                            bruttoNettoRad("Tilleggspensjon", "Supplementary pension", tpBrutto, tpNetto)
                        }
                    }
                    ifNotNull(saertilleggBrutto) { stBrutto ->
                        ifNotNull(saertilleggNetto) { stNetto ->
                            bruttoNettoRad("Særtillegg", "Special supplement", stBrutto, stNetto)
                        }
                    }
                    ifNotNull(fasteUtgifterBrutto) { fuBrutto ->
                        ifNotNull(fasteUtgifterNetto) { fuNetto ->
                            bruttoNettoRad(
                                "Faste utgifter ved institusjonsopphold",
                                "Fixed costs when institutionalised",
                                fuBrutto,
                                fuNetto,
                            )
                        }
                    }
                    ifNotNull(familietilleggBrutto) { ftBrutto ->
                        ifNotNull(familietilleggNetto) { ftNetto ->
                            bruttoNettoRad("Familietillegg", "Family supplement", ftBrutto, ftNetto)
                        }
                    }
                    sumBruttoNettoRad(sumBrutto, sumNetto)
                }
            }
        }
    }

    data class KunNetto(
        val virkDatoFom: Expression<LocalDate>,
        val grunnbeloep: Expression<Kroner>,
        val framtidigAarligInntekt: Expression<Kroner>,
        val grunnpensjonNetto: Expression<Kroner>,
        val tilleggspensjonNetto: Expression<Kroner?>,
        val saertilleggNetto: Expression<Kroner?>,
        val fasteUtgifterNetto: Expression<Kroner?>,
        val familietilleggNetto: Expression<Kroner?>,
        val sumNetto: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Din månedlige gjenlevendepensjon fra " + virkDatoFom.format() },
                    english { +"Your monthly survivor's pension from " + virkDatoFom.format() },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal {
                                +"Folketrygdens grunnbeløp (G) benyttet i beregningen er " + grunnbeloep.format() + "."
                            },
                            english {
                                +"The national insurance basic amount (G) used in the calculation is " + grunnbeloep.format() + "."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Framtidig årlig inntekt benyttet i beregningen er " + framtidigAarligInntekt.format() + "."
                            },
                            english {
                                +"Expected future earned income used in the calculation is " + framtidigAarligInntekt.format() + "."
                            },
                        )
                    }
                }
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2) {}
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned" },
                                english { +"Pension per month" },
                            )
                        }
                    },
                ) {
                    nettoRad("Grunnpensjon", "Basic pension", grunnpensjonNetto)
                    ifNotNull(tilleggspensjonNetto) { tpNetto ->
                        nettoRad("Tilleggspensjon", "Supplementary pension", tpNetto)
                    }
                    ifNotNull(saertilleggNetto) { stNetto ->
                        nettoRad("Særtillegg", "Special supplement", stNetto)
                    }
                    ifNotNull(fasteUtgifterNetto) { fuNetto ->
                        nettoRad(
                            "Faste utgifter ved institusjonsopphold",
                            "Fixed costs when institutionalised",
                            fuNetto,
                        )
                    }
                    ifNotNull(familietilleggNetto) { ftNetto ->
                        nettoRad("Familietillegg", "Family supplement", ftNetto)
                    }
                    sumNettoRad(sumNetto)
                }
            }
        }
    }
}

/**
 * Ytelser-per-måned-tabell uten innledende tittel/kulepunkter (Bokmål/Engelsk).
 *
 * Brukes av oversikts-vedlegget der hver beregningsperiode allerede har egen
 * overskrift og grunnbeløp-/inntektstekst over tabellen.
 *
 * Bruk [YtelserPerMaanedTabell.BruttoNetto] når brutto != netto, og
 * [YtelserPerMaanedTabell.KunNetto] når brutto == netto.
 */
object YtelserPerMaanedTabell {
    data class BruttoNetto(
        val grunnpensjonBrutto: Expression<Kroner>,
        val grunnpensjonNetto: Expression<Kroner>,
        val tilleggspensjonBrutto: Expression<Kroner?>,
        val tilleggspensjonNetto: Expression<Kroner?>,
        val saertilleggBrutto: Expression<Kroner?>,
        val saertilleggNetto: Expression<Kroner?>,
        val fasteUtgifterBrutto: Expression<Kroner?>,
        val fasteUtgifterNetto: Expression<Kroner?>,
        val familietilleggBrutto: Expression<Kroner?>,
        val familietilleggNetto: Expression<Kroner?>,
        val sumBrutto: Expression<Kroner>,
        val sumNetto: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned før fradrag for inntekt" },
                                english { +"Pension per month before deduction for income" },
                            )
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                                english { +"Pension per month after deduction for income" },
                            )
                        }
                    },
                ) {
                    bruttoNettoRad("Grunnpensjon", "Basic pension", grunnpensjonBrutto, grunnpensjonNetto)
                    ifNotNull(tilleggspensjonBrutto) { tpBrutto ->
                        ifNotNull(tilleggspensjonNetto) { tpNetto ->
                            bruttoNettoRad("Tilleggspensjon", "Supplementary pension", tpBrutto, tpNetto)
                        }
                    }
                    ifNotNull(saertilleggBrutto) { stBrutto ->
                        ifNotNull(saertilleggNetto) { stNetto ->
                            bruttoNettoRad("Særtillegg", "Special supplement", stBrutto, stNetto)
                        }
                    }
                    ifNotNull(fasteUtgifterBrutto) { fuBrutto ->
                        ifNotNull(fasteUtgifterNetto) { fuNetto ->
                            bruttoNettoRad(
                                "Faste utgifter ved institusjonsopphold",
                                "Fixed costs when institutionalised",
                                fuBrutto,
                                fuNetto,
                            )
                        }
                    }
                    ifNotNull(familietilleggBrutto) { ftBrutto ->
                        ifNotNull(familietilleggNetto) { ftNetto ->
                            bruttoNettoRad("Familietillegg", "Family supplement", ftBrutto, ftNetto)
                        }
                    }
                    sumBruttoNettoRad(sumBrutto, sumNetto, bold = true)
                }
            }
        }
    }

    data class KunNetto(
        val grunnpensjonNetto: Expression<Kroner>,
        val tilleggspensjonNetto: Expression<Kroner?>,
        val saertilleggNetto: Expression<Kroner?>,
        val fasteUtgifterNetto: Expression<Kroner?>,
        val familietilleggNetto: Expression<Kroner?>,
        val sumNetto: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned" },
                                english { +"Pension per month" },
                            )
                        }
                    },
                ) {
                    nettoRad("Grunnpensjon", "Basic pension", grunnpensjonNetto)
                    ifNotNull(tilleggspensjonNetto) { nettoRad("Tilleggspensjon", "Supplementary pension", it) }
                    ifNotNull(saertilleggNetto) { nettoRad("Særtillegg", "Special supplement", it) }
                    ifNotNull(fasteUtgifterNetto) {
                        nettoRad("Faste utgifter ved institusjonsopphold", "Fixed costs when institutionalised", it)
                    }
                    ifNotNull(familietilleggNetto) { nettoRad("Familietillegg", "Family supplement", it) }
                    sumNettoRad(sumNetto, bold = true)
                }
            }
        }
    }
}

// ---- Radhjelpere -----------------------------------------------------------

fun <LetterData : Any> TableScope<LangBokmalEnglish, LetterData>.bruttoNettoRad(
    bokmal: String,
    english: String,
    brutto: Expression<Kroner>,
    netto: Expression<Kroner>,
) {
    row {
        cell { text(bokmal { +bokmal }, english { +english }) }
        cell { includePhrase(KronerText(brutto)) }
        cell { includePhrase(KronerText(netto)) }
    }
}

fun <LetterData : Any> TableScope<LangBokmalEnglish, LetterData>.nettoRad(
    bokmal: String,
    english: String,
    netto: Expression<Kroner>,
) {
    row {
        cell { text(bokmal { +bokmal }, english { +english }) }
        cell { includePhrase(KronerText(netto)) }
    }
}

internal fun <LetterData : Any> TableScope<LangBokmalEnglish, LetterData>.sumBruttoNettoRad(
    brutto: Expression<Kroner>,
    netto: Expression<Kroner>,
    bold: Boolean = true,
) {
    val font = if (bold) BOLD else PLAIN
    row {
        cell {
            text(
                bokmal { +"Sum pensjon før skatt" },
                english { +"Total pension before tax" },
                font,
            )
        }
        cell { includePhrase(KronerText(brutto, font)) }
        cell { includePhrase(KronerText(netto, font)) }
    }
}

internal fun <LetterData : Any> TableScope<LangBokmalEnglish, LetterData>.sumNettoRad(
    netto: Expression<Kroner>,
    bold: Boolean = true,
) {
    val font = if (bold) BOLD else PLAIN
    row {
        cell {
            text(
                bokmal { +"Sum pensjon før skatt" },
                english { +"Total pension before tax" },
                font,
            )
        }
        cell { includePhrase(KronerText(netto, font)) }
    }
}

// ---- Private hjelpere ------------------------------------------------------





