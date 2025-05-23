package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.forholdstall67Soeker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.forholdstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengArTeller
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengAre91
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengArf92
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengarNevner
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.skjermingsgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.uforegradVed67
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.nyOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.KravSelectors.erForstegangsbehandling
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TilleggspensjonVedVirkSelectors.pgaUngUfore_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.tellerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.nevnerProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.tellerProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.YrkesskadeDetaljerVedVirkSelectors.poengAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.YrkesskadeDetaljerVedVirkSelectors.poengAre91
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.YrkesskadeDetaljerVedVirkSelectors.poengArf92
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.YrkesskadeDetaljerVedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.YrkesskadeDetaljerVedVirkSelectors.yrkesskadeUforegrad
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.GarantipensjonSatsTypeText
import no.nav.pensjon.brev.maler.fraser.common.BroekText
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.delingstalletVed67Ar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.garantipensjonSatsPerAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.satsType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.TableHeaderScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class OpplysningerBruktIBeregningTabellKap19(
    val trygdetidsdetaljerKap19VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap19VedVirk>,
    val tilleggspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.TilleggspensjonVedVirk?>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonPerManed>,
    val beregningKap19VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap19VedVirk>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap20VedVirk?>,
    val yrkesskadeDetaljerVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.YrkesskadeDetaljerVedVirk?>,
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonVedVirk>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val harTilleggspensjon = tilleggspensjonVedVirk.notNull()
            table(opplysningerBruktIBeregningenHeader(beregnetPensjonPerManedVedVirk.virkDatoFom)) {
                showIf(
                    beregnetPensjonPerManedVedVirk.flyktningstatusErBrukt
                            and not(beregningKap19VedVirk.redusertTrygdetid)
                            and not(beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false))
                ) {
                    //tabellFlyktningstatus_002
                    flyktningstatusFraUDIrad()
                }

                val beregningsmetodeKap19 = trygdetidsdetaljerKap19VedVirk.beregningsmetode
                showIf(beregningsmetodeKap19.isOneOf(FOLKETRYGD, NORDISK)) {
                    //tabellTT_002
                    trygdetidAarRad(trygdetidsdetaljerKap19VedVirk.anvendtTT)

                    showIf(tilleggspensjonVedVirk.notNull()) {
                        //vedleggTabellKap19Sluttpoengtall_001
                        ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Sluttpoengtall",
                                        Nynorsk to "Sluttpoengtall",
                                        English to "Final pension point score",
                                    )
                                }
                                cell { eval(it.format()) }
                            }
                        }

                        //vedleggTabellKap19PoengAr_001
                        showIf(beregningsmetodeKap19.isOneOf(FOLKETRYGD)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall poengår",
                                        Nynorsk to "Talet på poengår",
                                        English to "Number of pension point earning years",
                                    )
                                }
                                cell { includePhrase(AntallAarText(beregningKap19VedVirk.poengAar)) }
                            }
                        }

                        //vedleggTabellKap19PoengArf92_001
                        ifNotNull(beregningKap19VedVirk.poengArf92) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 45",
                                        Nynorsk to "Talet på år med pensjonsprosent 45",
                                        English to "Number of years calculated with pension percentage 45",
                                    )
                                }
                                cell {
                                    includePhrase(AntallAarText(it))
                                }
                            }
                        }

                        ifNotNull(beregningKap19VedVirk.poengAre91) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 42",
                                        Nynorsk to "Talet på år med pensjonsprosent 42",
                                        English to "Number of years calculated with pension percentage 42",
                                    )
                                }
                                cell {
                                    includePhrase(AntallAarText(it))
                                }
                            }
                        }
                    }
                }.orShowIf(beregningsmetodeKap19.isOneOf(EOS)) {
                    //tabellTTNorgeEOS_001
                    row {
                        cell {
                            text(
                                Bokmal to "Samlet trygdetid i Norge og andre EØS-land",
                                Nynorsk to "Samla trygdetid i Noreg og andre EØS-land",
                                English to "Total national insurance coverage in Norway and other EEA countries",
                            )
                        }
                        cell { includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT)) }
                    }

                    //tabellFaktiskTTBrokNorgeEOS_001
                    ifNotNull(
                        trygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                        trygdetidsdetaljerKap19VedVirk.nevnerTTEOS
                    ) { teller, nevner ->
                        trygdetidEOSrad(teller, nevner)
                    }

                    //vedleggTabellKap19SluttpoengtallEOS_001
                    showIf(harTilleggspensjon) {
                        ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Sluttpoengtall (EØS)",
                                        Nynorsk to "Sluttpoengtal (EØS)",
                                        English to "Final pension point score (EEA)",
                                    )
                                }
                                cell { eval(it.format()) }
                            }
                        }
                    }

                    //vedleggTabellKap19PoengArf92EOS_001
                    ifNotNull(beregningKap19VedVirk.poengArf92) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Antall år med pensjonsprosent 45 (EØS)",
                                    Nynorsk to "Talet på år med pensjonsprosent 45 (EØS)",
                                    English to "Number of years calculated with pension percentage 45 (EEA)",
                                )
                            }
                            cell { includePhrase(AntallAarText(it)) }
                        }
                    }

                    //vedleggTabellKap19PoengAre91EOS_001
                    ifNotNull(beregningKap19VedVirk.poengAre91) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Antall år med pensjonsprosent 42 (EØS)",
                                    Nynorsk to "Talet på år med pensjonsprosent 42 (EØS)",
                                    English to "Number of years calculated with pension percentage 42 (EEA)",
                                )
                            }
                            cell { includePhrase(AntallAarText(it)) }
                        }
                    }

                    //tabellPoengArBrokNorgeEOS_001
                    ifNotNull(
                        beregningKap19VedVirk.poengArTeller,
                        beregningKap19VedVirk.poengarNevner
                    ) { teller, nevner ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land",
                                    Nynorsk to "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land",
                                    English to "The ratio between point earning years in Norway and total point earning years in all EEA countries",
                                )
                            }
                            cell { includePhrase(BroekText(teller, nevner)) }
                        }
                    }
                }.orShow {
                    row {
                        cell {
                            text(
                                Bokmal to "Samlet trygdetid i Norge og avtaleland",
                                Nynorsk to "Samla trygdetid i Noreg og avtaleland",
                                English to "Total period of national insurance coverage in Norway and countries with social security agreement",
                            )
                        }
                        cell {
                            includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT))
                        }
                    }

                    ifNotNull(
                        trygdetidsdetaljerKap19VedVirk.tellerProRata,
                        trygdetidsdetaljerKap19VedVirk.nevnerProRata
                    ) { teller, nevner ->
                        prorataBroekRad(teller, nevner)
                    }

                    showIf(harTilleggspensjon) {
                        //vedleggTabellKap19Sluttpoengtall Avtaleland_001
                        ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Sluttpoengtall (avtaleland)",
                                        Nynorsk to "Sluttpoengtal (avtaleland)",
                                        English to "Final pension point score (Norway and countries with social security agreement)",
                                    )
                                }
                                cell { eval(it.format()) }
                            }
                        }

                        //vedleggTabellKap19PoengArf92Avtaleland_001
                        ifNotNull(beregningKap19VedVirk.poengArf92) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 45 (Norge og avtaleland)",
                                        Nynorsk to "Talet på år med pensjonsprosent 45 (Noreg og avtaleland) ",
                                        English to "Number of years calculated with pension percentage 45 (Norway and countries with social security agreement)",
                                    )
                                }
                                cell { includePhrase(AntallAarText(it)) }
                            }
                        }

                        //vedleggTabellKap19PoengAre91Avtaleland_001
                        ifNotNull(beregningKap19VedVirk.poengAre91) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 42 (Norge og avtaleland)",
                                        Nynorsk to "Talet på år med pensjonsprosent 42 (Noreg og avtaleland)",
                                        English to "Number of years calculated with pension percentage 42 (Norway and countries with social security agreement)",
                                    )
                                }
                                cell { includePhrase(AntallAarText(it)) }
                            }
                        }

                        //tabellPoengArBrokNorgeAvtaleland_001
                        ifNotNull(
                            beregningKap19VedVirk.poengArTeller,
                            beregningKap19VedVirk.poengarNevner
                        ) { teller, nevner ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland",
                                        Nynorsk to "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland ",
                                        English to "Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement",
                                    )
                                }
                                cell { includePhrase(BroekText(teller, nevner)) }
                            }
                        }
                    }
                }

                //vedleggTabellKap19Forholdstall_001
                showIf(beregningKap19VedVirk.forholdstallLevealder.greaterThan(0.0)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Forholdstall ved levealdersjustering",
                                Nynorsk to "Forholdstal ved levealdersjustering",
                                English to "Ratio for life expectancy adjustment",
                            )
                        }
                        cell { eval(beregningKap19VedVirk.forholdstallLevealder.format(scale = 3)) }
                    }
                }

                //tabellUngUfor_002
                showIf(tilleggspensjonVedVirk.pgaUngUfore_safe.ifNull(false)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Ung ufør",
                                Nynorsk to "Ung ufør",
                                English to "Young disabled person",
                            )
                        }
                        cell { includePhrase(Ja) }
                    }
                }

                //tabellYrkesskadeUforegrad_001
                ifNotNull(yrkesskadeDetaljerVedVirk) {
                    row {
                        cell {
                            text(
                                Bokmal to "Yrkesskade uføregrad",
                                Nynorsk to "Yrkesskade uføregrad",
                                English to "Occupational injury - degree of disability",
                            )
                        }
                        cell { eval(it.yrkesskadeUforegrad.format() + " %") }
                    }

                    //tabellYrkesskadeSluttpoengtall_001
                    row {
                        cell {
                            text(
                                Bokmal to "Sluttpoengtall ved yrkesskade",
                                Nynorsk to "Sluttpoengtal ved yrkesskade",
                                English to "Final pension point score on occupational injury",
                            )
                        }
                        cell { eval(it.sluttpoengtall.format()) }
                    }

                    //tabellYrkesskadePoengAr_001
                    row {
                        cell {
                            text(
                                Bokmal to "Antall poengår benyttet ved yrkesskadeberegningen",
                                Nynorsk to "Talet på poengår benyttet ved yrkesskadeberekning",
                                English to "Number of pension point earning years used in the calculation of occupational injury",
                            )
                        }
                        cell { includePhrase(AntallAarText(it.poengAr)) }
                    }

                    //tabellPoengArf92Yrkesskade_001
                    row {
                        cell {
                            text(
                                Bokmal to "Antall år med pensjonsprosent 45 benyttet ved yrkesskadeberegning",
                                Nynorsk to "Talet på år med pensjonsprosent 45 brukt ved yrkesskadeberekning",
                                English to "Number of years with pension percentage 45 used in the calculation of occupational injury",
                            )
                        }
                        cell { includePhrase(AntallAarText(it.poengArf92)) }
                    }

                    //tabellPoengAre91Yrkesskade_001
                    row {
                        cell {
                            text(
                                Bokmal to "Antall år med pensjonsprosent 42 benyttet ved yrkesskadeberegning",
                                Nynorsk to "Talet på år med pensjonsprosent 42 brukt ved yrkesskadeberekning",
                                English to "Number of years with pension percentage 42 used in the calculation of occupational injury",
                            )
                        }
                        cell { includePhrase(AntallAarText(it.poengAre91)) }
                    }
                }

                showIf(alderspensjonVedVirk.skjermingstilleggInnvilget) {
                    //tabellSkjermingstilleggUfgVed67_001
                    showIf(beregningKap19VedVirk.uforegradVed67.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Uføregrad v/67 år brukt ved beregning av skjermingstillegg",
                                    Nynorsk to "Uføregrad v/67 år brukt ved av berekning av skjermingstillegg",
                                    English to "Degree of disability at the age of 67 used for calculating the supplement for the disabled",
                                )
                            }
                            cell { eval(beregningKap19VedVirk.uforegradVed67.format() + " %") }
                        }
                    }

                    //tabellSkjermingstilleggSkjermingsgrad_001
                    showIf(beregningKap19VedVirk.skjermingsgrad.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Prosentsats for fastsetting av skjermingstillegg til uføre",
                                    Nynorsk to "Prosentsats for fastsetting av skjermingstillegg til uføre",
                                    English to "Percentage rate for determining the supplement for the disabled",
                                )
                            }
                            cell { eval(beregningKap19VedVirk.skjermingsgrad.format() + " %") }
                        }
                    }

                    //tabellSkjermingstilleggForholdstall_001
                    showIf(beregningKap19VedVirk.forholdstall67Soeker.greaterThan(0.0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstallet brukt ved beregningen av skjermingstillegg til uføre",
                                    Nynorsk to "Forholdstal brukt ved berekning av skjermingstillegg",
                                    English to "Ratio for life expectancy adjustment used for calculating the supplement for the disabled",
                                )
                            }
                            cell { eval(beregningKap19VedVirk.forholdstall67Soeker.format(scale = 3)) }
                        }
                    }
                }
            }
        }
    }
}

data class OpplysningerBruktIBeregningTabellKap20(
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonPerManed>,
    val beregningKap19VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap19VedVirk>,
    val trygdetidsdetaljerKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap20VedVirk?>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap20VedVirk?>,
    val krav: Expression<OpplysningerBruktIBeregningenAlderDto.Krav>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "For den delen av pensjonen din som er beregnet etter nye regler (kapittel 20) har vi brukt disse opplysningene i beregningen vår:",
                Nynorsk to "For den delen av pensjonen din som er berekna etter nye reglar (kapittel 20), har vi brukt desse opplysningane i berekninga vår:",
                English to "We have used the following information to calculate the part of your pension that comes under the new provisions (Chapter 20):",
            )

            table(opplysningerBruktIBeregningenHeader(beregnetPensjonPerManedVedVirk.virkDatoFom)) {
                showIf(
                    beregnetPensjonPerManedVedVirk.flyktningstatusErBrukt
                            and not(beregningKap19VedVirk.redusertTrygdetid)
                            and not(beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false))
                ) {
                    //tabellFlyktningstatus_002
                    flyktningstatusFraUDIrad()
                }
                ifNotNull(trygdetidsdetaljerKap20VedVirk) { trygdetidsdetaljer ->
                    //vedleggTabellKap20Trygdetid_001
                    row {
                        cell {
                            text(
                                Bokmal to "Trygdetid etter kapittel 20",
                                Nynorsk to "Trygdetid etter kapittel 20",
                                English to "National insurance coverage pursuant to Chapter 20",
                            )
                        }
                        cell { includePhrase(AntallAarText(trygdetidsdetaljer.anvendtTT)) }
                    }

                    ifNotNull(trygdetidsdetaljer.beregningsmetode) { beregningsmetode ->

                        //tabellFaktiskTTBrokNorgeEOS_001
                        ifNotNull(
                            trygdetidsdetaljer.tellerTTEOS,
                            trygdetidsdetaljer.nevnerTTEOS
                        ) { tellerTTEOS, nevnerTTEOS ->
                            showIf(beregningsmetode.isOneOf(EOS)) {
                                trygdetidEOSrad(tellerTTEOS, nevnerTTEOS)
                            }
                        }

                        //tabellTTBrokNorgeAvtaleland_001
                        ifNotNull(
                            trygdetidsdetaljer.tellerProRata_safe,
                            trygdetidsdetaljer.nevnerProRata_safe
                        ) { tellerProRata, nevnerProRata ->
                            showIf(beregningsmetode.isNotAnyOf(EOS, NORDISK, FOLKETRYGD)) {
                                prorataBroekRad(tellerProRata, nevnerProRata)
                            }
                        }
                    }
                }

                ifNotNull(beregningKap20VedVirk) { beregningKap20VedVirk ->
                    //tabellBeholdningForForsteUttak_001
                    showIf(krav.erForstegangsbehandling) {
                        ifNotNull(beregningKap20VedVirk.beholdningForForsteUttak_safe) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Pensjonsbeholdning før førstegangsuttak",
                                        Nynorsk to "Pensjonsbehaldning før førstegangsuttak",
                                        English to "Accumulated pension capital before initial withdrawal",
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }

                    //vedleggTabellKap20NyOpptjening_001
                    ifNotNull(beregningKap20VedVirk.nyOpptjening_safe) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Ny opptjening",
                                    Nynorsk to "Ny opptening",
                                    English to "New accumulated pension capital",
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    showIf(beregningKap20VedVirk.delingstallLevealder.greaterThan(0.0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Delingstall ved levealdersjustering",
                                    Nynorsk to "Delingstal ved levealdersjustering",
                                    English to "Ratio for life expectancy adjustment",
                                )
                            }
                            cell { eval(beregningKap20VedVirk.delingstallLevealder.format()) }
                        }
                    }
                }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.trygdetidEOSrad(
    tellerTTEOS: Expression<Int>,
    nevnerTTEOS: Expression<Int>
) {
    row {
        cell {
            text(
                Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
                Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
                English to "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
            )
        }
        cell { includePhrase(BroekText(tellerTTEOS, nevnerTTEOS)) }
    }
}

data class OpplysningerBruktIBeregningTabellAP2025(
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.AlderspensjonVedVirk>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.BeregningKap20VedVirk>,
    val vilkarsVedtak: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.VilkaarsVedtak>,
    val trygdetidsdetaljerKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.TrygdetidsdetaljerKap20VedVirk>,
    val garantipensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.GarantipensjonVedVirk?>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.BeregnetPensjonPerManedVedVirk>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(opplysningerBruktIBeregningenHeader(beregnetPensjonPerManedVedVirk.virkDatoFom)) {
                row {
                    //tabellBeholdningForForsteUttak_002
                    cell {
                        text(
                            Bokmal to "Pensjonsbeholdning ved uttak",
                            Nynorsk to "Pensjonsbehaldning ved uttak",
                            English to "Accumulated pension capital before initial withdrawal",
                        )
                    }
                    cell { includePhrase(KronerText(beregningKap20VedVirk.beholdningForForsteUttak)) }
                }

                //vedleggTabellKap20Delingstall_002
                row {
                    cell {
                        text(
                            Bokmal to "Delingstall ved uttak",
                            Nynorsk to "Delingstal ved uttak",
                            English to "Life expectancy adjustment divisor at withdrawal",
                        )
                    }
                    cell { eval(beregningKap20VedVirk.delingstallLevealder.format()) }
                }

                //vedleggTabellKap20Trygdetid_001
                showIf(not(vilkarsVedtak.avslattGarantipensjon)) {
                    trygdetidAarRad(trygdetidsdetaljerKap20VedVirk.anvendtTT)
                }

                //vedleggTabellKap20SatsGarP_001

                ifNotNull(garantipensjonVedVirk) { garantipensjonVedVirk ->
                    showIf(alderspensjonVedVirk.garantipensjonInnvilget and
                            garantipensjonVedVirk.nettoUtbetaltPerManed.greaterThan(0)
                    ) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Sats for garantipensjon (",
                                    Nynorsk to "Sats for garantipensjon (",
                                    English to "Guaranteed pension rate (",
                                )
                                includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                                text(
                                    Bokmal to ")",
                                    Nynorsk to ")",
                                    English to ")",
                                )

                            }
                            cell {
                                includePhrase(KronerText(garantipensjonVedVirk.garantipensjonSatsPerAr))
                            }
                        }

                        row {
                            cell { includePhrase(DelingstallVed67Aar) }
                            cell { eval(garantipensjonVedVirk.delingstalletVed67Ar.format()) }
                        }
                    }
                }
            }
        }
    }

}

fun opplysningerBruktIBeregningenHeader(beregningVirkDatoFom: Expression<LocalDate>): TableHeaderScope<LangBokmalNynorskEnglish, Unit>.() -> Unit =
    {
        //vedleggBeregnTabellOverskrift_001
        column(columnSpan = 4) {
            textExpr(
                Bokmal to "Opplysninger brukt i beregningen per ".expr() + beregningVirkDatoFom.format(),
                Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + beregningVirkDatoFom.format(),
                English to "Information used to calculate as of ".expr() + beregningVirkDatoFom.format(),
            )
        }
        column(alignment = RIGHT) { }
    }


private fun TableScope<LangBokmalNynorskEnglish, Unit>.flyktningstatusFraUDIrad() {
    row {
        cell {
            text(
                Bokmal to "Du er innvilget flyktningstatus fra UDI",
                Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                English to "You are registered with the status of a refugee granted by the UDI",
            )
        }
        cell { includePhrase(Ja) }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.trygdetidAarRad(trygdetid: Expression<Int>) {
    row {
        cell {
            text(
                Bokmal to "Trygdetid",
                Nynorsk to "Trygdetid",
                English to "National insurance coverage",
            )
        }
        cell { includePhrase(AntallAarText(trygdetid)) }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.prorataBroekRad(
    teller: Expression<Int>,
    nevner: Expression<Int>
) {
    row {
        cell {
            text(
                Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
                Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
                English to "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
            )
        }
        cell { includePhrase(BroekText(teller, nevner)) }
    }
}

object DelingstallVed67Aar : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Delingstall ved 67 år",
            Nynorsk to "Delingstal ved 67 år",
            English to "Life expectancy adjustment divisor at 67 years"
        )
    }

}