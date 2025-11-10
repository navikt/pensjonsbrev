package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class TBU3032_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3032EN, TBU3032NN, TBU3032]

		paragraph {
			text (
				bokmal { + "Etterbetaling av uføretrygd" },
				nynorsk { + "Etterbetaling av uføretrygd" },
				english { + "Final settlement" },
			)
			text (
				bokmal { + "Du får etterbetalt uføretrygd fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra NAV eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
				nynorsk { + "Du får etterbetalt uføretrygd frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå NAV eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
				english { + "You will be paid disability benefit retroactively, effective as of " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". The amount is normally paid within seven working days. Certain deductions may be made from this payment, such as for taxes and benefits you have received from NAV or others, e.g. through an occupational pension scheme. In such cases, the retroactive payment may be delayed by up to nine weeks. If any deductions have been made from your payment, it will be specified in the notice." },
			)
		}
    }
}
        