package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Kroner
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate


data class OpplysningerBruktIBeregningUTDto(
    val avkortningsbelopAr: Kroner,
    val belop: Kroner,
    val belopAr: Kroner,
    val belopArForAvkort: Kroner,
    val belopIEU_inntektEtterUforeGjeldende: Kroner,
    val belopsgrense_uforetrygdGjeldende: Kroner,
    val beregningsgrunnlagBelopAr_uforetrygdGjeldende: Kroner,
    val beregningsgrunnlagBelopAr_yrkesskadeGjeldende: Kroner,
    val brukersSivilstand_gjeldendeBeregnetUTPerManed: String,
    val forventetInntektAr_inntektsAvkortingGjeldende: Kroner,
    val grunnbelop_gjeldendeBeregnetUTPerManed: Kroner,
    val ifuInntekt_inntektForUforeGjeldende: Kroner,
    val inntektBruktIAvkortning: Kroner,
    val inntektsgrenseAr_inntektsAvkortingGjeldende: Kroner,
    val inntektstak_inntektsAvkortingGjeldende: Kroner,
    val inntektVedSkadetidspunkt_yrkesskadeGjeldende: Kroner,
    val justeringsbelopAr: Kroner,
    val kompensasjonsgrad_uforetrygdGjeldende: Double,
    val skadetidspunkt_yrkesskadeGjeldende: LocalDate,
    val uforegrad_uforetrygdGjeldende: Int,
    val uforetidspunkt_uforetrygdGjeldende: LocalDate,
    val virkDatoFom_gjeldendeBeregnetUTPerManed: LocalDate,
    val yrkesskadegrad_yrkesskadeGjeldende: Int

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
            val virkDatoFom = argument().select(OpplysningerBruktIBeregningUTDto::virkDatoFom_gjeldendeBeregnetUTPerManed).format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom.str(),
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom.str(),
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom.str()
            )
            val grunnbelop = argument().select(OpplysningerBruktIBeregningUTDto::grunnbelop_gjeldendeBeregnetUTPerManed).format()
            textExpr(
                Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbelop.str() + " kroner",
                Nynorsk to "Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbelop.str() + " kroner",
                English to "The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbelop.str() + "."
            )
        }
        columnHeaderRow {
            cell {
                text(
                    Bokmal to "?????",
                    Nynorsk to "?????",
                    English to "?????"
                )
            }
            cell {
                text(
                    Bokmal to "?????",
                    Nynorsk to "?????",
                    English to "?????"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Uføretidspunkt",
                    Nynorsk to "Uføretidspunkt",
                    English to "Date of disability"
                )
            }
            cell {
                val uforetidspunkt = argument().select(OpplysningerBruktIBeregningUTDto::uforetidspunkt_uforetrygdGjeldende).format()
                textExpr(
                    Bokmal to uforetidspunkt,
                    Nynorsk to uforetidspunkt,
                    English to uforetidspunkt
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Beregningsgrunnlag",
                    Nynorsk to "Utrekningsgrunnlag",
                    English to "Basis for calculation"
                )
            }
            cell {
                val beregningsgrunnlagBelopAr = argument().select(OpplysningerBruktIBeregningUTDto::beregningsgrunnlagBelopAr_uforetrygdGjeldende).format()
                textExpr(
                    Bokmal to beregningsgrunnlagBelopAr + " kr",
                    Nynorsk to beregningsgrunnlagBelopAr + " kr",
                    English to beregningsgrunnlagBelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Beregningsgrunnlag yrkesskade",
                    Nynorsk to "Utrekningsgrunnlag yrkesskade",
                    English to "Basis for calculation due to occupational injury"
                )
            }
            cell {
                val beregningsgrunnlagBelopAr = argument().select(OpplysningerBruktIBeregningUTDto::beregningsgrunnlagBelopAr_yrkesskadeGjeldende).format()
                textExpr(
                    Bokmal to beregningsgrunnlagBelopAr + " kr",
                    Nynorsk to beregningsgrunnlagBelopAr + " kr",
                    English to beregningsgrunnlagBelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt før uførhet",
                    Nynorsk to "Inntekt før uførleik",
                    English to "Income prior to disability"
                )
            }
            cell {
             val ifuInntekt = argument().select(OpplysningerBruktIBeregningUTDto::ifuInntekt_inntektForUforeGjeldende).format()
             textExpr(
                 Bokmal to ifuInntekt + " kr",
                 Nynorsk to ifuInntekt + " kr",
                 English to ifuInntekt + " NOK"
             )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt etter uførhet",
                    Nynorsk to "Inntekt etter uførleik",
                    English to "Income after disability"
                )
            }
            cell {
             val belopIEU = argument().select(OpplysningerBruktIBeregningUTDto::belopIEU_inntektEtterUforeGjeldende).format()
                textExpr(
                    Bokmal to belopIEU + " kr",
                    Nynorsk to belopIEU + " kr",
                    English to belopIEU + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Uføregrad",
                    Nynorsk to "Uføregrad",
                    English to "Degree of disability"
                )
            }
            cell {
                val uforegrad = argument().select(OpplysningerBruktIBeregningUTDto::uforegrad_uforetrygdGjeldende).str()
                    textExpr(
                        Bokmal to uforegrad + " %",
                        Nynorsk to uforegrad + " %",
                        English to uforegrad + " %"
                    )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntektsgrense",
                    Nynorsk to "Inntektsgrense",
                    English to "Income cap"
                )
            }
            cell {
                val belopsgrense = argument().select(OpplysningerBruktIBeregningUTDto::belopsgrense_uforetrygdGjeldende).format()
                textExpr(
                    Bokmal to belopsgrense + " kr",
                    Nynorsk to belopsgrense + " kr",
                    English to belopsgrense + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntektsgrense",
                    Nynorsk to "Inntektsgrense",
                    English to "Income cap"
                )
            }
            cell {
                val inntektsgrenseAr = argument().select(OpplysningerBruktIBeregningUTDto::inntektsgrenseAr_inntektsAvkortingGjeldende).format()
                textExpr(
                    Bokmal to inntektsgrenseAr + " kr",
                    Nynorsk to inntektsgrenseAr + " kr",
                    English to inntektsgrenseAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Forventet inntekt",
                    Nynorsk to "Forventa inntekt",
                    English to "Expected income"
                )
            }
            cell {
                val forventetInntektAr = argument().select(OpplysningerBruktIBeregningUTDto::forventetInntektAr_inntektsAvkortingGjeldende).format()
                textExpr(
                    Bokmal to forventetInntektAr + " kr",
                    Nynorsk to forventetInntektAr + " kr",
                    English to forventetInntektAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Kompensasjonsgrad",
                    Nynorsk to "Kompensasjonsgrad",
                    English to "Percentage of compensation"
                )
            }
            cell {
                val kompensasjonsgrad = argument().select(OpplysningerBruktIBeregningUTDto::kompensasjonsgrad_uforetrygdGjeldende).format()
                textExpr(
                    Bokmal to kompensasjonsgrad + " %",
                    Nynorsk to kompensasjonsgrad + " %",
                    English to kompensasjonsgrad + " %"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                    Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                    English to "Income that will lead to no payment of your disability benefit"
                )
            }
            cell {
                val inntektstak = argument().select(OpplysningerBruktIBeregningUTDto::inntektstak_inntektsAvkortingGjeldende).format()
                textExpr(
                    Bokmal to inntektstak + " kr",
                    Nynorsk to inntektstak + " kr",
                    English to inntektstak + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                    Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                    English to "Income that will lead to no payment of your disability benefit"
                )
            }
            cell {
                val inntektsgrenseAr = argument().select(OpplysningerBruktIBeregningUTDto::inntektsgrenseAr_inntektsAvkortingGjeldende).format()
                textExpr(
                    Bokmal to inntektsgrenseAr + " kr",
                    Nynorsk to inntektsgrenseAr + " kr",
                    English to inntektsgrenseAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Sivilstatus lagt til grunn i beregningen",
                    Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                    English to "Marital status applied to calculation"
                )
            }
            cell {
                val brukersSivilstand = argument().select(OpplysningerBruktIBeregningUTDto::brukersSivilstand_gjeldendeBeregnetUTPerManed).str()
                textExpr(
                    Bokmal to brukersSivilstand,
                    Nynorsk to brukersSivilstand,
                    English to brukersSivilstand
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Ung ufør",
                    Nynorsk to "Ung ufør",
                    English to "Young disabled"
                )
            }
            cell {
                text(
                    Bokmal to "Ja",
                    Nynorsk to "Ja",
                    English to "Yes"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Yrkesskadegrad",
                    Nynorsk to "Yrkesskadegrad",
                    English to "Degree of disability due to occupational injury"
                )
            }
            cell {
                val yrkesskadegrad = argument().select(OpplysningerBruktIBeregningUTDto::yrkesskadegrad_yrkesskadeGjeldende).str()
                textExpr(
                    Bokmal to yrkesskadegrad + " %",
                    Nynorsk to yrkesskadegrad + " %",
                    English to yrkesskadegrad + " %"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Skadetidspunktet for yrkesskaden",
                    Nynorsk to "Skadetidspunktet for yrkesskaden",
                    English to "Date of injury"
                )
            }
            cell {
                val skadetidspunkt = argument().select(OpplysningerBruktIBeregningUTDto::skadetidspunkt_yrkesskadeGjeldende).format()
                textExpr(
                    Bokmal to skadetidspunkt,
                    Nynorsk to skadetidspunkt,
                    English to skadetidspunkt
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                    Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                    English to "Annual income at the date of injury"
                )
            }
            cell {
                val inntektVedSkadetidspunkt = argument().select(OpplysningerBruktIBeregningUTDto::inntektVedSkadetidspunkt_yrkesskadeGjeldende).format()
                textExpr(
                    Bokmal to inntektVedSkadetidspunkt + " kr",
                    Nynorsk to inntektVedSkadetidspunkt + " kr",
                    English to inntektVedSkadetidspunkt + " NOK"
                )
            }
        }
    }


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
    }
}
