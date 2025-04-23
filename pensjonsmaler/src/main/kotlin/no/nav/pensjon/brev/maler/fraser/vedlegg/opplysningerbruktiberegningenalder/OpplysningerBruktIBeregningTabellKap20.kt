package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.nyOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.KravSelectors.erForstegangsbehandling
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.nevnerProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.tellerProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

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

            table(
                header = {
                    //vedleggBeregnTabellOverskrift_001
                    column(columnSpan = 4) {
                        textExpr(
                            Bokmal to "Opplysninger brukt i beregningen per ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                            Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                            English to "Information used to calculate as of ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                        )
                    }
                    column(alignment = RIGHT) { }
                }
            ) {
                showIf(
                    beregnetPensjonPerManedVedVirk.flyktningstatusErBrukt
                            and not(beregningKap19VedVirk.redusertTrygdetid)
                            and not(beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false))
                ) {
                    //tabellFlyktningstatus_002
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
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
                                            Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
                                            English to "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
                                        )
                                    }
                                    cell { eval(tellerTTEOS.format() + "/" + nevnerTTEOS.format()) }
                                }
                            }
                        }

                        //tabellTTBrokNorgeAvtaleland_001
                        ifNotNull(
                            trygdetidsdetaljer.tellerProRata_safe,
                            trygdetidsdetaljer.nevnerProRata_safe
                        ) { tellerProRata, nevnerProRata ->
                            showIf(beregningsmetode.isNotAnyOf(EOS, NORDISK, FOLKETRYGD)) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
                                            Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
                                            English to "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
                                        )
                                    }
                                    cell { eval(tellerProRata.format() + "/" + nevnerProRata.format()) }
                                }
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
