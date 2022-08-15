package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles.kroner
import no.nav.pensjon.brev.maler.fraser.common.Felles.maaneder
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.tableFormat
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


val opplysningerBruktIBeregningUT = createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
    title = newText(
        Bokmal to "Opplysninger om beregningen",
        Nynorsk to "Opplysningar om utrekninga",
        English to "Information about calculations"
    ),
    includeSakspart = false,
) {
    val harMinsteytelseSats = argument().map { it.minsteytelseGjeldende_sats > 0 }
    val ufoeretrygdGjeldendeErKonvertert = argument().map { it.ufoeretrygdGjeldende.erKonvertert }
    val erUnder20AarVedUngUfoere = argument().map { it.ungUfoerGjeldende_erUnder20Aar }
    val inntektFoerUfoereErSannsynligEndret = argument().map { it.inntektFoerUfoereGjeldende.erSannsynligEndret }
    val inntektsgrenseErUnderTak =
        argument().map { it.inntektsAvkortingGjeldende.inntektsgrenseAar.value < it.inntektsAvkortingGjeldende.inntektstak.value }
    val ufoeretrygdErKonvertert = argument().map { it.ufoeretrygdGjeldende.erKonvertert }

    paragraph {
        val virkDatoFom = argument().map { it.beregnetUTPerManedGjeldende.virkDatoFom }.format()
        val grunnbeloep = argument().map { it.beregnetUTPerManedGjeldende.grunnbeloep }.format()
        textExpr(
            Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner",
            Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + " Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner",
            English to "Data we have used in the calculations as of ".expr() + virkDatoFom + " The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + "."
        )
    }
// Start of table 1
    table(
        header = {
            column(3) {
                text(
                    Bokmal to "Opplysning",
                    Nynorsk to "Opplysning",
                    English to "Information"
                )
            }
            column(alignment = Element.Table.ColumnAlignment.RIGHT) {
                text(
                    Bokmal to "Verdi",
                    Nynorsk to "Verdi",
                    English to "Value"
                )
            }
        }
    ) {
        // Hvis ufoeretrygdGjeldende.uforetidspunkt != tom, include
        row {
            cell {
                text(
                    Bokmal to "Uføretidspunkt",
                    Nynorsk to "Uføretidspunkt",
                    English to "Date of disability"
                )
            }
            cell {
                val ufoeretidspunkt =
                    argument().map { it.ufoeretrygdGjeldende.ufoeretidspunkt }.format()
                textExpr(
                    Bokmal to ufoeretidspunkt,
                    Nynorsk to ufoeretidspunkt,
                    English to ufoeretidspunkt
                )
            }
        }
        showIf(argument().map { it.ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Beregningsgrunnlag",
                        Nynorsk to "Utrekningsgrunnlag",
                        English to "Basis for calculation"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar })
                }
            }
        }
        ifNotNull(argument().map { it.yrkesskadeGjeldende?.beregningsgrunnlagBeloepAar }) { beloep ->
            showIf(beloep.map { it.value > 0 })
            {
                row {
                    cell {
                        text(
                            Bokmal to "Beregningsgrunnlag yrkesskade",
                            Nynorsk to "Utrekningsgrunnlag yrkesskade",
                            English to "Basis for calculation due to occupational injury"
                        )
                    }
                    cell {
                        includePhrase(kroner, beloep)
                    }
                }
            }
        }
        showIf(argument().map { it.inntektFoerUfoereGjeldende.ifuInntekt.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt før uførhet",
                        Nynorsk to "Inntekt før uførleik",
                        English to "Income prior to disability"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektFoerUfoereGjeldende.ifuInntekt })
                }
            }
        }
        showIf(argument().map { it.inntektEtterUfoereGjeldende_beloepIEU.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt etter uførhet",
                        Nynorsk to "Inntekt etter uførleik",
                        English to "Income after disability"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektEtterUfoereGjeldende_beloepIEU })
                }
            }
        }
        // Mandatory
        row {
            cell {
                text(
                    Bokmal to "Uføregrad",
                    Nynorsk to "Uføregrad",
                    English to "Degree of disability"
                )
            }
            cell {
                val ufoeregrad = argument().map { it.ufoeretrygdGjeldende.ufoeregrad }.format()
                textExpr(
                    Bokmal to ufoeregrad + " %",
                    Nynorsk to ufoeregrad + " %",
                    English to ufoeregrad + " %"
                )
            }
        }
        // Mandatory
        showIf(argument().map { it.ufoeretrygdGjeldende.beloepsgrense.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntektsgrense",
                        Nynorsk to "Inntektsgrense",
                        English to "Income cap"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.ufoeretrygdGjeldende.beloepsgrense })
                }
            }
        }
        showIf(argument().map { it.inntektsAvkortingGjeldende.inntektsgrenseAar.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntektsgrense",
                        Nynorsk to "Inntektsgrense",
                        English to "Income cap"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektsAvkortingGjeldende.inntektsgrenseAar })
                }
            }
        }
        showIf(argument().map { it.inntektsAvkortingGjeldende.forventetInntektAar.value > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Forventet inntekt",
                        Nynorsk to "Forventa inntekt",
                        English to "Expected income"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektsAvkortingGjeldende.forventetInntektAar })
                }
            }
        }
        showIf(inntektsgrenseErUnderTak
                and argument().map { it.ufoeretrygdGjeldende.kompensasjonsgrad > 0 }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Kompensasjonsgrad",
                        Nynorsk to "Kompensasjonsgrad",
                        English to "Percentage of compensation"
                    )
                }
                cell {
                    val kompensasjonsgrad =
                        argument().map { it.ufoeretrygdGjeldende.kompensasjonsgrad }.format()
                    textExpr(
                        Bokmal to kompensasjonsgrad + " %",
                        Nynorsk to kompensasjonsgrad + " %",
                        English to kompensasjonsgrad + " %"
                    )
                }
            }
        }
        showIf(inntektsgrenseErUnderTak) {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                        Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                        English to "Income that will lead to no payment of your disability benefit"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektsAvkortingGjeldende.inntektstak })
                }
            }
        }.orShow {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                        Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                        English to "Income that will lead to no payment of your disability benefit"
                    )
                }
                cell {
                    includePhrase(kroner, argument().map { it.inntektsAvkortingGjeldende.inntektsgrenseAar })
                }
            }
        }
        // Mandatory
        row {
            cell {
                text(
                    Bokmal to "Sivilstatus lagt til grunn i beregningen",
                    Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                    English to "Marital status applied to calculation"
                )
            }
            cell {
                val brukersSivilstand =
                    argument().map { it.beregnetUTPerManedGjeldende.brukersSivilstand }.tableFormat()
                textExpr(
                    Bokmal to brukersSivilstand,
                    Nynorsk to brukersSivilstand,
                    English to brukersSivilstand
                )
            }
        }
        showIf(argument().map { it.ungUfoerGjeldende_erUnder20Aar ?: false }) {
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
        }

        ifNotNull(argument().map { it.yrkesskadeGjeldende }) { yrkesskade ->
            showIf(yrkesskade.map { it.yrkesskadegrad > 0 }) {
                row {
                    cell {
                        text(
                            Bokmal to "Yrkesskadegrad",
                            Nynorsk to "Yrkesskadegrad",
                            English to "Degree of disability due to occupational injury"
                        )
                    }
                    cell {
                        val yrkesskadegrad = yrkesskade.map { it.yrkesskadegrad }.format()
                        textExpr(
                            Bokmal to yrkesskadegrad + " %",
                            Nynorsk to yrkesskadegrad + " %",
                            English to yrkesskadegrad + " %"
                        )
                    }
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
                    val skadetidspunkt = yrkesskade.map { it.skadetidspunkt }.format()
                    textExpr(
                        Bokmal to skadetidspunkt,
                        Nynorsk to skadetidspunkt,
                        English to skadetidspunkt
                    )
                }
            }
            showIf(yrkesskade.map { it.inntektVedSkadetidspunkt.value > 0 })
            {
                row {
                    cell {
                        text(
                            Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                            Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                            English to "Annual income at the date of injury"
                        )
                    }
                    cell {
                        includePhrase(kroner, yrkesskade.map { it.inntektVedSkadetidspunkt })
                    }
                }
            }
        }

        val beregningsmetode = argument().map { it.trygdetidsdetaljerGjeldende.beregningsmetode }
        val brukerErFlyktning = argument().map { it.beregnetUTPerManedGjeldende.brukerErFlyktning }

        showIf(brukerErFlyktning and beregningsmetode.isOneOf(FOLKETRYGD)) {
            row {
                cell {
                    text(
                        Bokmal to "Du er innvilget flyktningstatus fra UDI",
                        Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                        English to "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)"
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
                        Bokmal to "Trygdetid (maksimalt 40 år)",
                        Nynorsk to "Trygdetid (maksimalt 40 år)",
                        English to "Insurance period (maximum 40 years)"
                    )
                }
                cell {
                    val anvendtTT = argument().map { it.trygdetidsdetaljerGjeldende.anvendtTT }.format()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }

        }

        showIf(beregningsmetode.isOneOf(EOS, NORDISK)) {
            row {
                cell {
                    text(
                        Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                        Nynorsk to "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i utrekninga (maksimalt 40 år)",
                        English to "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)"
                    )
                }
                // Implement logic for year/years
                cell {
                    val anvendtTT =
                        argument().map { it.trygdetidsdetaljerGjeldende.anvendtTT }.format()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }
        }
        showIf(not(beregningsmetode.isOneOf(EOS, NORDISK, FOLKETRYGD))) {
            row {
                cell {
                    text(
                        Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                        Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i utrekninga (maksimalt 40 år)",
                        English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)"
                    )
                }
                cell {
                    val anvendtTT =
                        argument().map { it.trygdetidsdetaljerGjeldende.anvendtTT }.format()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }
        }
        ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.faktiskTTNorge }) {
            showIf(beregningsmetode.isOneOf(FOLKETRYGD)) {
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i Norge",
                            Nynorsk to "Faktisk trygdetid i Noreg",
                            English to "Actual insurance period in Norway"
                        )
                    }
                    cell {
                        includePhrase(maaneder, it)

                    }
                }
            }
        }


        showIf(beregningsmetode.isOneOf(EOS)) {
            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.faktiskTTEOS }) { faktiskTTEOS ->
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i andre EØS-land",
                            Nynorsk to "Faktisk trygdetid i andre EØS-land",
                            English to "Actual insurance period(s) in other EEA countries"
                        )
                    }
                    cell {
                        includePhrase(maaneder, faktiskTTEOS)
                    }
                }
            }

            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.nevnerTTEOS }) { nevnerTTEOS ->
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                            Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                            English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                        )
                    }
                    cell { includePhrase(maaneder, nevnerTTEOS) }
                }
            }

            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.tellerTTEOS },
                argument().map { it.trygdetidsdetaljerGjeldende.nevnerTTEOS }) { tellerTTEOS, nevnerTTEOS ->
                row {
                    cell {
                        text(
                            Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                            Nynorsk to "Forholdstalet brukt i utrekning av trygdetid",
                            English to "Ratio applied in calculation of insurance period"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                            Nynorsk to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                            English to tellerTTEOS.format() + " / " + nevnerTTEOS.format()
                        )
                    }
                }
            }
        }

        showIf(beregningsmetode.isOneOf(NORDISK)) {
            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.faktiskTTNordiskKonv }) { faktiskTTNordiskKonv ->
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                            Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                            English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                        )
                    }
                    cell { includePhrase(maaneder, faktiskTTNordiskKonv) }
                }
            }
        }
        ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.framtidigTTNorsk }) { framtidigTTNorsk ->
            showIf(beregningsmetode.isOneOf(NORDISK, FOLKETRYGD) and framtidigTTNorsk.map { it < 480 }) {
                row {
                    cell {
                        text(
                            Bokmal to "Norsk framtidig trygdetid",
                            Nynorsk to "Norsk framtidig trygdetid",
                            English to "Future insurance period in Norway"
                        )
                    }
                    cell {
                        includePhrase(maaneder, framtidigTTNorsk)
                    }
                }
            }
        }
        showIf(beregningsmetode.isOneOf(NORDISK)) {
            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.tellerTTNordiskKonv },
                argument().map { it.trygdetidsdetaljerGjeldende.nevnerTTNordiskKonv }) { tellerTTNordiskKonv, nevnerTTNordiskKonv ->
                row {
                    cell {
                        text(
                            Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                            Nynorsk to "Forholdstalet brukt i reduksjon av norsk framtidig trygdetid",
                            English to "Ratio applied in reduction of future Norwegian insurance period"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                            Nynorsk to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                            English to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format()
                        )
                    }
                }
            }

            ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.samletTTNordiskKonv }) { samletTTNordiskKonv ->
                row {
                    cell {
                        text(
                            Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                            Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                            English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                        )
                    }
                    cell { includePhrase(maaneder, samletTTNordiskKonv) }
                }
            }
        }

        ifNotNull(argument().map { it.trygdetidsdetaljerGjeldende.utenforEOSogNorden }) {

            val faktiskTTBilateral = it.select(UtenforEOSogNorden::faktiskTTBilateral)
            val nevnerProRata = it.select(UtenforEOSogNorden::nevnerProRata)
            val tellerProRata = it.select(UtenforEOSogNorden::tellerProRata)

            showIf(beregningsmetode.isNotAnyOf(FOLKETRYGD, NORDISK, EOS)) {
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i annet avtaleland",
                            Nynorsk to "Faktisk trygdetid i anna avtaleland",
                            English to "Actual insurance period(s) in another partner country"
                        )
                    }
                    cell {
                        includePhrase(maaneder, faktiskTTBilateral)
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                            Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                            English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                        )
                    }
                    cell {
                        includePhrase(maaneder, nevnerProRata)
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                            Nynorsk to "Forholdstalet brukt i utrekning av uføretrygd",
                            English to "Ratio applied in calculation of insurance period"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to tellerProRata.format() + " / " + nevnerProRata.format(),
                            Nynorsk to tellerProRata.format() + " / " + nevnerProRata.format(),
                            English to tellerProRata.format() + " / " + nevnerProRata.format()
                        )
                    }
                }
            }
        }
        ifNotNull(argument().map { it.barnetilleggGjeldende }) { barnetillegg ->

            ifNotNull(barnetillegg.map { it.saerkullsbarn }) { saerkullsbarn ->
                showIf(saerkullsbarn.map { it.beloep.value > 0 })
                {
                    row {
                        cell {
                            text(
                                Bokmal to "Totalt antall barn du har barnetillegg for",
                                Nynorsk to "Totalt tal barn du har barnetillegg for",
                                English to "Total number of children for whom you receive child supplement"
                            )
                        }
                        cell {
                            val totaltAntallBarn = barnetillegg.map { it.grunnlag.totaltAntallBarn }.format()
                            textExpr(
                                Bokmal to totaltAntallBarn,
                                Nynorsk to totaltAntallBarn,
                                English to totaltAntallBarn
                            )
                        }
                    }
                }
            }

            ifNotNull(barnetillegg.map { it.saerkullsbarn }) { saerkullsbarn ->
                showIf(saerkullsbarn.map { it.beloep.value > 0 }) {
                    row {
                        cell {
                            text(
                                Bokmal to "Fribeløp for særkullsbarn",
                                Nynorsk to "Fribeløp for særkullsbarn",
                                English to "Exemption amount for children from a previous relationship"
                            )
                        }
                        cell {
                            includePhrase(kroner, saerkullsbarn.map { it.fribeloep })
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                                English to "Your income, which is used to calculate child supplement"
                            )
                        }
                        cell {
                            includePhrase(kroner, saerkullsbarn.map { it.inntektBruktIAvkortning })
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                                Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                                English to "Your income which means that no child supplement is received"
                            )
                        }
                        cell {
                            includePhrase(kroner, saerkullsbarn.map { it.inntektstak })
                        }
                    }
                }
            }
        }
    }


    showIf(harMinsteytelseSats) {
        includePhrase(rettTilMYOverskrift_001)
    }

    showIf(harMinsteytelseSats) {
        ifNotNull(erUnder20AarVedUngUfoere) { erUnder20Aar ->
            showIf(erUnder20Aar) {
                includePhrase(vedleggBeregnUTInfoMYUngUforUnder20_001)
            }.orShow {
                includePhrase(vedleggBeregnUTInfoMYUngUfor_001)
            }
        }
        showIf(erUnder20AarVedUngUfoere.map { it == null }) {
            showIf(ufoeretrygdGjeldendeErKonvertert) {
                includePhrase(vedleggBeregnUTInfoMY2_001)
            }.orShow {
                includePhrase(vedleggBeregnUTInfoMY_001)
            }
        }
    }

    showIf(harMinsteytelseSats) {
        includePhrase(vedleggBeregnUTDinMY_001, argument().map { it.minsteytelseGjeldende_sats })
    }

    showIf(inntektFoerUfoereErSannsynligEndret) {
        includePhrase(vedleggBeregnUTMinsteIFU_002)
    }

    showIf(
        harMinsteytelseSats
                and inntektFoerUfoereErSannsynligEndret
                and inntektsgrenseErUnderTak
    ) {

        includePhrase(slikFastsettesKompGradOverskrift_001)
        includePhrase(vedleggBeregnUTKompGrad_001)

        showIf(ufoeretrygdErKonvertert) {
            includePhrase(vedleggBeregnUTKompGradGjsnttKonvUT_001)
        }.orShow {
            includePhrase(vedleggBeregnUTKompGradGjsntt_001)
        }
    }

    ifNotNull(
        argument().map { it.barnetilleggGjeldende?.grunnlag },
        argument().map { it.barnetilleggGjeldende?.saerkullsbarn }
    ) { grunnlag, saerkullTillegg ->
        val erRedusertMotInntekt = saerkullTillegg.map { it.erRedusertMotinntekt }
        val fribeloepEllerInntektErPeriodisert = saerkullTillegg.map { it.fribeloepEllerInntektErPeriodisert }
        val harYrkesskadeGrad = argument().map {
            it.yrkesskadeGjeldende?.let { skade -> skade.yrkesskadegrad > 0 } ?: false
        }
        val harAnvendtTrygdetidUnder40 = argument().map { it.trygdetidsdetaljerGjeldende.anvendtTT < 40 }
        val justeringsBeloepAr = saerkullTillegg.map { it.justeringsbeloepAar }

        showIf(saerkullTillegg.map { it.erRedusertMotinntekt }) {
                includePhrase(slikBeregnBTOverskrift_001)
                includePhrase(vedleggBeregnUTInfoBTSB_001)
                includePhrase(vedleggBeregnUTInnlednBT_001)
            }

            showIf(harAnvendtTrygdetidUnder40 and harYrkesskadeGrad) {
                includePhrase(vedleggBeregnUTredusTTBTSB_001)
            }

            showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.map { it.value > 0 }) {
                includePhrase(
                    vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001,
                    saerkullTillegg.map { it.avkortningsbeloepAar }
                )
            }

            showIf(not(fribeloepEllerInntektErPeriodisert) and justeringsBeloepAr.map { it.value > 0 }) {
                includePhrase(
                    vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop_001,
                    saerkullTillegg.map { it.avkortningsbeloepAar })
            }

            showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.map { it.value > 0 }) {
                includePhrase(vedleggBeregnUTPeridisertFriBOgInntektBTSB_001,
                    saerkullTillegg.map { it.avkortningsbeloepAar })
            }

            showIf(fribeloepEllerInntektErPeriodisert and not(justeringsBeloepAr.map { it.value > 0 })) {
                includePhrase(
                    vedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001,
                    saerkullTillegg.map { it.avkortningsbeloepAar })
            }

            showIf(justeringsBeloepAr.map { it.value > 0 }) {
                includePhrase(
                    vedleggBeregnUTJusterBelopOver0BTSB_001,
                    saerkullTillegg.map { it.justeringsbeloepAar }
                )
            }
            showIf(justeringsBeloepAr.map { it.value < 0 }) {// < 0? Is there a minus operator from Pesys?
                includePhrase(
                    vedleggBeregnUTJusterBelopUnder0BTSB_001,
                    saerkullTillegg.map { it.justeringsbeloepAar }
                )
            }



// TABLE 2 - start

    showIf(erRedusertMotInntekt) {
                title1 {
                    text(
                        Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                        Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                        English to "Reduction of child supplement payment for children from a previous relationship before tax"
                    )
                }
                table(
                    header = {
                        column(2) {
                            text(
                                Bokmal to "Beskrivelse",
                                Nynorsk to "Beskrivelse",
                                English to "Description"
                            )
                        }
                        column(alignment = Element.Table.ColumnAlignment.RIGHT) {
                            text(
                                Bokmal to "Beløp",
                                Nynorsk to "Beløp",
                                English to "Amount"
                            )
                        }
                    }
                ) {
                    showIf(saerkullTillegg.map { it.beloep.value > 0 && it.justeringsbeloepAar.value != 0 }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                    English to "Yearly child supplement before income reduction"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.beloepAarFoerAvkort })
                            }
                        }
                    }
                    showIf(erRedusertMotInntekt) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                    Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                    English to "Total income applied in calculation of reduction in child supplement is ",
                                )
                            }

                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.inntektBruktIAvkortning })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { (it.beloep.value > 0 || (it.beloep.value < 0 && it.justeringsbeloepAar.value != 0)) }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                    Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                    English to "Exemption amount applied in calculation of reduction in child supplement is",
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.fribeloep })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { (it.beloep.value != 0 || (it.beloep.value == 0 && it.justeringsbeloepAar.value != 0)) }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Inntekt over fribeløpet er",
                                    Nynorsk to "Inntekt over fribeløpet er",
                                    English to "Income exceeding the exemption amount is",
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.inntektOverFribeloep })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map {
                        !it.fribeloepEllerInntektErPeriodisert
                                && (it.beloep.value != 0 || (it.beloep.value == 0 && it.beloepAar.value != 0))
                                && it.avkortningsbeloepAar.value > 0
                    }) {
                        row {
                            cell {
                                text( // TODO finn en fornuftig måte å vise regnestykket på
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                                    English to "- 50 percent of income exceeding the allowance amount"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.avkortningsbeloepAar })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map {
                        it.fribeloepEllerInntektErPeriodisert
                                && (it.beloep.value != 0 || (it.beloep.value == 0 && it.beloepAar.value != 0))
                                && it.avkortningsbeloepAar.value > 0
                    }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                                    English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.avkortningsbeloepAar })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { it.justeringsbeloepAar.value != 0 }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                                    English to "+ Amount which is used to adjust the reduction of child supplement"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.justeringsbeloepAar })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { it.beloep.value != 0 || (it.beloep.value == 0 && it.beloepAar.value != 0) }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                    Nynorsk to "Årleg barnetillegg etter reduksjon ut frå inntekt",
                                    English to "Yearly child supplement after income reduction"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.beloepAar })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { (it.beloep.value != 0 || (it.beloep.value == 0 && it.justeringsbeloepAar.value != 0)) }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Utbetaling av barnetillegg per måned",
                                    Nynorsk to "Utbetaling av barnetillegg per månad",
                                    English to "Child supplement payment for the remaining months of the year"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.beloep })
                            }
                        }
                    }
                    showIf(saerkullTillegg.map { it.beloep.value == 0 && it.beloepAar.value == 0 }) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                    English to "The income limit for receiving child supplement"
                                )
                            }
                            cell {
                                includePhrase(kroner, saerkullTillegg.map { it.inntektstak })
                            }
                        }
                    }
                }
            }
            // TABLE 2 - end
            showIf(saerkullTillegg.map { it.beloep.value > 0 }) {
                includePhrase(
                    vedleggBeregnUTredusBTSBPgaInntekt_001,
                    saerkullTillegg.map { it.beloep }
                )
            }
            showIf(saerkullTillegg.map { it.beloep.value == 0 && it.justeringsbeloepAar.value == 0 }) {
                includePhrase(vedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt_001)
            }
            showIf(saerkullTillegg.map { it.beloep.value == 0 && it.justeringsbeloepAar.value != 0 }) {
                includePhrase(vedleggBeregnUTJusterBelopIkkeUtbetalt_001)
            }
        }
    }
