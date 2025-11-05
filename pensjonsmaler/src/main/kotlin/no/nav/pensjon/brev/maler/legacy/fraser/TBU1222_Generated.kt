

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1222_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1222, TBU1222EN, TBU1222NN]

		paragraph {
			text (
				bokmal { + "Honnørkort" },
				nynorsk { + "Honnørkort" },
				english { + "Concessionary travel card" },
			)
			text (
				bokmal { + "Du har rett til honnørrabatt når du bruker offentlig transport på innenlandsreiser. Vi sender honnørkortet i posten hvis du får dette vedtaket digitalt." },
				nynorsk { + "Du har rett til honnørrabatt når du bruker offentleg transportmiddel på innanlandsreiser. Vi sender honnørkortet i posten dersom du får dette vedtaket digitalt." },
				english { + "You are entitled to discounted trips on public transport in Norway. You will receive your concessionary travel card in a separate shipment if you get this decision electronically. " },
			)
		}
    }
}
