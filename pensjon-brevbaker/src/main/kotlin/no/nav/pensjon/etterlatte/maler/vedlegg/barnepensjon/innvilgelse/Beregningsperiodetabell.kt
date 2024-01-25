package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

data class Beregningsperiodetabell(
    val beregningsperioder: Expression<List<BarnepensjonBeregningsperiode>>,
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
                            Language.Bokmal to "Grunnbeløp",
                            Language.Nynorsk to "Grunnbeløp",
                            Language.English to "Basic amount",
                        )
                    }
                    column(2) {
                        text(
                            Language.Bokmal to "Utbetaling per måned før skatt",
                            Language.Nynorsk to "Utbetaling per månad før skatt",
                            Language.English to "Payout per month before taxes",
                        )
                    }
                }
            ) {
                forEach(beregningsperioder) {
                    row {
                        cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                        cell {
                            textExpr(
                                Language.Bokmal to it.grunnbeloep.format() + " kr",
                                Language.Nynorsk to it.grunnbeloep.format() + " kr",
                                Language.English to "NOK ".expr() + it.grunnbeloep.format(),
                            )
                        }
                        cell {
                            textExpr(
                                Language.Bokmal to it.utbetaltBeloep.format() + " kr",
                                Language.Nynorsk to it.utbetaltBeloep.format() + " kr",
                                Language.English to "NOK ".expr() + it.utbetaltBeloep.format(),
                            )
                        }
                    }
                }
            }
        }
    }
}