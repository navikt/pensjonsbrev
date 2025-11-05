

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


data class TBU1175_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1175EN, TBU1175, TBU1175NN]

		paragraph {
			text (
				bokmal { + "Du har fått innvilget uføretrygd fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
				nynorsk { + "Du har fått innvilga uføretrygd frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
				english { + "You have been granted disability benefit from " + pe.vedtaksdata_virkningfom().format() + ". We call this the effective date. You will receive work assessment allowance until this date." },
			)
		}
    }
}
        