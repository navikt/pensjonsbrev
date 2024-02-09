package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

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
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.utbetaltBeloep

object BarnepensjonRevurderingFraser {

    data class RevurderingVedtak(
        val erEndret: Expression<Boolean>,
        val beregning: Expression<BarnepensjonBeregning>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertNyesteUtbetalingsperiodeDatoFom = beregning.sisteBeregningsperiode.datoFOM.format()
                val formatertVirkningsdato = beregning.virkningsdato.format()
                val formatertBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep.format()

                showIf(erEndret) {
                    showIf(harFlereUtbetalingsperioder) {
                        textExpr(
                            Language.Bokmal to "Barnepensjonen din er endret fra ".expr() + formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". ",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to "Barnepensjonen din er endret fra ".expr() + formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner hver måned før skatt. ",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                    text(
                        Language.Bokmal to "Se utbetalingsbeløp for tidligere perioder i vedlegg om etterbetaling.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din er vurdert på nytt. Du får fortsatt ".expr() + formatertBeloep + " kroner per måned før skatt.",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr()
                    )
                }
            }
        }
    }

    data class FyllInn(val etterbetaling: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph { text(
                Language.Bokmal to "(utfall jamfør tekstbibliotek)",
                Language.Nynorsk to "(utfall jamfør tekstbibliotek)",
                Language.English to "(utfall jamfør tekstbibliotek)",
            ) }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § <riktig paragrafhenvisning> ",
                    Language.Nynorsk to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § <riktig paragrafhenvisning> ",
                    Language.English to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § <riktig paragrafhenvisning> ",
                )
                showIf(etterbetaling) {
                    text(
                        Language.Bokmal to ", § 22-12 og § 22-13.",
                        Language.Nynorsk to ", § 22-12 og § 22-13.",
                        Language.English to ", § 22-12 og § 22-13."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "og § 22-12.",
                        Language.Nynorsk to "og § 22-12.",
                        Language.English to "og § 22-12."
                    )
                }

            }
        }
    }
}