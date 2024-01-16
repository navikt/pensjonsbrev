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
    val trygdetid: Expression<Trygdetid>,
    val sisteBeregningsperiode: Expression<OmstillingsstoenadBeregningsperiode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(1) {
                        text(
                            Language.Bokmal to "Trygdetid",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Stønad før reduksjon for inntekt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                        newline()
                        text(
                            Language.Bokmal to "(2,25 × G × trygdetid)",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Inntekt før skatt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Utbetaling per måned før skatt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            ) {
                row {
                    cell {
                        textExpr(
                            Language.Bokmal to sisteBeregningsperiode.trygdetid.format() + " år",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr(),
                        )
                        ifNotNull(trygdetid.prorataBroek) {
                            newline()
                            textExpr(
                                Language.Bokmal to "".expr() + "(" + trygdetid.prorataBroek.formatBroek() + ")",
                                Language.Nynorsk to "".expr(),
                                Language.English to "".expr(),
                            )
                        }
                    }
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.ytelseFoerAvkorting)) }
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.inntekt)) }
                    cell { includePhrase(Felles.KronerText(sisteBeregningsperiode.utbetaltBeloep)) }
                }
            }
        }
    }
}