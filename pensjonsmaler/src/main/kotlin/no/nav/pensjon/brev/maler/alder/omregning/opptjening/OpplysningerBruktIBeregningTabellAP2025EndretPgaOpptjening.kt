package no.nav.pensjon.brev.maler.alder.omregning.opptjening

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.BeregningKap20VedVirkSelectors.nyOpptjening
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableHeaderScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate


data class OpplysningerBruktIBeregningTabellAP2025EndretPgaOpptjening(
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.AlderspensjonVedVirk>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.BeregningKap20VedVirk>,
    val vilkarsVedtak: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.VilkaarsVedtak>,
    val trygdetidsdetaljerKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.TrygdetidsdetaljerKap20VedVirk>,
    val garantipensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.GarantipensjonVedVirk?>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.BeregnetPensjonPerManedVedVirk>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(opplysningerBruktIBeregningenHeader(beregnetPensjonPerManedVedVirk.virkDatoFom)) {

                ifNotNull(beregningKap20VedVirk.nyOpptjening) { opptjening ->
                    row {
                        cell {
                            text(
                                bokmal { +"Ny opptjening" },
                                nynorsk { +"Ny opptening" },
                                english { +"New accumulated pension capital" },
                            )
                        }
                        cell { includePhrase(KronerText(opptjening)) }
                    }
                }

                showIf(beregningKap20VedVirk.delingstallLevealder.greaterThan(0.0)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Delingstall" },
                                nynorsk { +"Delingstall" },
                                english { +"Life expectancy adjustment divisor" },
                            )
                        }
                        cell { eval(beregningKap20VedVirk.delingstallLevealder.format()) }
                    }
                }

                showIf(vilkarsVedtak.avslattGarantipensjon.not()) {
                    row {
                        cell { includePhrase(Vedtak.TrygdetidText) }
                        cell { includePhrase(AntallAarText(trygdetidsdetaljerKap20VedVirk.anvendtTT)) }
                    }
                }
            }
        }
    }
}

fun opplysningerBruktIBeregningenHeader(beregningVirkDatoFom: Expression<LocalDate>): TableHeaderScope<LangBokmalNynorskEnglish, Unit>.() -> Unit =
    {
        column(columnSpan = 4) {
            text(
                bokmal { +"Opplysninger brukt i beregningen per " + beregningVirkDatoFom.format() },
                nynorsk { +"Opplysningar brukte i berekninga frå " + beregningVirkDatoFom.format() },
                english { +"Information used to calculate as of " + beregningVirkDatoFom.format() },
            )
        }
        column(alignment = RIGHT) { }
    }
