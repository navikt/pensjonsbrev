package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.land
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.opptjeningsperiode
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell
import java.time.Period

data class Trygdetidstabell(
    val trygdetidsperioder: Expression<List<Trygdetidsperiode>>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(2) {
                        text(
                            Language.Bokmal to "Periode",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Land",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(2) {
                        text(
                            Language.Bokmal to "Beregnet trygdetid",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            ) {
                forEach(trygdetidsperioder) {
                    row {
                        cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                        cell {
                            textExpr(
                                Language.Bokmal to it.land,
                                Language.Nynorsk to it.land,
                                Language.English to it.land,
                            )
                        }
                        cell {
                            ifNotNull(it.opptjeningsperiode) {
                                textExpr(
                                    Language.Bokmal to it.format(),
                                    Language.Nynorsk to "".expr(),
                                    Language.English to "".expr()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Expression<Period>.format() = Expression.BinaryInvoke(
    first = this,
    second = Expression.FromScope(ExpressionScope<Any, *>::language),
    operation = PeriodFormatter,
)

object PeriodFormatter : LocalizedFormatter<Period>() {
    override fun apply(first: Period, second: Language): String {
        val actualValues = listOfNotNull(
            first.years.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it år"
                    Language.English -> "$it"
                    Language.Nynorsk -> "$it"
                }
            },
            first.months.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it måneder"
                    Language.English -> ""
                    Language.Nynorsk -> ""
                }
            },
            first.days.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it dager"
                    Language.English -> ""
                    Language.Nynorsk -> ""
                }
            }
        )

        return actualValues.joinToString(separator = ", ") { it }
    }
}
