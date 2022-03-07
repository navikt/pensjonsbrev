package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Kroner
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus

data class OpplysningerBruktIBeregningUTDto(
    val avkortningsbelopAr: Kroner,
    val belop: Kroner,
    val belopAr: Kroner,
    val belopArForAvkort: Kroner,
    val inntektBruktIAvkortning: Kroner,
    val inntektstak: Kroner,
    val justeringsbelopAr: Kroner
)

val opplysningerBruktIBeregningUT = createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
    title = newText(
        Bokmal to "Opplysninger om beregningen",
        Nynorsk to "Opplysningar om utrekninga",
        English to "Information about calculations"
    ),
    includeSakspart = false,
) {
    table {
        title {
            text(
                Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                English to "Reduction of child supplement payment for children from a previous relationship before tax"
            )
        }
        columnHeaderRow {
            cell {
                text(
                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                    English to "Yearly child supplement before income reduction"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner(1000) med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::belopArForAvkort).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er",
                    Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er",
                    English to "Total income applied in calculation of reduction in child supplement is"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner(1000) med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::inntektBruktIAvkortning).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        /* IF (
        barnetilleggSBGjeldende.fribelopEllerInntektErPeriodisert == false
        && (
        barnetilleggSBGjeldende.belop != 0
        || barnetilleggSBGjeldende.belop == 0 && barnetilleggSBGjeldende.justeringsbelopAr != 0
        )
        && barnetilleggSBGjeldende.avkortingsbelopAr > 0
        )
        */
        columnHeaderRow {
            cell {
                text(
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                    English to "- 50 percent of income exceeding the allowance amount"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::avkortningsbelopAr).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }

        columnHeaderRow {
            cell {
                text(
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                    English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::avkortningsbelopAr).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                    English to "+ Amount which is used to adjust the reduction of child supplement"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::justeringsbelopAr).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        columnHeaderRow {
            cell {
                text(
                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg etter reduksjon ut frå inntekt",
                    English to "Yearly child supplement after income reduction"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::belopAr).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Utbetaling av barnetillegg per måned",
                    Nynorsk to "Utbetaling av barnetillegg per månad",
                    English to "Child supplement payment for the remaining months of the year"
                )
            }
            cell {
                // TODO: Lag gjenbrukbar formattering av Kroner med språkstøtte og enhet (kr/NOK)
                val belop = argument().select(OpplysningerBruktIBeregningUTDto::belop).format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
        columnHeaderRow {
            cell {
                text(
                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                    English to "The income limit for receiving child supplement"
                )
            }
        }
    }
}
