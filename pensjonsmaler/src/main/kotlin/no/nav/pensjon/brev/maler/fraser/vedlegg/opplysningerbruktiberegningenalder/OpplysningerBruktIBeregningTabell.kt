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
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
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
                                        bokmal { +"Sluttpoengtall" },
                                        nynorsk { +"Sluttpoengtall" },
                                        english { +"Final pension point score" },
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
                                        bokmal { +"Antall poengår" },
                                        nynorsk { +"Talet på poengår" },
                                        english { +"Number of pension point earning years" },
                                    )
                                }
                                cell { includePhrase(AntallAarText(beregningKap19VedVirk.poengAar)) }
                            }
                        }

                        //vedleggTabellKap19PoengArf92_001
                        ifNotNull(beregningKap19VedVirk.poengArf92) { antallAar ->
                            showIf(antallAar.greaterThan(0)) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Antall år med pensjonsprosent 45" },
                                            nynorsk { +"Talet på år med pensjonsprosent 45" },
                                            english { +"Number of years calculated with pension percentage 45" },
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(antallAar))
                                    }
                                }
                            }
                        }

                        ifNotNull(beregningKap19VedVirk.poengAre91) { antallAar ->
                            showIf(antallAar.greaterThan(0)) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Antall år med pensjonsprosent 42" },
                                            nynorsk { +"Talet på år med pensjonsprosent 42" },
                                            english { +"Number of years calculated with pension percentage 42" },
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(antallAar))
                                    }
                                }
                            }
                        }
                    }.orShowIf(beregningsmetodeKap19.isOneOf(EOS)) {
                        //tabellTTNorgeEOS_001
                        row {
                            cell {
                                text(
                                    bokmal { +"Samlet trygdetid i Norge og andre EØS-land" },
                                    nynorsk { +"Samla trygdetid i Noreg og andre EØS-land" },
                                    english { +"Total national insurance coverage in Norway and other EEA countries" },
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
                                            bokmal { +"Sluttpoengtall (EØS)" },
                                            nynorsk { +"Sluttpoengtal (EØS)" },
                                            english { +"Final pension point score (EEA)" },
                                        )
                                    }
                                    cell { eval(it.format()) }
                                }
                            }
                        }

                        //vedleggTabellKap19PoengArf92EOS_001
                        ifNotNull(beregningKap19VedVirk.poengArf92) { antallAar ->
                            showIf(antallAar.greaterThan(0)) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Antall år med pensjonsprosent 45 (EØS)" },
                                            nynorsk { +"Talet på år med pensjonsprosent 45 (EØS)" },
                                            english { +"Number of years calculated with pension percentage 45 (EEA)" },
                                        )
                                    }
                                    cell { includePhrase(AntallAarText(antallAar)) }
                                }
                            }
                        }

                        //vedleggTabellKap19PoengAre91EOS_001
                        ifNotNull(beregningKap19VedVirk.poengAre91) { antallAar ->
                            showIf(antallAar.greaterThan(0)) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Antall år med pensjonsprosent 42 (EØS)" },
                                            nynorsk { +"Talet på år med pensjonsprosent 42 (EØS)" },
                                            english { +"Number of years calculated with pension percentage 42 (EEA)" },
                                        )
                                    }
                                    cell { includePhrase(AntallAarText(antallAar)) }
                                }
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
                                        bokmal { +"Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land" },
                                        nynorsk { +"Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land" },
                                        english { +"The ratio between point earning years in Norway and total point earning years in all EEA countries" },
                                    )
                                }
                                cell { includePhrase(BroekText(teller, nevner)) }
                            }
                        }
                    }.orShow {
                        row {
                            cell {
                                text(
                                    bokmal { +"Samlet trygdetid i Norge og avtaleland" },
                                    nynorsk { +"Samla trygdetid i Noreg og avtaleland" },
                                    english { +"Total period of national insurance coverage in Norway and countries with social security agreement" },
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
                                            bokmal { +"Sluttpoengtall (avtaleland)" },
                                            nynorsk { +"Sluttpoengtal (avtaleland)" },
                                            english { +"Final pension point score (Norway and countries with social security agreement)" },
                                        )
                                    }
                                    cell { eval(it.format()) }
                                }
                            }

                            //vedleggTabellKap19PoengArf92Avtaleland_001
                            ifNotNull(beregningKap19VedVirk.poengArf92) { antallAar ->
                                showIf(antallAar.greaterThan(0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { +"Antall år med pensjonsprosent 45 (Norge og avtaleland)" },
                                                nynorsk { +"Talet på år med pensjonsprosent 45 (Noreg og avtaleland) " },
                                                english { +"Number of years calculated with pension percentage 45 (Norway and countries with social security agreement)" },
                                            )
                                        }
                                        cell { includePhrase(AntallAarText(antallAar)) }
                                    }
                                }
                            }

                            //vedleggTabellKap19PoengAre91Avtaleland_001
                            ifNotNull(beregningKap19VedVirk.poengAre91) { antallAar ->
                                showIf(antallAar.greaterThan(0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { +"Antall år med pensjonsprosent 42 (Norge og avtaleland)" },
                                                nynorsk { +"Talet på år med pensjonsprosent 42 (Noreg og avtaleland)" },
                                                english { +"Number of years calculated with pension percentage 42 (Norway and countries with social security agreement)" },
                                            )
                                        }
                                        cell { includePhrase(AntallAarText(antallAar)) }
                                    }
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
                                            bokmal { +"Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland" },
                                            nynorsk { +"Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland " },
                                            english { +"Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement" },
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
                                    bokmal { +"Forholdstall ved levealdersjustering" },
                                    nynorsk { +"Forholdstal ved levealdersjustering" },
                                    english { +"Ratio for life expectancy adjustment" },
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
                                    bokmal { +"Ung ufør" },
                                    nynorsk { +"Ung ufør" },
                                    english { +"Young disabled person" },
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
                                    bokmal { +"Yrkesskade uføregrad" },
                                    nynorsk { +"Yrkesskade uføregrad" },
                                    english { +"Occupational injury - degree of disability" },
                                )
                            }
                            cell { eval(it.yrkesskadeUforegrad.format() + " %") }
                        }

                        //tabellYrkesskadeSluttpoengtall_001
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall ved yrkesskade" },
                                    nynorsk { +"Sluttpoengtal ved yrkesskade" },
                                    english { +"Final pension point score on occupational injury" },
                                )
                            }
                            cell { eval(it.sluttpoengtall.format()) }
                        }

                        //tabellYrkesskadePoengAr_001
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår benyttet ved yrkesskadeberegningen" },
                                    nynorsk { +"Talet på poengår benyttet ved yrkesskadeberekning" },
                                    english { +"Number of pension point earning years used in the calculation of occupational injury" },
                                )
                            }
                            cell { includePhrase(AntallAarText(it.poengAr)) }
                        }

                        //tabellPoengArf92Yrkesskade_001
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45 benyttet ved yrkesskadeberegning" },
                                    nynorsk { +"Talet på år med pensjonsprosent 45 brukt ved yrkesskadeberekning" },
                                    english { +"Number of years with pension percentage 45 used in the calculation of occupational injury" },
                                )
                            }
                            cell { includePhrase(AntallAarText(it.poengArf92)) }
                        }

                        //tabellPoengAre91Yrkesskade_001
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42 benyttet ved yrkesskadeberegning" },
                                    nynorsk { +"Talet på år med pensjonsprosent 42 brukt ved yrkesskadeberekning" },
                                    english { +"Number of years with pension percentage 42 used in the calculation of occupational injury" },
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
                                        bokmal { +"Uføregrad v/67 år brukt ved beregning av skjermingstillegg" },
                                        nynorsk { +"Uføregrad v/67 år brukt ved av berekning av skjermingstillegg" },
                                        english { +"Degree of disability at the age of 67 used for calculating the supplement for the disabled" },
                                    )
                                }
                                cell { eval(beregningKap19VedVirk.uforegradVed67.format() + " %") }
                            }
                        }

                        //tabellSkjermingstilleggSkjermingsgrad_001
                        showIf(beregningKap19VedVirk.skjermingsgrad.greaterThan(0.0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Prosentsats for fastsetting av skjermingstillegg til uføre" },
                                        nynorsk { +"Prosentsats for fastsetting av skjermingstillegg til uføre" },
                                        english { +"Percentage rate for determining the supplement for the disabled" },
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
                                        bokmal { +"Forholdstallet brukt ved beregningen av skjermingstillegg til uføre" },
                                        nynorsk { +"Forholdstal brukt ved berekning av skjermingstillegg" },
                                        english { +"Ratio for life expectancy adjustment used for calculating the supplement for the disabled" },
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
                bokmal { +"For den delen av pensjonen din som er beregnet etter nye regler (kapittel 20) har vi brukt disse opplysningene i beregningen vår:" },
                nynorsk { +"For den delen av pensjonen din som er berekna etter nye reglar (kapittel 20), har vi brukt desse opplysningane i berekninga vår:" },
                english { +"We have used the following information to calculate the part of your pension that comes under the new provisions (Chapter 20):" },
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
                                bokmal { +"Trygdetid" },
                                nynorsk { +"Trygdetid" },
                                english { +"National insurance coverage" },
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
                                        bokmal { +"Pensjonsbeholdning før førstegangsuttak" },
                                        nynorsk { +"Pensjonsbehaldning før førstegangsuttak" },
                                        english { +"Accumulated pension capital before initial withdrawal" },
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
                                    bokmal { +"Ny opptjening" },
                                    nynorsk { +"Ny opptening" },
                                    english { +"New accumulated pension capital" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    showIf(beregningKap20VedVirk.delingstallLevealder.greaterThan(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Delingstall ved levealdersjustering" },
                                    nynorsk { +"Delingstal ved levealdersjustering" },
                                    english { +"Ratio for life expectancy adjustment" },
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
                bokmal { +"Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land" },
                nynorsk { +"Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land" },
                english { +"The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries" },
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
                            bokmal { +"Pensjonsbeholdning ved uttak" },
                            nynorsk { +"Pensjonsbehaldning ved uttak" },
                            english { +"Accumulated pension capital before initial withdrawal" },
                        )
                    }
                    cell { includePhrase(KronerText(beregningKap20VedVirk.beholdningForForsteUttak)) }
                }

                //vedleggTabellKap20Delingstall_002
                row {
                    cell {
                        text(
                            bokmal { +"Delingstall ved uttak" },
                            nynorsk { +"Delingstal ved uttak" },
                            english { +"Life expectancy adjustment divisor at withdrawal" },
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
                    showIf(
                        alderspensjonVedVirk.garantipensjonInnvilget and
                                garantipensjonVedVirk.nettoUtbetaltPerManed.greaterThan(0)
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sats for garantipensjon (" },
                                    nynorsk { +"Sats for garantipensjon (" },
                                    english { +"Guaranteed pension rate (" },
                                )
                                includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                                text(
                                    bokmal { +")" },
                                    nynorsk { +")" },
                                    english { +")" },
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
            text(
                bokmal { +"Opplysninger brukt i beregningen per " + beregningVirkDatoFom.format() },
                nynorsk { +"Opplysningar brukte i berekninga frå " + beregningVirkDatoFom.format() },
                english { +"Information used to calculate as of " + beregningVirkDatoFom.format() },
            )
        }
        column(alignment = RIGHT) { }
    }


private fun TableScope<LangBokmalNynorskEnglish, Unit>.flyktningstatusFraUDIrad() {
    row {
        cell {
            text(
                bokmal { +"Du er innvilget flyktningstatus fra UDI" },
                nynorsk { +"Du er innvilga flyktningstatus frå UDI" },
                english { +"You are registered with the status of a refugee granted by the UDI" },
            )
        }
        cell { includePhrase(Ja) }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.trygdetidAarRad(trygdetid: Expression<Int>) {
    row {
        cell { includePhrase(Vedtak.TrygdetidText) }
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
                bokmal { +"Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland" },
                nynorsk { +"Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland" },
                english { +"Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement" },
            )
        }
        cell { includePhrase(BroekText(teller, nevner)) }
    }
}

object DelingstallVed67Aar : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Delingstall ved 67 år" },
            nynorsk { +"Delingstal ved 67 år" },
            english { +"Life expectancy adjustment divisor at 67 years" }
        )
    }
}
