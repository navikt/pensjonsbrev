package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FeatureToggleDummy
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test

class HTMLDocumentRendererTest {
    @Test
    fun renderDesignReference() {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        LetterImpl(
            LetterExample.template,
            createLetterExampleDto(),
            Language.Bokmal,
            FellesFactory.fellesAuto
        ).renderTestHtml("LETTER_EXAMPLE")
    }
}