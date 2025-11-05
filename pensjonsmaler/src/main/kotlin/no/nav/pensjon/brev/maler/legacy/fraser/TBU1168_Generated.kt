

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1168_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1168EN, TBU1168, TBU1168NN]

		paragraph {
			text (
				bokmal { + "Du er ikke innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom, fordi vi har vurdert at andre medisinske årsaker har ført til din varige nedsatte inntektsevne <FRITEKST: konkret begrunnelse>." },
				nynorsk { + "Du er ikkje innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom fordi vi har vurdert at andre medisinske årsaker har ført til at du har varig nedsett inntektsevne <FRITEKST: konkret begrunnelse>." },
				english { + "You have not been granted disability benefit under the provisions for occupational injury or illness, because we have concluded that there are other medical reasons for your permanently reduced earning ability <FRITEKST: konkret begrunnelse>." },
			)
		}
    }
}
