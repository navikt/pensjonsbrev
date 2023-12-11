package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.land
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.opptjeningsperiode
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

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
                            Language.Nynorsk to "Periode",
                            Language.English to "Period",
                        )
                    }
                    column(1) {
                        text(
                            Language.Bokmal to "Land",
                            Language.Nynorsk to "Land",
                            Language.English to "Country",
                        )
                    }
                    column(2) {
                        text(
                            Language.Bokmal to "Beregnet trygdetid",
                            Language.Nynorsk to "Utrekna tid",
                            Language.English to "Calculated time",
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
                                    Language.Nynorsk to it.format(),
                                    Language.English to it.format(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Expression<Periode>.format() = Expression.BinaryInvoke(
    first = this,
    second = Expression.FromScope(ExpressionScope<Any, *>::language),
    operation = PeriodeFormatter,
)

object PeriodeFormatter : LocalizedFormatter<Periode>() {
    override fun apply(first: Periode, second: Language): String {
        val actualValues = listOfNotNull(
            first.aar.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it år"
                    Language.Nynorsk -> "$it år"
                    Language.English -> "$it years"
                }
            },
            first.maaneder.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it måneder"
                    Language.Nynorsk -> "$it månadar"
                    Language.English -> "$it months"
                }
            },
            first.dager.takeIf { it > 0 }?.let {
                when (second) {
                    Language.Bokmal -> "$it dager"
                    Language.Nynorsk -> "$it dagar"
                    Language.English -> "$it days"
                }
            }
        )

        return actualValues.joinToString(separator = ", ") { it }
    }
}
