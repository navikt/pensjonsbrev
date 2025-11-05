

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1132_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1132, TBU1132EN, TBU1132NN]

		paragraph {
			text (
				bokmal { + "Vi kan på grunnlag av bestemmelsene i <FRITEKST: Trygdeavtale> gi unntak fra folketrygdlovens vilkår om medlemskap fram til uføretidspunktet, ved å legge sammen perioder med medlemskap i folketrygden og medlemskap i et annet land Norge har trygdeavtale med." },
				nynorsk { + "Vi kan på grunnlag av reglane i <FRITEKST: Trygdeavtale> gi unntak frå vilkåra i folketrygdlova om medlemskap fram til uføretidspunktet, ved å leggje saman periodar med medlemstid i Noreg og medlemstid i eit anna land Noreg har trygdeavtale med." },
				english { + "Based on the provisions in <FRITEKST: Trygdeavtale>, we can grant an exemption from the provisions of the Norwegian National Insurance Act relating to membership up to the date of disability. We do this by adding together the periods of your membership in Norway and your period of membership in another country with which Norway has a social security agreement." },
			)
		}
    }
}
