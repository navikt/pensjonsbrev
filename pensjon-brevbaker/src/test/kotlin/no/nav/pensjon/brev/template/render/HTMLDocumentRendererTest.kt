package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test

class HTMLDocumentRendererTest {
    @Test
    fun renderDesignReference() {
        Letter(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("LETTER_EXAMPLE")
    }
}