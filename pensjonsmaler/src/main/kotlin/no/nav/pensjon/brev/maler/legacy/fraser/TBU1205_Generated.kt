package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU1205_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1205NN, TBU1205, TBU1205EN]

		paragraph {
			text (
				bokmal { + "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensen din." },
				nynorsk { + "Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensa di." },
				english { + "You may have an annual income up to 40 percent of the National Insurance basic amount, without your disability benefit being reduced. This is currently " + pe.ut_inntektsgrense_faktisk().format() + ", which is your income limit." },
			)
		}
    }
}
        