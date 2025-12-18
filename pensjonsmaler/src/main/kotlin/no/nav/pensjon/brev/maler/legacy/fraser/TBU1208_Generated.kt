package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU1208_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1208NN, TBU1208, TBU1208EN]

		paragraph {
			text (
				bokmal { + "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad." },
				nynorsk { + "Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad. " },
				english { + "Your disability benefit will not be reduced until your income exceeds the income limit of " + pe.ut_inntektsgrense_faktisk().format() + "." },
			)
		}
		paragraph {
			text(
				bokmal { + "For deg utgjør kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er bare den delen av inntekten din som overstiger " + pe.ut_inntektsgrense_faktisk().format()
                        + ", som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av den inntekten du har over " + pe.ut_inntektsgrense_faktisk().format() + " trekkes fra uføretrygden din." },
				nynorsk { + "For deg utgjer kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er berre den delen av inntekta di som overstig " + pe.ut_inntektsgrense_faktisk().format()
                        + ", som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av inntekta du har over " + pe.ut_inntektsgrense_faktisk().format() + " blir trekt frå uføretrygda di." },
				english { + "It is only the part of your income that exceeds the income limit that will result in a reduction of your disability benefit. Your disability benefit is reduced by " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " percent of income exceeding " + pe.ut_inntektsgrense_faktisk().format()
                        + " because your compensation level is " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " percent. You can read more about this in the attachment " + quoted("Information about calculations") + "." },
			)
		}
    }
}
