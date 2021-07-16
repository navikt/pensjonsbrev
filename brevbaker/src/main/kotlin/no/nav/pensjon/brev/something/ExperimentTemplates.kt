package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.template.PensjonInnvilget
import no.nav.pensjon.brev.template.SaksNr
import no.nav.pensjon.brev.template.createTemplate

object ExperimentTemplates {
    val eksempelBrev = createTemplate("eksempelBrev", PensjonLatex) {
        parameters {
            required { SaksNr }
            optional { PensjonInnvilget }
        }

        outline {
            title1 {
                text("Heisann. ")
                phrase(Fraser.Tittel.pensjonInnvilget)
            }
        }
    }
}