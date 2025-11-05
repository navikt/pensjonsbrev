

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


data class TBU1178_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1178EN, TBU1178, TBU1178NN]

		paragraph {
			text (
				bokmal { + "Du har fått innvilget uføretrygd fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
				nynorsk { + "Du har fått innvilga uføretrygd frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyljer 18 år." },
				english { + "You have been granted disability benefit from " + pe.vedtaksdata_virkningfom().format() + ". We call this the effective date. To be eligible for disability benefit, you must have turned 18. Therefore, you will receive disability benefit from the month after you turn 18." },
			)
		}
    }
}
        