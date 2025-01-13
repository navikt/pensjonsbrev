package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.land
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.opptjeningsperiode
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.type
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
                            Language.Bokmal to "Grunnlag trygdetid",
                            Language.Nynorsk to "Grunnlag trygdetid",
                            Language.English to "Basis for contribution time",
                        )
                    }
                }
            ) {
                forEach(trygdetidsperioder) { periode ->
                    row {
                        cell { includePhrase(PeriodeITabell(periode.datoFOM, periode.datoTOM)) }
                        cell {
                            textExpr(
                                Language.Bokmal to periode.land,
                                Language.Nynorsk to periode.land,
                                Language.English to periode.land,
                            )
                        }
                        cell {
                            ifNotNull(periode.opptjeningsperiode) {
                                textExpr(
                                    Language.Bokmal to it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (fremtidig trygdetid)", ""),
                                    Language.Nynorsk to it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (framtidig trygdetid)", ""),
                                    Language.English to  it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (future contribution time)", ""),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Expression<Periode>.format(): StringExpression = format(PeriodeFormatter)

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

    override fun stableHashCode(): Int = "PeriodeFormatter".hashCode()
}
