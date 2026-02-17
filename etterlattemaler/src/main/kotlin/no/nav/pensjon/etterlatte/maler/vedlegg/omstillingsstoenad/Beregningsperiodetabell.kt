package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.institusjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjonType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.ytelseFoerAvkorting
import no.nav.pensjon.etterlatte.maler.SanksjonType
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

data class Beregningsperiodetabell(
    val beregningsperioder: Expression<List<OmstillingsstoenadBeregningsperiode>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(1) {
                        text(
                            bokmal { +"Periode" },
                            nynorsk { +"Periode" },
                            english { +"Period" },
                        )
                    }
                    column(1) {
                        text(
                            bokmal { +"Stønad før reduksjon for inntekt" },
                            nynorsk { +"Stønad før reduksjon for inntekt" },
                            english { +"Allowance paid before income reduction" },
                        )
                    }
                    column(1) {
                        text(
                            bokmal { +"Inntekten din" },
                            nynorsk { +"Inntekta di" },
                            english { +"Your income" },
                        )
                    }
                    column(1) {
                        text(
                            bokmal { +"Utbetaling per måned" },
                            nynorsk { +"Utbetaling per månad" },
                            english { +"Payout per month" },
                        )
                    }
                }
            ) {
                forEach(beregningsperioder) { periode ->
                    row {
                        cell { includePhrase(PeriodeITabell(periode.datoFOM, periode.datoTOM)) }
                        cell { includePhrase(KronerText(periode.ytelseFoerAvkorting)) }
                        cell { includePhrase(KronerText(periode.inntekt)) }
                        cell {
                            includePhrase(KronerText(periode.utbetaltBeloep))
                            showIf(periode.sanksjon) {
                                text(
                                    bokmal { +" - " + ifElse(periode.sanksjonType.equalTo(SanksjonType.IKKE_INNVILGET_PERIODE),
                                        "stans", "sanksjon")},
                                    nynorsk { +" - " + ifElse(periode.sanksjonType.equalTo(SanksjonType.IKKE_INNVILGET_PERIODE),
                                        "stans", "sanksjon") },
                                    english { +" - " + ifElse(periode.sanksjonType.equalTo(SanksjonType.IKKE_INNVILGET_PERIODE),
                                        "stopped", "sanction") },
                                )
                            }
                            showIf(periode.institusjon) {
                                text(
                                    bokmal { +" - institusjon" },
                                    nynorsk { +" - institusjon" },
                                    english { +" - institution" },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}