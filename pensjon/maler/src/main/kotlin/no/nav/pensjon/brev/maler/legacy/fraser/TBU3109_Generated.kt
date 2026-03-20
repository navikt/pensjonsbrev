

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.pegruppe10.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*


data class TBU3109_Generated(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3109EN, TBU3109NN, TBU3109]

		paragraph {
			text (
				bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn " },
				nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" },
				english { + "You have documented that you have fixed and necessary housing expenses and that you support children" },
			)

			//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
			showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
				text (
					bokmal { + "og/eller ektefelle " },
					nynorsk { + " og/eller ektefelle" },
					english { + " and/or" + " spouse" },
				)
			}
			text (
				bokmal { + "under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
				nynorsk { + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
				english { + " during your stay in the institution. Therefore, we have concluded that your disability benefit payment will not be reduced." },
			)
		}
    }
}
        