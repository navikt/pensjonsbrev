package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Phrase

object Fraser {
    object Tittel {
        val pensjonInnvilget = Phrase.Static.create("pensjonInnvilget", Language.Bokmal to "Du har fått innvilget pensjon")
    }
}