package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.ytelseFoerAvkorting
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.formatBroek

data class Beregningsperiodetabell(
    val sisteBeregningsperiode: Expression<OmstillingsstoenadBeregningsperiode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(1) {
                        text(
                            Language.Bokmal to "Stønad før reduksjon for inntekt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Inntekten din",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Utbetaling per måned",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            ) {
                row {
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.ytelseFoerAvkorting)) }
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.inntekt)) }
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.utbetaltBeloep)) }
                }
            }
        }
    }
}