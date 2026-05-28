package no.nav.pensjon.brev.maler.fraser.gjenlevende

import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalEnglish
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
 * Bruk [bruttoNettoTabell] når brutto != netto (3 kolonner: komponent, brutto, netto).
 * Bruk [kunNettoTabell] når brutto == netto (2 kolonner: komponent, netto).
 *
 * Radhjelperne [bruttoNettoRad]/[nettoRad] tar `Expression<Kroner>` direkte slik at
 * malen kan gjenbrukes på tvers av DTO-er med ulik `Komponent`-modell.
 */

// ---- Outline-blokker -------------------------------------------------------

fun <LetterData : Any> OutlineOnlyScope<LangBokmalEnglish, LetterData>.gjenlevendepensjonBeregningTabellBruttoNetto(
    virkDatoFom: Expression<LocalDate>,
    grunnbeloep: Expression<Kroner>,
    framtidigAarligInntekt: Expression<Kroner>,
    sumBrutto: Expression<Kroner>,
    sumNetto: Expression<Kroner>,
    rader: TableScope<LangBokmalEnglish, LetterData>.() -> Unit,
) {
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
            rader()
            sumBruttoNettoRad(sumBrutto, sumNetto)
        }
    }
}

fun <LetterData : Any> OutlineOnlyScope<LangBokmalEnglish, LetterData>.gjenlevendepensjonBeregningTabellKunNetto(
    virkDatoFom: Expression<LocalDate>,
    grunnbeloep: Expression<Kroner>,
    framtidigAarligInntekt: Expression<Kroner>,
    sumNetto: Expression<Kroner>,
    rader: TableScope<LangBokmalEnglish, LetterData>.() -> Unit,
) {
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
            rader()
            sumNettoRad(sumNetto)
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





