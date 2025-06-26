package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.text

object AvslagUnder1aarTT : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i X antall dager/måneder. /Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i X antall dager/måneder. /Våre opplysningar viser at du ikkje har budd eller arbeidd i Noreg.",
                English to "We have registered that you have been living or working in Norway X days/months. /We have no record of you living or working in Norway.",
            )
        }

    }
}