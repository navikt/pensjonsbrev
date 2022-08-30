package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import org.junit.jupiter.api.Test

class PensjonHTMLTest {
    @Test
    fun renderDesignReference() {
        Letter(
            DesignReferenceLetter.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("DESIGN_REFERENCE_LETTER", it) }
    }
}