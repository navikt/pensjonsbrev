package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.brukerErFlyktning
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.antallbarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarFoerAvkort
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.innvilgetBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.ifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.antallbarn
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarFoerAvkort
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.innvilgetBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTNorge
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.nevnerTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.samletTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.tellerTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.utenforEOSogNorden
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.beregningsgrunnlagBeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.erKonvertert
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.UtenforEOSogNordenSelectors.faktiskTTBilateral
import no.nav.pensjon.brev.api.model.vedlegg.UtenforEOSogNordenSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.UtenforEOSogNordenSelectors.tellerProRata
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.beregningsgrunnlagBeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.inntektVedSkadetidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.skadetidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.yrkesskadegrad
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.yrkesskadegrad_safe
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles.KronerText
import no.nav.pensjon.brev.maler.fraser.common.Felles.MaanederText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.tableFormat
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


val vedleggOpplysningerBruktIBeregningUTBarnetillegg =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val harMinsteytelseSats = minsteytelseGjeldende_sats.ifNull(0.0).greaterThan(0.0)
        val inntektsgrenseErUnderTak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()
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
                        English to "Information",
                        FontType.BOLD
                    )
                }
                column(alignment = ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Verdi",
                        Nynorsk to "Verdi",
                        English to "Value",
                        FontType.BOLD
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
                    val ufoeretidspunkt = ufoeretrygdGjeldende.ufoeretidspunkt.format()
                    textExpr(
                        Bokmal to ufoeretidspunkt,
                        Nynorsk to ufoeretidspunkt,
                        English to ufoeretidspunkt
                    )
                }
            }
            showIf(ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Beregningsgrunnlag",
                            Nynorsk to "Utrekningsgrunnlag",
                            English to "Basis for calculation"
                        )
                    }
                    cell {
                        includePhrase(KronerText(ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar))
                    }
                }
            }
            ifNotNull(yrkesskadeGjeldende.beregningsgrunnlagBeloepAar_safe) { beloep ->
                showIf(beloep.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Beregningsgrunnlag yrkesskade",
                                Nynorsk to "Utrekningsgrunnlag yrkesskade",
                                English to "Basis for calculation due to occupational injury"
                            )
                        }
                        cell {
                            includePhrase(KronerText(beloep))
                        }
                    }
                }
            }
            showIf(inntektFoerUfoereGjeldende.ifuInntekt.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt før uførhet",
                            Nynorsk to "Inntekt før uførleik",
                            English to "Income prior to disability"
                        )
                    }
                    cell {
                        includePhrase(KronerText(inntektFoerUfoereGjeldende.ifuInntekt))
                    }
                }
            }
            showIf(inntektEtterUfoereGjeldende_beloepIEU.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt etter uførhet",
                            Nynorsk to "Inntekt etter uførleik",
                            English to "Income after disability"
                        )
                    }
                    cell {
                        includePhrase(KronerText(inntektEtterUfoereGjeldende_beloepIEU))
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
                    val ufoeregrad = ufoeretrygdGjeldende.ufoeregrad.format()
                    textExpr(
                        Bokmal to ufoeregrad + " %",
                        Nynorsk to ufoeregrad + " %",
                        English to ufoeregrad + " %"
                    )
                }
            }
            // Mandatory
            showIf(ufoeretrygdGjeldende.beloepsgrense.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Inntektsgrense",
                            Nynorsk to "Inntektsgrense",
                            English to "Income cap"
                        )
                    }
                    cell {
                        includePhrase(KronerText(ufoeretrygdGjeldende.beloepsgrense))
                    }
                }
            }
            showIf(inntektsAvkortingGjeldende.inntektsgrenseAar.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Inntektsgrense",
                            Nynorsk to "Inntektsgrense",
                            English to "Income cap"
                        )
                    }
                    cell {
                        includePhrase(KronerText(inntektsAvkortingGjeldende.inntektsgrenseAar))
                    }
                }
            }
            showIf(inntektsAvkortingGjeldende.forventetInntektAar.greaterThan(0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Forventet inntekt",
                            Nynorsk to "Forventa inntekt",
                            English to "Expected income"
                        )
                    }
                    cell {
                        includePhrase(KronerText(inntektsAvkortingGjeldende.forventetInntektAar))
                    }
                }
            }
            showIf(inntektsgrenseErUnderTak and ufoeretrygdGjeldende.kompensasjonsgrad.greaterThan(0.0)) {
                row {
                    cell {
                        text(
                            Bokmal to "Kompensasjonsgrad",
                            Nynorsk to "Kompensasjonsgrad",
                            English to "Percentage of compensation"
                        )
                    }
                    cell {
                        val kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad.format()
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
                        includePhrase(KronerText(inntektsAvkortingGjeldende.inntektstak))
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
                        includePhrase(KronerText(inntektsAvkortingGjeldende.inntektsgrenseAar))
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
                    val brukersSivilstand = beregnetUTPerManedGjeldende.brukersSivilstand.tableFormat()
                    textExpr(
                        Bokmal to brukersSivilstand,
                        Nynorsk to brukersSivilstand,
                        English to brukersSivilstand
                    )
                }
            }
            showIf(ungUfoerGjeldende_erUnder20Aar.ifNull(false)) {
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

            ifNotNull(yrkesskadeGjeldende) { yrkesskade ->
                showIf(yrkesskade.yrkesskadegrad.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Yrkesskadegrad",
                                Nynorsk to "Yrkesskadegrad",
                                English to "Degree of disability due to occupational injury"
                            )
                        }
                        cell {
                            val yrkesskadegrad = yrkesskade.yrkesskadegrad.format()
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
                        val skadetidspunkt = yrkesskade.skadetidspunkt.format()
                        textExpr(
                            Bokmal to skadetidspunkt,
                            Nynorsk to skadetidspunkt,
                            English to skadetidspunkt
                        )
                    }
                }
                showIf(yrkesskade.inntektVedSkadetidspunkt.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                English to "Annual income at the date of injury"
                            )
                        }
                        cell {
                            includePhrase(KronerText(yrkesskade.inntektVedSkadetidspunkt))
                        }
                    }
                }
            }

            val beregningsmetode = trygdetidsdetaljerGjeldende.beregningsmetode

            showIf(beregnetUTPerManedGjeldende.brukerErFlyktning and beregningsmetode.isOneOf(FOLKETRYGD)) {
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
                        val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
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
                        val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
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
                        val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
                        textExpr(
                            Bokmal to anvendtTT + " år",
                            Nynorsk to anvendtTT + " år",
                            English to anvendtTT + " years"
                        )
                    }
                }
            }
            ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTNorge) {
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
                            includePhrase(MaanederText(it))
                        }
                    }
                }
            }

            showIf(beregningsmetode.isOneOf(EOS)) {
                ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTEOS) { faktiskTTEOS ->
                    row {
                        cell {
                            text(
                                Bokmal to "Faktisk trygdetid i andre EØS-land",
                                Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                English to "Actual insurance period(s) in other EEA countries"
                            )
                        }
                        cell {
                            includePhrase(MaanederText(faktiskTTEOS))
                        }
                    }
                }

                ifNotNull(trygdetidsdetaljerGjeldende.nevnerTTEOS) { nevnerTTEOS ->
                    row {
                        cell {
                            text(
                                Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                                English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                            )
                        }
                        cell { includePhrase(MaanederText(nevnerTTEOS)) }
                    }
                }

                ifNotNull(
                    trygdetidsdetaljerGjeldende.tellerTTEOS,
                    trygdetidsdetaljerGjeldende.nevnerTTEOS
                ) { tellerTTEOS, nevnerTTEOS ->
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
                ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                    row {
                        cell {
                            text(
                                Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                                English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                            )
                        }
                        cell { includePhrase(MaanederText(faktiskTTNordiskKonv)) }
                    }
                }
            }
            ifNotNull(trygdetidsdetaljerGjeldende.framtidigTTNorsk) { framtidigTTNorsk ->
                showIf(beregningsmetode.isOneOf(NORDISK, FOLKETRYGD) and framtidigTTNorsk.lessThan(480)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Norsk framtidig trygdetid",
                                Nynorsk to "Norsk framtidig trygdetid",
                                English to "Future insurance period in Norway"
                            )
                        }
                        cell {
                            includePhrase(MaanederText(framtidigTTNorsk))
                        }
                    }
                }
            }
            showIf(beregningsmetode.isOneOf(NORDISK)) {
                ifNotNull(
                    trygdetidsdetaljerGjeldende.tellerTTNordiskKonv,
                    trygdetidsdetaljerGjeldende.nevnerTTNordiskKonv
                ) { tellerTTNordiskKonv, nevnerTTNordiskKonv ->
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

                ifNotNull(trygdetidsdetaljerGjeldende.samletTTNordiskKonv) { samletTTNordiskKonv ->
                    row {
                        cell {
                            text(
                                Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                                Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                                English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                            )
                        }
                        cell { includePhrase(MaanederText(samletTTNordiskKonv)) }
                    }
                }
            }

            ifNotNull(trygdetidsdetaljerGjeldende.utenforEOSogNorden) {

                val faktiskTTBilateral = it.faktiskTTBilateral
                val nevnerProRata = it.nevnerProRata
                val tellerProRata = it.tellerProRata

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
                            includePhrase(MaanederText(faktiskTTBilateral))
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
                            includePhrase(MaanederText(nevnerProRata))
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
            ifNotNull(
                barnetilleggGjeldende,
                barnetilleggGjeldende.saerkullsbarn_safe,
                barnetilleggGjeldende.fellesbarn_safe
            ) { barnetillegg, saerkullsbarn, fellesbarn ->
                showIf(saerkullsbarn.beloep.greaterThan(0) or fellesbarn.beloep.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Totalt antall barn du har barnetillegg for",
                                Nynorsk to "Totalt tal barn du har barnetillegg for",
                                English to "Total number of children for whom you receive child supplement"
                            )
                        }
                        cell {
                            val totaltAntallBarn =
                                ((saerkullsbarn.antallbarn).format() + (fellesbarn.antallbarn).format())
                            textExpr(
                                Bokmal to totaltAntallBarn,
                                Nynorsk to totaltAntallBarn,
                                English to totaltAntallBarn
                            )
                        }
                    }
                    showIf(saerkullsbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp for særkullsbarn",
                                    Nynorsk to "Fribeløp for særkullsbarn",
                                    English to "Exemption amount for children from a previous relationship"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullsbarn.fribeloep))
                            }
                        }
                    }
                    showIf(fellesbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp for fellesbarn",
                                    Nynorsk to "Fribeløp for fellessbarn",
                                    English to "Exemption amount for joint children"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.fribeloep))
                            }
                        }
                    }
                    showIf(not(fellesbarn.innvilgetBarnetillegg)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                    Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                                    English to "Your income, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullsbarn.inntektBruktIAvkortning))
                            }
                        }
                    }.orShowIf(fellesbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                    Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                                    English to "Your income, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.inntektBruktIAvkortning))
                            }
                        }
                    }
                    showIf(fellesbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg",
                                    Nynorsk to "Samla inntekt til annen forelder som er brukt i fastsetjinga av barnetillegg",
                                    English to "Income of the other parent, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.inntektAnnenForelder))
                            }
                        }
                    }
                    showIf(saerkullsbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                                    Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                                    English to "Your income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullsbarn.inntektstak))
                            }
                        }
                    }.orShowIf(fellesbarn.innvilgetBarnetillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt",
                                    Nynorsk to "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt",
                                    English to "Total combined income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.inntektstak))
                            }
                        }
                    }
                }
            }
        }

        showIf(harMinsteytelseSats) {
            includePhrase(RettTilMYOverskrift_001)
        }

        showIf(harMinsteytelseSats) {
            ifNotNull(ungUfoerGjeldende_erUnder20Aar) { erUnder20Aar ->
                showIf(erUnder20Aar) {
                    includePhrase(VedleggBeregnUTInfoMYUngUforUnder20_001)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMYUngUfor_001)
                }
            }.orShow {
                showIf(ufoeretrygdGjeldende.erKonvertert) {
                    includePhrase(VedleggBeregnUTInfoMY2_001)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMY_001)
                }
            }
        }

        ifNotNull(minsteytelseGjeldende_sats) {
            showIf(harMinsteytelseSats) {
                includePhrase(VedleggBeregnUTDinMY_001(it))
            }
        }

        showIf(inntektFoerUfoereGjeldende.erSannsynligEndret) {
            includePhrase(VedleggBeregnUTMinsteIFU_002)
        }

        showIf(
            harMinsteytelseSats
                and inntektFoerUfoereGjeldende.erSannsynligEndret
                and inntektsgrenseErUnderTak
        ) {

            includePhrase(SlikFastsettesKompGradOverskrift_001)
            includePhrase(VedleggBeregnUTKompGrad_001)

            showIf(ufoeretrygdGjeldende.erKonvertert) {
                includePhrase(VedleggBeregnUTKompGradGjsnttKonvUT_001)
            }.orShow {
                includePhrase(VedleggBeregnUTKompGradGjsntt_001)
            }
        }

        ifNotNull(
            barnetilleggGjeldende.saerkullsbarn_safe,
        ) { saerkullTillegg ->
            val fribeloepEllerInntektErPeriodisert = saerkullTillegg.fribeloepEllerInntektErPeriodisert
            val harYrkesskadeGrad = yrkesskadeGjeldende.yrkesskadegrad_safe.ifNull(0).greaterThan(0)
            val harAnvendtTrygdetidUnder40 = trygdetidsdetaljerGjeldende.anvendtTT.lessThan(40)
            val justeringsBeloepAr = saerkullTillegg.justeringsbeloepAar

            showIf(saerkullTillegg.erRedusertMotinntekt) {
                includePhrase(SlikBeregnBTOverskrift_001)
                includePhrase(VedleggBeregnUTInfoBTSB_001)
                includePhrase(VedleggBeregnUTInnlednBT_001)
            }

            showIf(harAnvendtTrygdetidUnder40 and harYrkesskadeGrad) {
                includePhrase(VedleggBeregnUTredusTTBTSB_001)
            }

            showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.greaterThan(0)) {
                includePhrase(VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001(saerkullTillegg.avkortningsbeloepAar))
            }

            showIf(not(fribeloepEllerInntektErPeriodisert) and justeringsBeloepAr.greaterThan(0)) {
                includePhrase(VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop_001(saerkullTillegg.avkortningsbeloepAar))
            }

            showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.greaterThan(0)) {
                includePhrase(VedleggBeregnUTPeridisertFriBOgInntektBTSB_001(saerkullTillegg.avkortningsbeloepAar, saerkullTillegg.harFlereBarn))
            }

            showIf(fribeloepEllerInntektErPeriodisert and not(justeringsBeloepAr.greaterThan(0))) {
                includePhrase(VedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001(saerkullTillegg.avkortningsbeloepAar, saerkullTillegg.harFlereBarn))
            }



// TABLE 2 Saerkullsbarn - start
            showIf(saerkullTillegg.erRedusertMotinntekt) {
                title1 {
                    text(
                        Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                        Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                        English to "Reduction of child supplement payment for children from a previous relationship before tax"
                    )
                }

                showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001(saerkullTillegg.avkortningsbeloepAar))
                }

                showIf(not(fribeloepEllerInntektErPeriodisert) and justeringsBeloepAr.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop_001(saerkullTillegg.avkortningsbeloepAar))
                }

                showIf(fribeloepEllerInntektErPeriodisert and justeringsBeloepAr.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTPeridisertFriBOgInntektBTSB_001(saerkullTillegg.avkortningsbeloepAar, saerkullTillegg.harFlereBarn))
                }

                showIf(fribeloepEllerInntektErPeriodisert and not(justeringsBeloepAr.greaterThan(0))) {
                    includePhrase(VedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001(saerkullTillegg.avkortningsbeloepAar, saerkullTillegg.harFlereBarn))
                }

                showIf(justeringsBeloepAr.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTJusterBelopOver0BTSB_001(saerkullTillegg.justeringsbeloepAar))
                }
                // TODO: < 0? Is there a minus operator from Pesys?
                showIf(justeringsBeloepAr.lessThan(0)) {
                    includePhrase(VedleggBeregnUTJusterBelopUnder0BTSB_001(saerkullTillegg.justeringsbeloepAar))
                }

                table(
                    header = {
                        column(2) {
                            text(
                                Bokmal to "Beskrivelse",
                                Nynorsk to "Beskrivelse",
                                English to "Description",
                                FontType.BOLD
                            )
                        }
                        column(alignment = ColumnAlignment.RIGHT) {
                            text(
                                Bokmal to "Beløp",
                                Nynorsk to "Beløp",
                                English to "Amount",
                                FontType.BOLD,
                            )
                        }
                    }
                ) {
                    showIf(
                        saerkullTillegg.beloep.greaterThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                            0
                        )
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                    English to "Yearly child supplement before income reduction"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.beloepAarFoerAvkort))
                            }
                        }
                    }
                    showIf(saerkullTillegg.erRedusertMotinntekt) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                    Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                    English to "Total income applied in calculation of reduction in child supplement is ",
                                )
                            }

                            cell {
                                includePhrase(KronerText(saerkullTillegg.inntektBruktIAvkortning))
                            }
                        }
                    }
                    showIf(
                        saerkullTillegg.beloep.greaterThan(0) or (saerkullTillegg.beloep.lessThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                            0
                        ))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                    Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                    English to "Exemption amount applied in calculation of reduction in child supplement is",
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.fribeloep))
                            }
                        }
                    }
                    showIf(
                        saerkullTillegg.beloep.notEqualTo(0) or (saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                            0
                        ))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Inntekt over fribeløpet er",
                                    Nynorsk to "Inntekt over fribeløpet er",
                                    English to "Income exceeding the exemption amount is",
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.inntektOverFribeloep))
                            }
                        }
                    }
                    showIf(
                        not(saerkullTillegg.fribeloepEllerInntektErPeriodisert)
                            and (saerkullTillegg.beloep.notEqualTo(0) or (saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.beloepAar.notEqualTo(
                            0
                        )))
                            and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
                    ) {
                        row {
                            cell {
                                text( // TODO finn en fornuftig måte å vise regnestykket på
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                                    English to "- 50 percent of income exceeding the allowance amount"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.avkortningsbeloepAar))
                            }
                        }
                    }
                    showIf(
                        saerkullTillegg.fribeloepEllerInntektErPeriodisert
                            and (saerkullTillegg.beloep.notEqualTo(0) or (saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.beloepAar.notEqualTo(
                            0
                        )))
                            and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                                    English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.avkortningsbeloepAar))
                            }
                        }
                    }
                    showIf(saerkullTillegg.justeringsbeloepAar.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                                    English to "+ Amount which is used to adjust the reduction of child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.justeringsbeloepAar))
                            }
                        }
                    }
                    showIf(
                        saerkullTillegg.beloep.notEqualTo(0) or (saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.beloepAar.notEqualTo(
                            0
                        ))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                    English to "= Yearly child supplement after income reduction",
                                    FontType.BOLD,
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.beloepAar))
                            }
                        }
                    }
                    showIf(
                        saerkullTillegg.beloep.notEqualTo(0) or (saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                            0
                        ))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Utbetaling av barnetillegg per måned",
                                    Nynorsk to "Utbetaling av barnetillegg per månad",
                                    English to "Child supplement payment for the remaining months of the year"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.beloep))
                            }
                        }
                    }
                    showIf(saerkullTillegg.beloep.equalTo(0) and saerkullTillegg.beloepAar.equalTo(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                    English to "The income limit for receiving child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullTillegg.inntektstak))
                            }
                        }
                    }
                }
            }
        }  // TABLE 2 - end

        // FELLESBARN - start
        ifNotNull(
            barnetilleggGjeldende.fellesbarn_safe,
        ) { fellesTillegg ->

            // TABLE 2 Fellesbarn - start
            showIf(fellesTillegg.erRedusertMotinntekt) {
                title1 {
                    text(
                        Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt",
                        Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt",
                        English to "Reduction of child supplement payment for joint children before tax"
                    )
                }
                showIf(fellesTillegg.justeringsbeloepAar.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTJusterBelopOver0BTFB(fellesTillegg.justeringsbeloepAar))
                }

                showIf(fellesTillegg.justeringsbeloepAar.lessThan(0)) {
                    includePhrase(VedleggBeregnUTJusterBelopUnder0BTFB(fellesTillegg.justeringsbeloepAar))
                }

                table(
                    header = {
                        column(columnSpan = 2) {
                            text(
                                Bokmal to "Beskrivelse",
                                Nynorsk to "Beskrivelse",
                                English to "Description",
                                FontType.BOLD
                            )
                        }
                        column(alignment = ColumnAlignment.RIGHT) {
                            text(
                                Bokmal to "Beløp",
                                Nynorsk to "Beløp",
                                English to "Amount",
                                FontType.BOLD
                            )
                        }
                    }
                ) {
                    showIf(fellesTillegg.beloep.greaterThan(0) and fellesTillegg.justeringsbeloepAar.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                    English to "Yearly child supplement before income reduction"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.beloepAarFoerAvkort))
                            }
                        }
                    }
                    showIf(fellesTillegg.erRedusertMotinntekt) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                    Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                    English to "Total income applied in calculation of reduction in child supplement is "
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.inntektBruktIAvkortning))
                            }
                        }
                    }
                    showIf(fellesTillegg.beloep.greaterThan(0) and fellesTillegg.justeringsbeloepAar.notEqualTo(0))
                    {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                    Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                    English to "Exemption amount applied in calculation of reduction in child supplement is"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.fribeloep))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.beloep.notEqualTo(0) or (fellesTillegg.beloep.equalTo(0))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Inntekt over fribeløpet er",
                                    Nynorsk to "Inntekt over fribeløpet er",
                                    English to "Income exceeding the exemption amount is",
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.inntektOverFribeloep))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.beloep.notEqualTo(0) and fellesTillegg.justeringsbeloepAar.notEqualTo(0) and fellesTillegg.avkortningsbeloepAar.greaterThan(
                            0
                        )
                    ) {
                        row {
                            cell {
                                text( // TODO finn en fornuftig måte å vise regnestykket på
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløep",
                                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                                    English to "- 50 percent of income exceeding the allowance amount"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.avkortningsbeloepAar))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.justeringsbeloepAar.notEqualTo(0)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Nynorsk to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    English to "+ Amount which is used to adjust the reduction of child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.justeringsbeloepAar))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.beloep.notEqualTo(0) or (fellesTillegg.beloep.equalTo(0) and fellesTillegg.beloepAar.notEqualTo(
                            0
                        ))
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                    English to "= Yearly child supplement after income reduction",
                                    FontType.BOLD,
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.beloepAar))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.beloep.greaterThan(0) or fellesTillegg.beloep.equalTo(0)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Utbetaling av barnetillegg per måned",
                                    Nynorsk to "Utbetaling av barnetillegg per månad",
                                    English to "Child supplement payment for the remaining months of the year"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.beloep))
                            }
                        }
                    }
                    showIf(
                        fellesTillegg.beloep.equalTo(0) and fellesTillegg.justeringsbeloepAar.equalTo(0)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                    English to "The income limit for receiving child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesTillegg.inntektstak))
                            }
                        }
                    }
                }
            } // TABLE 2 Felles barn - end
        }
    }




        /*showIf(saerkullTillegg.beloep.greaterThan(0)) {
                includePhrase(VedleggBeregnUTredusBTSBPgaInntekt_001(saerkullTillegg.beloep))
            }.orShowIf(saerkullTillegg.beloep.equalTo(0)) {
                showIf(saerkullTillegg.justeringsbeloepAar.equalTo(0)) {
                    includePhrase(VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt_001)
                } orShow {
                    includePhrase(VedleggBeregnUTJusterBelopIkkeUtbetalt_001)
                }
            }

                ifNotNull(
                    barnetilleggGjeldende.fellesbarn_safe,
                ) { fellesbarn ->
                    showIf(fellesbarn.beloep.greaterThan(0)) {
                        includePhrase(
                            MaanedligTilleggFellesbarn(
                                beloep_barnetilleggFBGjeldende = fellesbarn.beloep,
                                harFlereBarn = fellesbarn.harFlereBarn,
                            )
                        )
                    }
                    showIf(fellesbarn.innvilgetBarnetillegg) {
                        includePhrase(
                            FaaIkkeUtbetaltTilleggFellesbarn(
                                beloep_barnetilleggFBGjeldende = fellesbarn.beloep,
                                justeringsbeloepAar_barnetilleggFBGjeldende = fellesbarn.justeringsbeloepAar,
                                harFlereBarn = fellesbarn.harFlereBarn,
                            )
                        )
                    }
                }
            }
        }

*/
