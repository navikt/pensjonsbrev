

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1215_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1215NN, TBU1215, TBU1215EN]

		paragraph {
			text (
				bokmal { + "Du er innvilget uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. " },
				nynorsk { + "Du er innvilga uføretrygd med attlevandetillegg. Tillegget er rekna ut etter utrekningsgrunnlaget og trygdetida både for deg og for den avdøde ektefellen din. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med. " },
				english { + "You have been granted disability benefit with a survivor's supplement. This supplement is calculated on the basis of your and your deceased spouse's bases for calculation and periods of national insurance coverage. If your income exceeds your income limit, your survivor's supplement will be reduced by the same percentage as your disability benefit. " },
			)

			//PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
			showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()){
				text (
					bokmal { + "Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
					nynorsk { + "Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
					english { + "The supplement is time limited to five years from your effective date. " },
				)
			}
		}
    }
}
        