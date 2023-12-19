package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test

class PensjonHTMLRendererTest {
    @Test
    fun renderDesignReference() {
        Letter(
            DesignReferenceLetter.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("DESIGN_REFERENCE_LETTER")
    }
}