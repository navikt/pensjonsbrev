package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TrygdetidEOSInnledning : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Trygdetiden din i andre EØS-land er fastsatt på grunnlag av følgende perioder:" },
                nynorsk { + "Trygdetida di i andre EØS-land er fastsett på grunnlag av følgjande periodar:" },
            )
        }
    }
}