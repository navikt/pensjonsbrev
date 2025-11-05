

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1262_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1262NN, TBU1262EN, TBU1262]

		paragraph {
			text (
				bokmal { + "Vi har kommet fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. <FRITEKST: Konkret begrunnelse>." },
				nynorsk { + "Vi har kome fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom. <FRITEKST: Konkret begrunnelse>." },
				english { + "We have concluded that " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " percent of your disability was caused by a certified occupational injury or occupational illness. <FRITEKST: konkret begrunnelse>." },
			)
		}
    }
}
        