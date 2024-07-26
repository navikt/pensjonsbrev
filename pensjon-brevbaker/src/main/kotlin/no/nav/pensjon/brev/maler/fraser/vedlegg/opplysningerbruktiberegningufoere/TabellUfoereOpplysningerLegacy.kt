package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.vedtaksdata_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.beregningsdata_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.kravhode_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsDataSelectors.beregningufore_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.beregningvirkningdatofom_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.beregningytelseskomp_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.reduksjonsgrunnlag_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.ReduksjonsgrunnlagSelectors.andelytelseavoifu_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.ReduksjonsgrunnlagSelectors.gradertoppjustertifu_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.ReduksjonsgrunnlagSelectors.prosentsatsoifufortak_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.uforetrygdordiner_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningsgrunnlagOrdinarSelectors.antallarinntektiavtaleland_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningsgrunnlagOrdinarSelectors.antallarover1g_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.UforetrygdOrdinerSelectors.ytelsesgrunnlag_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.YtelsesgrunnlagSelectors.beregningsgrunnlagordinar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.kravarsaktype_safe
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.YrkesskadeGjeldendeSelectors.yrkesskadegrad_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.borMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.erUngUfoer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.harMinsteytelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.inntektEtterUfoereGjeldendeBeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerLegacyDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.tableFormat
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class TabellUfoereOpplysningerLegacy(
    val tabellUfoereOpplysningerLegacy: Expression<OpplysningerBruktIBeregningenLegacyDto.TabellufoereOpplysningerLegacyDto>,
    val pe: Expression<PE>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val inntektsgrenseErUnderTak = tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektsgrenseAar
            .lessThan(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektstak)

        val beregningufore = pe.vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe
        val uforetrygdordiner = beregningufore.beregningytelseskomp_safe.uforetrygdordiner_safe
        paragraph {
            table(
                header = {
                    column(3) {
                        text(
                            Bokmal to "Opplysning",
                            Nynorsk to "Opplysning",
                            English to "Information",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                    column(columnSpan = 2,alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(
                            Bokmal to "Uføretidspunkt",
                            Nynorsk to "Uføretidspunkt",
                            English to "Date of disability"
                        )
                    }
                    cell {
                        val ufoeretidspunkt = tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.ufoeretidspunkt.format()
                        textExpr(
                            Bokmal to ufoeretidspunkt,
                            Nynorsk to ufoeretidspunkt,
                            English to ufoeretidspunkt
                        )
                    }
                }
                showIf(tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar.greaterThan(0) and not(tabellUfoereOpplysningerLegacy.harMinsteytelse)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Beregningsgrunnlag",
                                Nynorsk to "Utrekningsgrunnlag",
                                English to "Basis for calculation"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.beregningsgrunnlagBeloepAar))
                        }
                    }
                }
                ifNotNull(tabellUfoereOpplysningerLegacy.yrkesskadeGjeldende.beregningsgrunnlagBeloepAar_safe) { beloep ->
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
                                includePhrase(Felles.KronerText(beloep))
                            }
                        }
                    }
                }
                showIf(tabellUfoereOpplysningerLegacy.inntektFoerUfoereGjeldende.ifuInntekt.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Inntekt før uførhet",
                                Nynorsk to "Inntekt før uførleik",
                                English to "Income prior to disability"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.inntektFoerUfoereGjeldende.ifuInntekt))
                        }
                    }
                }
                ifNotNull(tabellUfoereOpplysningerLegacy.inntektEtterUfoereGjeldendeBeloep) { beloep ->
                    showIf(beloep.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Inntekt etter uførhet",
                                    Nynorsk to "Inntekt etter uførleik",
                                    English to "Income after disability"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(beloep))
                            }
                        }
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
                        val ufoeregrad = tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.ufoeregrad.format()
                        textExpr(
                            Bokmal to ufoeregrad + " %",
                            Nynorsk to ufoeregrad + " %",
                            English to ufoeregrad + " %"
                        )
                    }
                }
                showIf(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektsgrenseAar.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Inntektsgrense",
                                Nynorsk to "Inntektsgrense",
                                English to "Income cap"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektsgrenseAar))
                        }
                    }
                }
                showIf(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.forventetInntektAar.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Forventet inntekt",
                                Nynorsk to "Forventa inntekt",
                                English to "Expected income"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.forventetInntektAar))
                        }
                    }
                }
                showIf(inntektsgrenseErUnderTak and tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.kompensasjonsgrad.greaterThan(0.0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Kompensasjonsgrad",
                                Nynorsk to "Kompensasjonsgrad",
                                English to "Percentage of compensation"
                            )
                        }
                        cell {
                            val kompensasjonsgrad = tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.kompensasjonsgrad.format()
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
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektstak))
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
                            includePhrase(Felles.KronerText(tabellUfoereOpplysningerLegacy.inntektsAvkortingGjeldende.inntektsgrenseAar))
                        }
                    }
                }

                showIf(tabellUfoereOpplysningerLegacy.harMinsteytelse) {
                    row {
                        cell {
                            text(
                                Bokmal to "Sivilstatus lagt til grunn i beregningen",
                                Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                                English to "Marital status applied to calculation"
                            )
                        }
                        cell {
                            ifNotNull(tabellUfoereOpplysningerLegacy.borMedSivilstand){
                                textExpr(
                                    Bokmal to it.tableFormat(),
                                    Nynorsk to it.tableFormat(),
                                    English to it.tableFormat()
                                )
                            }.orShowIf(tabellUfoereOpplysningerLegacy.brukersSivilstand.isOneOf(Sivilstand.ENKE)) {
                                text(
                                    Bokmal to "Enke/Enkemann",
                                    Nynorsk to "Enkje/Enkjemann",
                                    English to "Widow/widower",
                                )
                            }.orShow {
                                text(
                                    Bokmal to "Enslig",
                                    Nynorsk to "Einsleg",
                                    English to "Single",
                                )
                            }
                        }
                    }

                    ifNotNull(tabellUfoereOpplysningerLegacy.borMedSivilstand){ borMedSivilstand->
                        showIf(borMedSivilstand.isOneOf(BorMedSivilstand.GIFT_LEVER_ADSKILT, BorMedSivilstand.PARTNER_LEVER_ADSKILT)) {
                            val erGift = borMedSivilstand.isOneOf(BorMedSivilstand.GIFT_LEVER_ADSKILT)
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to "Du eller ".expr()
                                                + ifElse(erGift, "ektefellen", "partneren") +
                                                " er registrert med annet bosted, eller er på institusjon",
                                        Nynorsk to "Du eller ".expr()
                                                + ifElse(erGift, "ektefellen", "partnaren") +
                                                " er registrert med annan bustad, eller er på institusjon",
                                        English to "You or your ".expr()
                                                + ifElse(erGift, "spouse", "partner") +
                                                " have been registered as having a different address, or as living in an institution",
                                    )
                                }
                                cell {
                                    text(
                                        Bokmal to "Ja",
                                        Nynorsk to "Ja",
                                        English to "Yes",
                                    )
                                }
                            }
                        }

                    }
                }

                showIf(tabellUfoereOpplysningerLegacy.erUngUfoer) {
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

                ifNotNull(tabellUfoereOpplysningerLegacy.yrkesskadeGjeldende) { yrkesskade ->
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
                                includePhrase(Felles.KronerText(yrkesskade.inntektVedSkadetidspunkt))
                            }
                        }
                    }
                }

                val beregningsmetode = tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.beregningsmetode

                showIf(tabellUfoereOpplysningerLegacy.beregnetUTPerManedGjeldende.brukerErFlyktning) {
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
                }
                showIf(beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Trygdetid (maksimalt 40 år)",
                                Nynorsk to "Trygdetid (maksimalt 40 år)",
                                English to "Insurance period (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Bokmal to anvendtTT + " år",
                                Nynorsk to anvendtTT + " år",
                                English to anvendtTT + " years"
                            )
                        }
                    }
                }

                showIf(beregningsmetode.isOneOf(Beregningsmetode.EOS, Beregningsmetode.NORDISK)) {
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
                            val anvendtTT = tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Bokmal to anvendtTT + " år",
                                Nynorsk to anvendtTT + " år",
                                English to anvendtTT + " years"
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
                                Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                                Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i utrekninga (maksimalt 40 år)",
                                English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.anvendtTT.format()
                            textExpr(
                                Bokmal to anvendtTT + " år",
                                Nynorsk to anvendtTT + " år",
                                English to anvendtTT + " years"
                            )
                        }
                    }
                }
                ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.faktiskTTNorge) {
                    showIf(beregningsmetode.isNotAnyOf(Beregningsmetode.FOLKETRYGD)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge",
                                    Nynorsk to "Faktisk trygdetid i Noreg",
                                    English to "Actual insurance period in Norway"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(it))
                            }
                        }
                    }
                }

                showIf(beregningsmetode.isOneOf(Beregningsmetode.EOS)) {
                    ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.faktiskTTEOS) { faktiskTTEOS ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                    English to "Actual insurance period(s) in other EEA countries"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTEOS))
                            }
                        }
                    }

                    ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.nevnerTTEOS) { nevnerTTEOS ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                    Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                                    English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(nevnerTTEOS)) }
                        }
                    }

                    ifNotNull(
                        tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.tellerTTEOS,
                        tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.nevnerTTEOS
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

                showIf(beregningsmetode.isOneOf(Beregningsmetode.NORDISK)) {
                    ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                    Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                                    English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(faktiskTTNordiskKonv)) }
                        }
                    }
                }
                ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.framtidigTTNorsk) { framtidigTTNorsk ->
                    showIf(
                        beregningsmetode.isOneOf(
                            Beregningsmetode.NORDISK,
                            Beregningsmetode.FOLKETRYGD
                        ) and framtidigTTNorsk.lessThan(480)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Norsk framtidig trygdetid",
                                    Nynorsk to "Norsk framtidig trygdetid",
                                    English to "Future insurance period in Norway"
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
                        tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.tellerTTNordiskKonv,
                        tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.nevnerTTNordiskKonv
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

                    ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.samletTTNordiskKonv) { samletTTNordiskKonv ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(samletTTNordiskKonv)) }
                        }
                    }
                }

                ifNotNull(tabellUfoereOpplysningerLegacy.trygdetidsdetaljerGjeldende.utenforEOSogNorden) {

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
                                    Bokmal to "Faktisk trygdetid i annet avtaleland",
                                    Nynorsk to "Faktisk trygdetid i anna avtaleland",
                                    English to "Actual insurance period(s) in another partner country"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTBilateral))
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
                                includePhrase(Felles.MaanederText(nevnerProRata))
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

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "f_bh_bo_utl" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksbrev_safe.vedtaksdata_safe.kravhode_safe.kravarsaktype_safe.ifNull("").equalTo("f_bh_bo_utl") and tabellUfoereOpplysningerLegacy.yrkesskadeGjeldende.yrkesskadegrad_safe.ifNull(0).lessThan(tabellUfoereOpplysningerLegacy.ufoeretrygdGjeldende.ufoeregrad)) {
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "År med inntekt over folketrygdens grunnbeløp før uføretidspunktet",
                                Nynorsk to "År med inntekt over grunnbeløpet i folketrygda før uføretidspunktet",
                                English to "Years of income exceeding the National Insurance basic amount at date of disability",
                            )
                        }
                        cell {
                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArOver1G
                            val antallAarOver1G =
                                beregningufore.beregningytelseskomp_safe.uforetrygdordiner_safe.ytelsesgrunnlag_safe.beregningsgrunnlagordinar_safe.antallarover1g_safe.ifNull(0)
                            textExpr(
                                Bokmal to antallAarOver1G.format() + " år",
                                Nynorsk to antallAarOver1G.format() + " år",
                                English to antallAarOver1G.format() + " years",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArInntektIAvtaleland <> 0) THEN      INCLUDE ENDIF
                showIf(uforetrygdordiner.notEqualTo(0)) {
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "År med inntekt i utlandet brukt i beregningen",
                                Nynorsk to "År med inntekt i utlandet",
                                English to "Years with income abroad",
                            )
                        }
                        cell {
                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArInntektIAvtaleland
                            val antallAarOverInntektIAvtaleland = uforetrygdordiner.ytelsesgrunnlag_safe.beregningsgrunnlagordinar_safe.antallarinntektiavtaleland_safe.ifNull(0)
                            textExpr(
                                Bokmal to antallAarOverInntektIAvtaleland.format() + " år",
                                Nynorsk to antallAarOverInntektIAvtaleland.format() + " år",
                                English to antallAarOverInntektIAvtaleland.format() + " years",
                            )
                        }
                    }
                }

                ifNotNull(tabellUfoereOpplysningerLegacy.barnetilleggGjeldende) { barnetillegg ->
                    row {
                        cell {
                            text(
                                Bokmal to "Totalt antall barn du har barnetillegg for",
                                Nynorsk to "Totalt antall barn du har barnetillegg for",
                                English to "Total number of children for whom you receive child supplement"
                            )
                        }
                        val totaltAntallBarn = barnetillegg.foedselsdatoPaaBarnTilleggetGjelder.size()
                        cell {
                            textExpr(
                                Bokmal to totaltAntallBarn.format(),
                                Nynorsk to totaltAntallBarn.format(),
                                English to totaltAntallBarn.format(),
                            )
                        }
                    }
                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU > 0 AND
                    // PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU > 95 AND
                    // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF

                    val reduksjonsgrunnlag = beregningufore.reduksjonsgrunnlag_safe
                    showIf(
                        reduksjonsgrunnlag.gradertoppjustertifu_safe.ifNull(Kroner(0)).greaterThan(0)
                            and reduksjonsgrunnlag.andelytelseavoifu_safe.ifNull(0.0).greaterThan(95.0)
                            and beregningufore.beregningvirkningdatofom_safe.ifNull(LocalDate.MIN).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                        row {
                            cell {
                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_ProsentsatsOIFUForTak
                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU
                                val prosentSats = reduksjonsgrunnlag.prosentsatsoifufortak_safe.ifNull(0)
                                textExpr(
                                    Bokmal to prosentSats.format() + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)",
                                    Nynorsk to prosentSats.format() + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)",
                                    English to prosentSats.format() + " % of income before disability, adjusted for changes in the basic amount",
                                )
                            }
                            cell {
                                Felles.KronerText(reduksjonsgrunnlag.gradertoppjustertifu_safe.ifNull(Kroner(0)))
                            }
                        }
                    }
                    ifNotNull(barnetillegg.saerkullsbarn) { saerkullsbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp for særkullsbarn",
                                    Nynorsk to "Fribeløp for særkullsbarn",
                                    English to "Exemption amount for children from a previous relationship"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(saerkullsbarn.fribeloep))
                            }
                        }
                    }
                    ifNotNull(barnetillegg.fellesbarn) { fellesbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Fribeløp for fellesbarn",
                                    Nynorsk to "Fribeløp for fellessbarn",
                                    English to "Exemption amount for joint children"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(fellesbarn.fribeloep))
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
                                    Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                    Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                                    English to "Your income, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                showIf(samletInntektBruktIAvkortningFelles.greaterThan(0)) {
                                    includePhrase(Felles.KronerText(samletInntektBruktIAvkortningFelles))
                                }.orShow {
                                    includePhrase(Felles.KronerText(inntektBruktIAvkortningSaerkull))
                                }
                            }
                        }
                    }

                    ifNotNull(barnetillegg.fellesbarn) { fellesbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg",
                                    Nynorsk to "Samla inntekt til annen forelder som er brukt i fastsetjinga av barnetillegg",
                                    English to "Income of the other parent, which is used to calculate child supplement"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(fellesbarn.inntektAnnenForelder))
                            }
                        }
                    }

                    val inntektstakFelles = barnetillegg.fellesbarn_safe.inntektstak_safe.ifNull(Kroner(0))
                    val inntektstakSaerkull = barnetillegg.saerkullsbarn_safe.inntektstak_safe.ifNull(Kroner(0))
                    showIf(inntektstakSaerkull.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                                    Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                                    English to "Your income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(inntektstakSaerkull))
                            }
                        }
                    }
                    ifNotNull(barnetillegg.fellesbarn) { fellesBarn ->
                        showIf(fellesBarn.beloepFratrukketAnnenForeldersInntekt.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Beløp som er trukket fra annen forelders inntekt (inntil 1G)",
                                        Nynorsk to "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)",
                                        English to "Amount deducted from the other parent's income (up to 1G)",
                                    )
                                }
                                cell {
                                    includePhrase(Felles.KronerText(fellesBarn.beloepFratrukketAnnenForeldersInntekt))
                                }
                            }
                        }
                    }

                    showIf(inntektstakFelles.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt",
                                    Nynorsk to "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt",
                                    English to "Total combined income which means that no child supplement is received"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(inntektstakFelles))
                            }
                        }
                    }
                }
            }
        }
    }
}