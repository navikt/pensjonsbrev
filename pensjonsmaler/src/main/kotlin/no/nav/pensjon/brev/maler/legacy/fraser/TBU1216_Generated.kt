package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1216_Generated : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        // [TBU1216]

        paragraph {
            text(
                Bokmal to "Gjenlevendetillegget ditt er ikke redusert.",
            )
        }
    }
}
