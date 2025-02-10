package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepFratrukketAnnenForeldersInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.samletInntektBruktIAvkortning_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektBruktIAvkortning_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.fellesbarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.foedselsdatoPaaBarnTilleggetGjelder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.saerkullsbarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BeregnetUTPerManedGjeldendeSelectors.brukerErFlyktning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektFoerUfoereGjeldendeSelectors.ifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektsAvkortingGjeldendeSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.UtenforEOSogNordenSelectors.faktiskTTBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.UtenforEOSogNordenSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.UtenforEOSogNordenSelectors.tellerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.faktiskTTNorge
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.nevnerTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.samletTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.tellerTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.utenforEOSogNorden
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.UfoeretrygdGjeldendeSelectors.beregningsgrunnlagBeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.UfoeretrygdGjeldendeSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.UfoeretrygdGjeldendeSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.UfoeretrygdGjeldendeSelectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.YrkesskadeGjeldendeSelectors.beregningsgrunnlagBeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.YrkesskadeGjeldendeSelectors.inntektVedSkadetidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.YrkesskadeGjeldendeSelectors.skadetidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.YrkesskadeGjeldendeSelectors.yrkesskadegrad
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.tableFormat
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TabellUfoereOpplysninger(
    val ufoeretrygdGjeldende: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende>,
    val yrkesskadeGjeldende: Expression<OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende?>,
    val inntektFoerUfoereGjeldende: Expression<OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende>,
    val inntektsAvkortingGjeldende: Expression<OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende>,
    val inntektsgrenseErUnderTak: Expression<Boolean>,
    val beregnetUTPerManedGjeldende: Expression<OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende>,
    val inntektEtterUfoereGjeldendeBeloep: Expression<Kroner?>,
    val erUngUfoer: Expression<Boolean>,
    val trygdetidsdetaljerGjeldende: Expression<OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende>,
    val barnetilleggGjeldende: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende?>,
    val harMinsteytelse: Expression<Boolean>,
    val brukersSivilstand: Expression<Sivilstand>,
    val borMedSivilstand: Expression<BorMedSivilstand?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(4) {
                        text(
                            Language.Bokmal to "Opplysning",
                            Language.Nynorsk to "Opplysning",
                            Language.English to "Information",
                        )
                    }
                    column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Uføretidspunkt",
                            Language.Nynorsk to "Uføretidspunkt",
                            Language.English to "Date of disability"
                        )
                    }
                    cell {
                        val ufoeretidspunkt = ufoeretrygdGjeldende.ufoeretidspunkt.format()
                        textExpr(
                            Language.Bokmal to ufoeretidspunkt,
                            Language.Nynorsk to ufoeretidspunkt,
                            Language.English to ufoeretidspunkt
                        )
                    }
                }
                showIf(ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar.greaterThan(0) and not(harMinsteytelse)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Beregningsgrunnlag",
                                Language.Nynorsk to "Utrekningsgrunnlag",
                                Language.English to "Basis for calculation"
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
                                    Language.Bokmal to "Beregningsgrunnlag yrkesskade",
                                    Language.Nynorsk to "Utrekningsgrunnlag yrkesskade",
                                    Language.English to "Basis for calculation due to occupational injury"
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
                                Language.Bokmal to "Inntekt før uførhet",
                                Language.Nynorsk to "Inntekt før uførleik",
                                Language.English to "Income prior to disability"
                            )
                        }
                        cell {
                            includePhrase(KronerText(inntektFoerUfoereGjeldende.ifuInntekt))
                        }
                    }
                }
                ifNotNull(inntektEtterUfoereGjeldendeBeloep) { beloep ->
                    showIf(beloep.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Inntekt etter uførhet",
                                    Language.Nynorsk to "Inntekt etter uførleik",
                                    Language.English to "Income after disability"
                                )
                            }
                            cell {
                                includePhrase(KronerText(beloep))
                            }
                        }
                    }
                }
                row {
                    cell {
                        text(
                            Language.Bokmal to "Uføregrad",
                            Language.Nynorsk to "Uføregrad",
                            Language.English to "Degree of disability"
                        )
                    }
                    cell {
                        val ufoeregrad = ufoeretrygdGjeldende.ufoeregrad.format()
                        textExpr(
                            Language.Bokmal to ufoeregrad + " %",
                            Language.Nynorsk to ufoeregrad + " %",
                            Language.English to ufoeregrad + " %"
                        )
                    }
                }
                showIf(inntektsAvkortingGjeldende.inntektsgrenseAar.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Inntektsgrense",
                                Language.Nynorsk to "Inntektsgrense",
                                Language.English to "Income cap"
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
                                Language.Bokmal to "Forventet inntekt",
                                Language.Nynorsk to "Forventa inntekt",
                                Language.English to "Expected income"
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
                                Language.Bokmal to "Kompensasjonsgrad",
                                Language.Nynorsk to "Kompensasjonsgrad",
                                Language.English to "Percentage of compensation"
                            )
                        }
                        cell {
                            val kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad.format()
                            textExpr(
                                Language.Bokmal to kompensasjonsgrad + " %",
                                Language.Nynorsk to kompensasjonsgrad + " %",
                                Language.English to kompensasjonsgrad + " %"
                            )
                        }
                    }
                }
                showIf(inntektsgrenseErUnderTak) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                                Language.Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                                Language.English to "Income that will lead to no payment of your disability benefit"
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
                                Language.Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                                Language.Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                                Language.English to "Income that will lead to no payment of your disability benefit"
                            )
                        }
                        cell {
                            includePhrase(KronerText(inntektsAvkortingGjeldende.inntektsgrenseAar))
                        }
                    }
                }

                showIf(harMinsteytelse) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Sivilstatus lagt til grunn i beregningen",
                                Language.Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                                Language.English to "Marital status applied to calculation"
                            )
                        }
                        cell {
                            ifNotNull(borMedSivilstand){
                                textExpr(
                                    Language.Bokmal to it.tableFormat(),
                                    Language.Nynorsk to it.tableFormat(),
                                    Language.English to it.tableFormat()
                                )
                            }.orShowIf(brukersSivilstand.isOneOf(Sivilstand.ENKE)) {
                                text(
                                    Language.Bokmal to "Enke/Enkemann",
                                    Language.Nynorsk to "Enkje/Enkjemann",
                                    Language.English to "Widow/widower",
                                )
                            }.orShow {
                                text(
                                    Language.Bokmal to "Enslig",
                                    Language.Nynorsk to "Einsleg",
                                    Language.English to "Single",
                                )
                            }
                        }
                    }

                    ifNotNull(borMedSivilstand){ borMedSivilstand->
                        showIf(borMedSivilstand.isOneOf(
                            BorMedSivilstand.GIFT_LEVER_ADSKILT,
                            BorMedSivilstand.PARTNER_LEVER_ADSKILT
                        )) {
                            val erGift = borMedSivilstand.isOneOf(BorMedSivilstand.GIFT_LEVER_ADSKILT)
                            row {
                                cell {
                                    textExpr(
                                        Language.Bokmal to "Du eller ".expr()
                                                + ifElse(erGift, "ektefellen", "partneren") +
                                                " er registrert med annet bosted, eller er på institusjon",
                                        Language.Nynorsk to "Du eller ".expr()
                                                + ifElse(erGift, "ektefellen", "partnaren") +
                                                " er registrert med annan bustad, eller er på institusjon",
                                        Language.English to "You or your ".expr()
                                                + ifElse(erGift, "spouse", "partner") +
                                                " have been registered as having a different address, or as living in an institution",
                                    )
                                }
                                cell {
                                    text(
                                        Language.Bokmal to "Ja",
                                        Language.Nynorsk to "Ja",
                                        Language.English to "Yes",
                                    )
                                }
                            }
                        }

                    }
                }

                showIf(erUngUfoer) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Ung ufør",
                                Language.Nynorsk to "Ung ufør",
                                Language.English to "Young disabled"
                            )
                        }
                        cell {
                            text(
                                Language.Bokmal to "Ja",
                                Language.Nynorsk to "Ja",
                                Language.English to "Yes"
                            )
                        }
                    }
                }

                ifNotNull(yrkesskadeGjeldende) { yrkesskade ->
                    showIf(yrkesskade.yrkesskadegrad.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Yrkesskadegrad",
                                    Language.Nynorsk to "Yrkesskadegrad",
                                    Language.English to "Degree of disability due to occupational injury"
                                )
                            }
                            cell {
                                val yrkesskadegrad = yrkesskade.yrkesskadegrad.format()
                                textExpr(
                                    Language.Bokmal to yrkesskadegrad + " %",
                                    Language.Nynorsk to yrkesskadegrad + " %",
                                    Language.English to yrkesskadegrad + " %"
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Skadetidspunktet for yrkesskaden",
                                Language.Nynorsk to "Skadetidspunktet for yrkesskaden",
                                Language.English to "Date of injury"
                            )
                        }
                        cell {
                            val skadetidspunkt = yrkesskade.skadetidspunkt.format()
                            textExpr(
                                Language.Bokmal to skadetidspunkt,
                                Language.Nynorsk to skadetidspunkt,
                                Language.English to skadetidspunkt
                            )
                        }
                    }
                    showIf(yrkesskade.inntektVedSkadetidspunkt.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                    Language.Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                    Language.English to "Annual income at the date of injury"
                                )
                            }
                            cell {
                                includePhrase(KronerText(yrkesskade.inntektVedSkadetidspunkt))
                            }
                        }
                    }
                }

                val beregningsmetode = trygdetidsdetaljerGjeldende.beregningsmetode

                showIf(beregnetUTPerManedGjeldende.brukerErFlyktning) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Du er innvilget flyktningstatus fra UDI",
                                Language.Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                                Language.English to "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)"
                            )
                        }
                        cell {
                            text(
                                Language.Bokmal to "Ja",
                                Language.Nynorsk to "Ja",
                                Language.English to "Yes"
                            )
                        }
                    }
                }
                showIf(beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Trygdetid (maksimalt 40 år)",
                                Language.Nynorsk to "Trygdetid (maksimalt 40 år)",
                                Language.English to "Insurance period (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Language.Bokmal to anvendtTT + " år",
                                Language.Nynorsk to anvendtTT + " år",
                                Language.English to anvendtTT + " years"
                            )
                        }
                    }
                }

                showIf(beregningsmetode.isOneOf(Beregningsmetode.EOS, Beregningsmetode.NORDISK)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                                Language.Nynorsk to "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i utrekninga (maksimalt 40 år)",
                                Language.English to "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)"
                            )
                        }
                        // Implement logic for year/years
                        cell {
                            val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Language.Bokmal to anvendtTT + " år",
                                Language.Nynorsk to anvendtTT + " år",
                                Language.English to anvendtTT + " years"
                            )
                        }
                    }
                }
                showIf(
                    not(
                        beregningsmetode.isOneOf(
                            Beregningsmetode.EOS,
                            Beregningsmetode.NORDISK,
                            Beregningsmetode.FOLKETRYGD
                        )
                    )
                ) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                                Language.Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i utrekninga (maksimalt 40 år)",
                                Language.English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Language.Bokmal to anvendtTT + " år",
                                Language.Nynorsk to anvendtTT + " år",
                                Language.English to anvendtTT + " years"
                            )
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTNorge) {
                    showIf(beregningsmetode.isNotAnyOf(Beregningsmetode.FOLKETRYGD)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg",
                                    Language.English to "Actual insurance period in Norway"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(it))
                            }
                        }
                    }
                }

                showIf(beregningsmetode.isOneOf(Beregningsmetode.EOS)) {
                    ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTEOS) { faktiskTTEOS ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Language.Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                    Language.English to "Actual insurance period(s) in other EEA countries"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTEOS))
                            }
                        }
                    }

                    ifNotNull(trygdetidsdetaljerGjeldende.nevnerTTEOS) { nevnerTTEOS ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                                    Language.English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(nevnerTTEOS)) }
                        }
                    }

                    ifNotNull(
                        trygdetidsdetaljerGjeldende.tellerTTEOS,
                        trygdetidsdetaljerGjeldende.nevnerTTEOS
                    ) { tellerTTEOS, nevnerTTEOS ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                                    Language.Nynorsk to "Forholdstalet brukt i utrekning av trygdetid",
                                    Language.English to "Ratio applied in calculation of insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                                    Language.Nynorsk to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                                    Language.English to tellerTTEOS.format() + " / " + nevnerTTEOS.format()
                                )
                            }
                        }
                    }
                }

                showIf(beregningsmetode.isOneOf(Beregningsmetode.NORDISK)) {
                    ifNotNull(trygdetidsdetaljerGjeldende.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                    Language.Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                                    Language.English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(faktiskTTNordiskKonv)) }
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljerGjeldende.framtidigTTNorsk) { framtidigTTNorsk ->
                    showIf(
                        beregningsmetode.isOneOf(
                            Beregningsmetode.NORDISK,
                            Beregningsmetode.FOLKETRYGD
                        ) and framtidigTTNorsk.lessThan(480)
                    ) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Norsk framtidig trygdetid",
                                    Language.Nynorsk to "Norsk framtidig trygdetid",
                                    Language.English to "Future insurance period in Norway"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(framtidigTTNorsk))
                            }
                        }
                    }
                }
                showIf(beregningsmetode.isOneOf(Beregningsmetode.NORDISK)) {
                    ifNotNull(
                        trygdetidsdetaljerGjeldende.tellerTTNordiskKonv,
                        trygdetidsdetaljerGjeldende.nevnerTTNordiskKonv
                    ) { tellerTTNordiskKonv, nevnerTTNordiskKonv ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                                    Language.Nynorsk to "Forholdstalet brukt i reduksjon av norsk framtidig trygdetid",
                                    Language.English to "Ratio applied in reduction of future Norwegian insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                                    Language.Nynorsk to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                                    Language.English to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format()
                                )
                            }
                        }
                    }

                    ifNotNull(trygdetidsdetaljerGjeldende.samletTTNordiskKonv) { samletTTNordiskKonv ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    Language.Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    Language.English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(samletTTNordiskKonv)) }
                        }
                    }
                }

                ifNotNull(trygdetidsdetaljerGjeldende.utenforEOSogNorden) {

                    val faktiskTTBilateral = it.faktiskTTBilateral
                    val nevnerProRata = it.nevnerProRata
                    val tellerProRata = it.tellerProRata

                    showIf(
                        beregningsmetode.isNotAnyOf(
                            Beregningsmetode.FOLKETRYGD,
                            Beregningsmetode.NORDISK,
                            Beregningsmetode.EOS
                        )
                    ) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i annet avtaleland",
                                    Language.Nynorsk to "Faktisk trygdetid i anna avtaleland",
                                    Language.English to "Actual insurance period(s) in another partner country"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTBilateral))
                            }
                        }
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                    Language.English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(nevnerProRata))
                            }
                        }
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                                    Language.Nynorsk to "Forholdstalet brukt i utrekning av uføretrygd",
                                    Language.English to "Ratio applied in calculation of insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerProRata.format() + " / " + nevnerProRata.format(),
                                    Language.Nynorsk to tellerProRata.format() + " / " + nevnerProRata.format(),
                                    Language.English to tellerProRata.format() + " / " + nevnerProRata.format()
                                )
                            }
                        }
                    }

                    //førstegangsbehandling bruker bor i utlandet
                    //TODO manglende felt år med inntekt
                    //TODO år med inntekt brukt i beregningen

                }

                ifNotNull(barnetilleggGjeldende) { barnetillegg ->
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Totalt antall barn du har barnetillegg for",
                                Language.Nynorsk to "Totalt antall barn du har barnetillegg for",
                                Language.English to "Total number of children for whom you receive child supplement"
                            )
                        }
                        val totaltAntallBarn = barnetillegg.foedselsdatoPaaBarnTilleggetGjelder.size()
                        cell {
                            textExpr(
                                Language.Bokmal to totaltAntallBarn.format(),
                                Language.Nynorsk to totaltAntallBarn.format(),
                                Language.English to totaltAntallBarn.format(),
                            )
                        }
                    }
                    ifNotNull(barnetillegg.saerkullsbarn) { saerkullsbarn ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Fribeløp for særkullsbarn",
                                    Language.Nynorsk to "Fribeløp for særkullsbarn",
                                    Language.English to "Exemption amount for children from a previous relationship"
                                )
                            }
                            cell {
                                includePhrase(KronerText(saerkullsbarn.fribeloep))
                            }
                        }
                    }
                    ifNotNull(barnetillegg.fellesbarn) { fellesbarn ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Fribeløp for fellesbarn",
                                    Language.Nynorsk to "Fribeløp for fellessbarn",
                                    Language.English to "Exemption amount for joint children"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.fribeloep))
                            }
                        }
                    }

                    val samletInntektBruktIAvkortningFelles =
                        barnetillegg.fellesbarn_safe.samletInntektBruktIAvkortning_safe.ifNull(Kroner(0))
                    val inntektBruktIAvkortningSaerkull =
                        barnetillegg.saerkullsbarn_safe.inntektBruktIAvkortning_safe.ifNull(Kroner(0))
                    showIf(samletInntektBruktIAvkortningFelles.greaterThan(0) or inntektBruktIAvkortningSaerkull.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                    Language.Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                                    Language.English to "Your income, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                showIf(samletInntektBruktIAvkortningFelles.greaterThan(0)) {
                                    includePhrase(KronerText(samletInntektBruktIAvkortningFelles))
                                }.orShow {
                                    includePhrase(KronerText(inntektBruktIAvkortningSaerkull))
                                }
                            }
                        }
                    }

                    ifNotNull(barnetillegg.fellesbarn) { fellesbarn ->
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg",
                                    Language.Nynorsk to "Samla inntekt til annen forelder som er brukt i fastsetjinga av barnetillegg",
                                    Language.English to "Income of the other parent, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                includePhrase(KronerText(fellesbarn.inntektAnnenForelder))
                            }
                        }
                    }

                    val inntektstakFelles = barnetillegg.fellesbarn_safe.inntektstak_safe.ifNull(Kroner(0))
                    val inntektstakSaerkull = barnetillegg.saerkullsbarn_safe.inntektstak_safe.ifNull(Kroner(0))
                    showIf(inntektstakSaerkull.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                                    Language.Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                                    Language.English to "Your income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(KronerText(inntektstakSaerkull))
                            }
                        }
                    }
                    ifNotNull(barnetillegg.fellesbarn) { fellesBarn ->
                        showIf(fellesBarn.beloepFratrukketAnnenForeldersInntekt.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Beløp som er trukket fra annen forelders inntekt (inntil 1G)",
                                        Language.Nynorsk to "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)",
                                        Language.English to "Amount deducted from the other parent's income (up to 1G)",
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(fellesBarn.beloepFratrukketAnnenForeldersInntekt))
                                }
                            }
                        }
                    }

                    showIf(inntektstakFelles.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt",
                                    Language.Nynorsk to "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt",
                                    Language.English to "Total combined income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(KronerText(inntektstakFelles))
                            }
                        }
                    }
                }
            }
        }
    }
}