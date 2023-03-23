package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.Kroner
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
    val forventetInntektAar: Expression<Kroner>,
    val harGammelUTBeloepUlikNyUTBeloep: Expression<Boolean>,
    val harInntektsgrenseLessThanInntektstak: Expression<Boolean>,
    val harNyUTBeloep: Expression<Boolean>,
    val inntektsgrenseAar: Expression<Kroner>,
    val kompensasjonsgrad: Expression<Double>,
    val kravAarsakType: Expression<KravAarsakType>,
    val nettoPerAarReduksjonUT: Expression<Kroner>,
    val overskytendeInntekt: Expression<Kroner>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            (kravAarsakType.isOneOf(KravAarsakType.ENDRET_INNTEKT)) and harGammelUTBeloepUlikNyUTBeloep and forventetInntektAar.greaterThanOrEqual(
                inntektsgrenseAar
            ) and harInntektsgrenseLessThanInntektstak and harNyUTBeloep) {
            title1 {
                text(
                    Bokmal to "Slik beregner vi reduksjonen av uføretrygden",
                    Nynorsk to "Slik bereknar vi reduksjonen av uføretrygda",
                    English to "This is how the reduction in your disability benefit is calculated"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad.format() + " % = ".expr() + nettoPerAarReduksjonUT.format() + " kroner i reduksjon for året".expr(),
                    Nynorsk to "".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad.format() + " % = ".expr() + nettoPerAarReduksjonUT.format() + " kroner i reduksjon for året".expr(),
                    English to "NOK ".expr() + overskytendeInntekt.format() + " kr x ".expr() + kompensasjonsgrad.format() + " % = NOK ".expr() + nettoPerAarReduksjonUT.format() + " in reductions for the year".expr()
                )
            }
        }
    }
}
