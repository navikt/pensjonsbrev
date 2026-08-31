package no.nav.pensjon.brev.alder.maler.endring

import no.nav.pensjon.brev.alder.maler.felles.AntallAarText
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.opplysningerBruktIBeregningenHeader
import no.nav.pensjon.brev.alder.model.endring.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.alder.model.endring.selectors.opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.beregnetPensjonPerManedVedVirk.*
import no.nav.pensjon.brev.alder.model.endring.selectors.opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.beregningKap20VedVirk.*
import no.nav.pensjon.brev.alder.model.endring.selectors.opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.trygdetidsdetaljerKap20VedVirk.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


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
                row {
                    cell {
                        text(
                            bokmal { +"Ny opptjening" },
                            nynorsk { +"Ny opptening" },
                            english { +"New accumulated pension capital" },
                        )
                    }
                    cell { includePhrase(KronerText(beregningKap20VedVirk.nyOpptjening.ifNull(Kroner(0)))) }
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

                row {
                    cell { includePhrase(Vedtak.TrygdetidText) }
                    cell { includePhrase(AntallAarText(trygdetidsdetaljerKap20VedVirk.anvendtTT)) }
                }
            }
        }
    }
}

