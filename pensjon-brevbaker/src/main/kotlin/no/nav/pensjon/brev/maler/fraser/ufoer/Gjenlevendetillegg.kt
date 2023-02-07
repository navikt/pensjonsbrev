package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object Gjenlevendetillegg {

    // TBU1214, TBU3360, TBU1216, TBU2368, TBU1133
    data class HarGjenlevendetillegg(
        val forventetInntekt: Expression<Kroner>,
        val harGjenlevendetilleggInnvilget: Expression<Boolean>,
        val inntektsgrense: Expression<Kroner>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harGjenlevendetilleggInnvilget) {
                title1 {
                    text(
                        Bokmal to "For deg som mottar gjenlevendetillegg",
                        Nynorsk to "For deg som mottar gjenlevendetillegg",
                        English to "Survivor's supplement"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du mottar uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Gjenlevendetillegget ditt har samme grad som uføretrygden din. Endringer i opptjeningsgrunnlaget for uføretrygden din vil også ha betydning for beregningen av gjenlevendetillegget ditt. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
                        Nynorsk to "Du mottar uføretrygd med gjenlevendetillegg. Tillegget er beregna etter ditt eget og din avdøde ektefelles berekningsgrunnlag og trygdetid. Gjenlevendetillegget ditt har same grad som uføretrygden din. Endringar i oppteningsgrunnlaget for uføretrygden din vil også ha betydning for berekningen av gjenlevendetillegget ditt. Tenar du meir enn inntektsgrensen din , reduserer vi gjenlevendetillegget ditt med same prosent som vi reduserer uføretrygden din med.",
                        English to "You receive disability benefit with survivor's supplement. The calculation of the supplement is based on both your own and your deceased spouse's basis for calculation and social security period. Your survivor's supplement has the same percent rate as your disability benefit. Any changes in the basis of earnings for your disability benefit will also have importance for the calculation of your survivor's supplement. If you earn more than your income limit, your survivor's supplement will be reduced with the same percent rate that your disability benefit is reduced with."
                    )
                }
                paragraph {
                    showIf(forventetInntekt.lessThanOrEqual(inntektsgrense)) {
                        text(
                            Bokmal to "Gjenlevendetillegget ditt er ikke redusert.",
                            Nynorsk to "Gjenlevendetillegget ditt er ikkje redusert.",
                            English to "Your survivor's supplement is not reduced."
                        )
                    }.orShowIf(forventetInntekt.greaterThanOrEqual(inntektsgrense)) {
                        textExpr(
                            Bokmal to "Du har en inntekt tilsvarende ".expr() + forventetInntekt.format() + " kroner. Gjenlevendetillegget er redusert ut fra dette.".expr(),
                            Nynorsk to "Du har ein inntekt tilsvarande " .expr() + forventetInntekt.format() + " kroner. Gjenlevendetillegget er redusert ut frå dette.".expr(),
                            English to "You have an income equivalent to NOK ".expr() + forventetInntekt.format() + ", which is more than your income limit of NOK ".expr() + inntektsgrense.format() + ". Your survivor's supplement is therefore reduced.".expr()
                        )
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
                        Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om utrekninga».",
                        English to "You can read more about this in the attachment «Information about calculations»."
                    )
                }
            }
        }
    }
}