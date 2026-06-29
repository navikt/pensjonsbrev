package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.selectors.trygdetidsperiode.*
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
                            bokmal { +"Periode" },
                            nynorsk { +"Periode" },
                            english { +"Period" },
                        )
                    }
                    column(1) {
                        text(
                            bokmal { +"Land" },
                            nynorsk { +"Land" },
                            english { +"Country" },
                        )
                    }
                    column(2) {
                        text(
                            bokmal { +"Grunnlag trygdetid" },
                            nynorsk { +"Grunnlag trygdetid" },
                            english { +"Basis for contribution time" },
                        )
                    }
                }
            ) {
                forEach(trygdetidsperioder) { periode ->
                    row {
                        cell { includePhrase(PeriodeITabell(periode.datoFOM, periode.datoTOM)) }
                        cell {
                            ifNotNull(periode.land) { land ->
                                text(
                                    bokmal { +land + " (" + periode.landkode + ")" },
                                    nynorsk { +land + " (" + periode.landkode + ")" },
                                    english { +land + " (" + periode.landkode + ")" },
                                )
                            }.orShow {
                                text(
                                    bokmal { +periode.landkode },
                                    nynorsk { +periode.landkode },
                                    english { +periode.landkode },
                                )
                            }
                        }
                        cell {
                            ifNotNull(periode.opptjeningsperiode) {
                                text(
                                    bokmal { +it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (fremtidig trygdetid)", "") },
                                    nynorsk { +it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (framtidig trygdetid)", "") },
                                    english { + it.format() + ifElse(periode.type.equalTo(TrygdetidType.FREMTIDIG), " (future contribution time)", "") },
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
