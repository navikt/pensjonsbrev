package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU047V : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du har vært medlem i folketrygden i mindre enn 4/5 av tiden fra du fylte 16 år og fram til uføretidspunktet ditt. Det betyr at den framtidige trygdetiden din skal utgjøre 40 år hvor vi trekker fra 4/5 av tiden fra du fylte 16 år fram til uføretidspunktet ditt.",
                Nynorsk to "Du har vore medlem i folketrygda i mindre enn 4/5 av tida frå du fylte 16 år og fram til uføretidspunktet ditt. Det vil seie at den framtidige trygdetida di skal utgjere 40 år, der vi trekkjer frå 4/5 av tida frå du fylte 16 år og fram til uføretidspunktet ditt.",
                English to "You have had national insurance coverage for less than 4/5 of the period from the year you turned 16 years old until the date of your disability. This means that your future period of national insurance coverage will total 40 years, where we deduct 4/5 of the period from the year you turned 16 years old until the date of your disability.",
            )
        }
    }
}
