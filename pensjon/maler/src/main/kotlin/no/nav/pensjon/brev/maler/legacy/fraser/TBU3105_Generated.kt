

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.pegruppe10.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*


data class TBU3105_Generated(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3105EN, TBU3105NN, TBU3105]

		paragraph {
			text (
				bokmal { + "Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til NAV. Forsørger du barn " },
				nynorsk { + "Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til NAV. Viss du forsørgjer barn" },
				english { + "If you have fixed and necessary housing expenses, we will consider a lower reduction in your disability benefit. You must submit documentation of your housing expenses to NAV. If you support children " },
			)

			//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
			showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
				text (
					bokmal { + "og/eller ektefelle " },
					nynorsk { + " og/eller ektefelle" },
					english { + "and/or spouse " },
				)
			}
			text (
				bokmal { + "under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
				nynorsk { + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
				english { + "during your stay in the institution, we will not reduce your disability benefit." },
			)
		}
    }
}
        