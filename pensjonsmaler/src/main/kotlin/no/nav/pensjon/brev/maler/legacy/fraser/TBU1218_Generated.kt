

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1218_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1218NN, TBU1218, TBU1218EN]

		paragraph {
			text (
				bokmal { + "For deg som kombinerer uføretrygd og alderspensjon" },
				nynorsk { + "For deg som kombinerer uføretrygd og alderspensjon" },
				english { + "For those combining the disability benefit with retirement pension" },
			)
			text (
				bokmal { + "Du mottar alderspensjon fra folketrygden. Hvis du kombinerer uføretrygd og alderspensjon kan disse til sammen ikke utgjøre mer enn 100 prosent." },
				nynorsk { + "Du mottar alderspensjon frå folketrygda. Viss du kombinerer uføretrygd og alderspensjon, kan den totale prosenten ikkje vere høgare enn 100 prosent." },
				english { + "You receive an old-age pension. If you combine disability benefit and old-age pension, the total percentage cannot exceed 100 percent." },
			)
		}
    }
}
