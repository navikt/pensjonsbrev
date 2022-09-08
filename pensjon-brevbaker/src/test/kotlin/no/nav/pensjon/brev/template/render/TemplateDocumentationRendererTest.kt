package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.maler.UngUfoerAuto
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test

class TemplateDocumentationRendererTest {

    @Test
    fun renderLetterExample() {
        Letter(
            LetterExample.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { TemplateDocumentationRenderer.render(it) }
            .also { writeTestHTML("DESIGN_REFERENCE_LETTER", it, "template_doc") }
    }

    @Test
    fun renderUngUfoerAuto() {
        Letter(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Bokmal,
            Fixtures.felles,
        ).let { TemplateDocumentationRenderer.render(it) }
            .also { writeTestHTML("UNG_UFOER_AUTO", it, "template_doc") }
    }

}