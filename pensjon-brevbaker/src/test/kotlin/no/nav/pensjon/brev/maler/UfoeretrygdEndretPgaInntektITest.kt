package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class UfoeretrygdEndretPgaInntektITest {

    @Test
    fun testPdf() {
        Letter(
            UfoeretrygdEndretPgaInntekt.template,
            Fixtures.create<UfoeretrygdEndretPgaInntektDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UFOER_ENDRET_PGA_INNTEKT", it) }
    }

}