package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.PeriodeITabell

data class Beregningsperiodetabell(
    val beregningsperioder: Expression<List<Beregningsperiode>>,
    val grunnbeloep: Expression<Kroner>,
    val antallBarn: Expression<Int>
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
                            Language.Bokmal to "Grunnbeløp",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    column(2) {
                        text(
                            Language.Bokmal to "Utbetaling per måned før skatt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            ) {
                forEach(beregningsperioder) {
                    row {
                        cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                        cell { textExpr(
                            Language.Bokmal to it.grunnbeloep.format(),
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr(),
                        )
                        }
                        cell { textExpr(
                            Language.Bokmal to it.utbetaltBeloep.format(),
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