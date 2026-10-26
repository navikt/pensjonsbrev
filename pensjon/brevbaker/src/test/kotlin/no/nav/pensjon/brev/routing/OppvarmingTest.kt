package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.brev.brevbaker.pdfbygger.api.PDFCompilationOutput
import no.nav.pensjon.brev.pensjonOgUfoereProductionTemplates
import no.nav.pensjon.brev.template.brevbakerConfig
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OppvarmingTest {
    private val pdfByggerService = object : PDFByggerService {
        override suspend fun producePDF(pdfRequest: PDFRequest) = PDFCompilationOutput(ByteArray(0))
        override suspend fun producePDFV2(pdfRequest: LetterPDFRequest) = PDFCompilationOutput(ByteArray(0))
    }

    @Test
    fun `oppvarming med produksjonsmaler fullfoerer uten feil`() {
        val objectMapper = jacksonObjectMapper().apply { brevbakerConfig() }
        assertTrue(varmOpp(pensjonOgUfoereProductionTemplates.hentRedigerbareMaler(), objectMapper, pdfByggerService))
    }
}
