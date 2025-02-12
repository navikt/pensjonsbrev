package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU046V_1 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Trygdetiden din i land som Norge har trygdeavtale med, er fastsatt på grunnlag av følgende perioder:",
                Nynorsk to "Trygdetida di i land som Noreg har trygdeavtale med, er fastsett på grunnlag av følgjande periodar:",
                English to "Your period of national insurance coverage in countries with which Norway has a national insurance agreement, has been determined on the basis of the following periods of coverage:",
            )
        }
    }
}
