package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
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
                                Language.Nynorsk to "".expr(),
                                Language.English to "".expr(),
                            )
                        }
                        cell {
                            textExpr(
                                Language.Bokmal to it.opptjeningsperiode,
                                Language.Nynorsk to "".expr(),
                                Language.English to "".expr(),
                            )
                        }
                    }
                }
            }
        }
    }
}