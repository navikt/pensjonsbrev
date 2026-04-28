package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU047V: OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du har vært medlem i folketrygden i mindre enn 4/5 av tiden fra du fylte 16 år og fram til uføretidspunktet ditt. Det betyr at den framtidige trygdetiden din skal utgjøre 40 år hvor vi trekker fra 4/5 av tiden fra du fylte 16 år fram til uføretidspunktet ditt." },
                nynorsk { + "Du har vore medlem i folketrygda i mindre enn 4/5 av tida frå du fylte 16 år og fram til uføretidspunktet ditt. Det vil seie at den framtidige trygdetida di skal utgjere 40 år, der vi trekkjer frå 4/5 av tida frå du fylte 16 år og fram til uføretidspunktet ditt." },
            )
        }
    }
}