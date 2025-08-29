package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
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
                            bokmal { +"Periode" },
                            nynorsk { +"Periode" },
                            english { +"Period" },
                        )
                    }
                    column(1) {
                        text(
                            bokmal { +"Grunnbeløp" },
                            nynorsk { +"Grunnbeløp" },
                            english { +"Basic amount" },
                        )
                    }
                    column(2) {
                        text(
                            bokmal { +"Utbetaling per måned før skatt" },
                            nynorsk { +"Utbetaling per månad før skatt" },
                            english { +"Payout per month before taxes" },
                        )
                    }
                }
            ) {
                forEach(beregningsperioder) {
                    row {
                        cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                        cell { includePhrase(KronerText(it.grunnbeloep)) }
                        cell { includePhrase(KronerText(it.utbetaltBeloep)) }
                    }
                }
            }
        }
    }
}