

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3730_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3730, TBU3730NN, TBU3730EN]

		paragraph {
			text (
				Bokmal to "Skatt for deg som bor i utlandet",
				Nynorsk to "Skatt for deg som bur i utlandet",
				English to "Tax for people who live abroad",
			)
			text (
				Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på $SKATTEETATEN_URL. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
				Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på $SKATTEETATEN_URL. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
				English to "You can find more information about withholding tax to Norway at $SKATTEETATEN_URL. For information about taxation from your country of residence, you can contact the locale tax authorities.",
			)
		}
    }
}
