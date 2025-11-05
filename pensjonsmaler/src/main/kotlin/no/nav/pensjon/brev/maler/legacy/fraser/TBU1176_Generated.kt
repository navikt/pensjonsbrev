

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_virkningfom
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1176_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1176EN, TBU1176, TBU1176NN]

		paragraph {
			text (
				bokmal { + "Du har fått innvilget uføretrygd fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til <FRITEKST: dato for opphør> og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
				nynorsk { + "Du har fått innvilga uføretrygd frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til <FRITEKST: Dato for opphør>, og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
				english { + "You have been granted disability benefit from " + pe.vedtaksdata_virkningfom().format() + ". We call this the effective date. Work assessment allowance is paid up to <FRITEKST: Dato for opphør> and disability benefit is paid for the remaining days of " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
			)
		}
    }
}
        