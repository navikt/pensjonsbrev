package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class EndringIOpptjeningAutoTest {

    @Test
    fun testPdf() {
        Letter(
            EndringIOpptjeningAuto.template,
            Fixtures.create<EndringIOpptjeningAutoDto>().copy(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_ENDR_OPPTJ_AUTO_BOKMAL", it) }
    }

}
