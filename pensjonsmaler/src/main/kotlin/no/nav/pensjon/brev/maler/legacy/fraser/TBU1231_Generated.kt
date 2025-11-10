package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*


data class TBU1231_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1231, TBU1231NN, TBU1231EN]

		paragraph {
			text (
				bokmal { + "Med vennlig hilsen" },
				nynorsk { + "Med vennleg helsing" },
				english { + "Yours sincerely" },
			)
			text (
				bokmal { + "<Navn attesterer>	" + pe.xml_brev_saksbehandler() },
				nynorsk { + "<Namn attesterar>	" + pe.xml_brev_saksbehandler() },
				english { + "<Name of certifier>	" + pe.xml_brev_saksbehandler() },
			)
			text (
				bokmal { + "Saksbehandler	Saksbehandler" },
				nynorsk { + "Saksbehandlar	Saksbehandlar" },
				english { + "Officer in charge	Officer in charge" },
			)
			text (
				bokmal { + pe.kontaktinformasjon_navnavsenderenhet() },
				nynorsk { + pe.kontaktinformasjon_navnavsenderenhet() },
				english { + pe.kontaktinformasjon_navnavsenderenhet() },
			)
		}
    }
}
        