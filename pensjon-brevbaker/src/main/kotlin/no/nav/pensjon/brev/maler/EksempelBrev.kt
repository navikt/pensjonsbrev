package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.something.Fraser
import no.nav.pensjon.brev.something.PensjonLatex
import no.nav.pensjon.brev.template.*

object EksempelBrev : StaticTemplate {
    override val template = createTemplate("eksempelBrev", PensjonLatex, languages(Language.Bokmal)) {
        parameters {
            optional { PensjonInnvilget }
        }

        outline {
            title1 {
                text(Language.Bokmal to "Heisann. ")
                phrase(Fraser.Tittel.pensjonInnvilget)
            }
        }
    }
}