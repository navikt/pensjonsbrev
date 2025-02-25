package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.KronerText
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
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.ytelseFoerAvkorting
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

data class BeregningsperiodetabellEtterbetaling(
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
                    column(2) {
                        text(
                            Language.Bokmal to "Stønad før reduksjon for inntekt",
                            Language.Nynorsk to "Stønad før reduksjon",
                            Language.English to "Allowance amount before reduction",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Inntekt",
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
                forEach(beregningsperioder) {
                    row {
                        cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                        cell { includePhrase(KronerText(it.ytelseFoerAvkorting)) }
                        cell { includePhrase(KronerText(it.inntekt)) }
                        cell { includePhrase(KronerText(it.utbetaltBeloep)) }
                    }
                }
            }
        }
    }
}