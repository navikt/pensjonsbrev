package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk_minus_60000
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU2357_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2357, TBU2357NN, TBU2357EN]

		paragraph {
			textExpr (
				Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " kroner per år. Du kan i tillegg ha en årlig inntekt på 60 000 kroner, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + " kroner.",
				Nynorsk to "Vi har lagt til grunn at du framover skal ha ei inntekt på ".expr() + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " kroner per år. Du kan i tillegg ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + " kroner.",
				English to "Our calculations are based on your future annual income being NOK ".expr() + pe.ut_inntektsgrense_faktisk_minus_60000().format() + ". In addition, you may earn an annual income of up to NOK 60 000 without your disability benefit being reduced. This is currently NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ", which is your income limit.",
			)
		}
    }
}