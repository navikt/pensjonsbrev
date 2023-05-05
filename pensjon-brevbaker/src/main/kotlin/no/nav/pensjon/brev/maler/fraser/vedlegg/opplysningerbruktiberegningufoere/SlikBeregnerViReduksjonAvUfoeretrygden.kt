package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoPerAarReduksjonUT_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.overskytendeInntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// brevkode <> PE_UT_04_108, PE_UT_04_109,

data class SlikBeregnerViReduksjonAvUfoeretrygden(
    val kravAarsakType: Expression<KravAarsakType>,
    val ufoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.Ufoeretrygd>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val kompensasjonsgrad = ufoeretrygd.kompensasjonsgrad.format()

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


