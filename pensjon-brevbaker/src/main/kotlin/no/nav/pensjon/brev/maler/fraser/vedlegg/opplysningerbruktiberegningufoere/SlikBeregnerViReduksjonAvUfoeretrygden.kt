package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harGammelUTBeloepUlikNyUTBeloep_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoPerAarReduksjonUT_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.overskytendeInntekt_safe
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

/* IF KravAarsakType = "ENDRET_INNTEKT"
AND
harGammelUTBeloepUlikNyUTBeloep
AND
harInntektsgrenseLessThanInntektstak,
AND
brevkode <> PE_UT_04_108, PE_UT_04_109,
AND
harNyUTBeloep */
data class SlikBeregnerViReduksjonAvUfoeretrygden(
    val inntektsAvkortingGjeldende: Expression<OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende>,
    val kravAarsakType: Expression<KravAarsakType>,
    val ufoeretrygdGjeldende: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad.format()
        val harInntektsgrenseLessThanInntektstak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)

        ifNotNull(
            ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep_safe,
            ufoeretrygdOrdinaer.harNyUTBeloep_safe
        ) { harUlikeBeloep, harNyBeloep ->
            showIf(
                (kravAarsakType.isOneOf(KravAarsakType.ENDRET_INNTEKT)) and harUlikeBeloep
                        and inntektsAvkortingGjeldende.forventetInntektAar.greaterThanOrEqual(inntektsAvkortingGjeldende.inntektsgrenseAar)
                        and harInntektsgrenseLessThanInntektstak and harNyBeloep
            ) {
                title1 {
                    text(
                        Bokmal to "Slik beregner vi reduksjonen av uføretrygden",
                        Nynorsk to "Slik bereknar vi reduksjonen av uføretrygda",
                        English to "This is how the reduction in your disability benefit is calculated"
                    )
                }
                ifNotNull(
                    ufoeretrygdOrdinaer.overskytendeInntekt_safe,
                    ufoeretrygdOrdinaer.nettoPerAarReduksjonUT_safe
                ) { overskytendeInntekt, nettoPerAarReduksjonUT ->
                    paragraph {
                        textExpr(
                            Bokmal to "".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad + " % = ".expr() +
                                    nettoPerAarReduksjonUT.format() + " kroner i reduksjon for året".expr(),
                            Nynorsk to "".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad + " % = ".expr() +
                                    nettoPerAarReduksjonUT.format() + " kroner i reduksjon for året".expr(),
                            English to "NOK ".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad + " % = NOK ".expr() +
                                    nettoPerAarReduksjonUT.format() + " in reductions for the year".expr()
                        )
                    }
                }
            }
        }
    }
}
