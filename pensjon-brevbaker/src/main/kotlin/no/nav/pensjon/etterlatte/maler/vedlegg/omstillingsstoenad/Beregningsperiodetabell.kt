package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.institusjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.ytelseFoerAvkorting
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
                            Language.Bokmal to "Periode",
                            Language.Nynorsk to "Periode",
                            Language.English to "Period",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Stønad før reduksjon for inntekt",
                            Language.Nynorsk to "Stønad før reduksjon for inntekt",
                            Language.English to "Allowance paid before income reduction",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Inntekten din",
                            Language.Nynorsk to "Inntekta di",
                            Language.English to "Your income",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Utbetaling per måned",
                            Language.Nynorsk to "Utbetaling per månad",
                            Language.English to "Payout per month",
                        )
                    }
                }
            ) {
                forEach(beregningsperioder) { periode ->
                    row {
                        cell { includePhrase(PeriodeITabell(periode.datoFOM, periode.datoTOM)) }
                        cell { includePhrase(Felles.KronerText(periode.ytelseFoerAvkorting)) }
                        cell { includePhrase(Felles.KronerText(periode.inntekt)) }
                        cell {
                            includePhrase(Felles.KronerText(periode.utbetaltBeloep))
                            showIf(periode.sanksjon) {
                                text(
                                    Language.Bokmal to " - sanksjon",
                                    Language.Nynorsk to " - sanksjon",
                                    Language.English to " - sanction",
                                )
                            }
                            showIf(periode.institusjon) {
                                text(
                                    Language.Bokmal to " - institusjon",
                                    Language.Nynorsk to " - institusjon",
                                    Language.English to " - institution",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}