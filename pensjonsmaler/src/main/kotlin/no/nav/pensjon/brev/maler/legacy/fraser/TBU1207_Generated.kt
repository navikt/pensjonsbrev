package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU1207_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1207NN, TBU1207, TBU1207EN]

        paragraph {
            textExpr(
                Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " kroner per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + " kroner.",
                Nynorsk to "Vi har lagt til grunn at du framover skal ha ei inntekt på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " kroner per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + " kroner.",
                English to "We have determined that your future income will be NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per year. You may also have an annual income of 40 percent of the national insurance basic amount, without your disability benefit being reduced. This is currently NOK " + pe.ut_inntektsgrense_faktisk().format() + ", which is your income limit.",
            )
        }
    }
}
