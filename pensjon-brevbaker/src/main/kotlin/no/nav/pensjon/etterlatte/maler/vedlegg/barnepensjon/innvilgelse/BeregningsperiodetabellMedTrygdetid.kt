package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse

import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

data class BeregningsperiodetabellMedTrygdetid(
    val beregningsperioder: Expression<List<Beregningsperiode>>,
    val aarTrygdetid: Expression<Int>
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
                    column(1) {
                        text(
                            Language.Bokmal to "Trygdetid",
                            Language.Nynorsk to "",
                            Language.English to "",
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
                                Language.Bokmal to aarTrygdetid.format() + " år",
                                Language.Nynorsk to aarTrygdetid.format() + " år",
                                Language.English to aarTrygdetid.format() + " years",
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