package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.something.Fraser
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.base.PensjonLatex

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        name = "eksempelBrev",
        title = title(Language.Bokmal to "Eksempelbrev"),
        base = PensjonLatex,
        lang = languages(Language.Bokmal)
    ) {
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