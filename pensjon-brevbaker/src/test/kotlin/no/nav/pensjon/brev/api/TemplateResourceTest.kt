package no.nav.pensjon.brev.api

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.latex.LaTeXCompilerHttpService
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.maler.example.Testmaler
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TemplateResourceTest {
    private val pdfInnhold = "generert pdf"
    private val pdf = pdfInnhold.toByteArray()
    private val latexMock = mockk<LaTeXCompilerHttpService> {
        coEvery { producePDF(any(), any()) } returns PDFCompilationOutput(pdf)
    }
    private val autobrev = AutobrevTemplateResource("autobrev", Testmaler.hentAutobrevmaler(), latexMock, mockk(), mockk())

    private val validAutobrevRequest = BestillBrevRequest(
        LetterExample.kode,
        createLetterExampleDto(),
        Fixtures.fellesAuto,
        LanguageCode.BOKMAL
    )

    @BeforeEach
    fun setup() {
        FeatureToggleSingleton.init(
            object : FeatureToggleService {
                override fun isEnabled(toggle: FeatureToggle) = true
            }
        )
    }

    @Test
    fun `can renderPDF with valid letterData`(): Unit = runBlocking {
        val result = autobrev.renderPDF(validAutobrevRequest)
        assertEquals(
            LetterResponse(pdfInnhold.toByteArray(), ContentType.Application.Pdf.toString(), LetterExample.template.letterMetadata),
            result
        )
    }

    @Test
    fun `can renderHTML with valid letterData`() {
        val result = autobrev.renderHTML(validAutobrevRequest)
        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(), result.contentType)
        assertEquals(LetterExample.template.letterMetadata, result.letterMetadata)
    }

    @Test
    fun `fails renderPDF with invalid letterData`(): Unit = runBlocking {
        assertThrows<ParseLetterDataException> {
            autobrev.renderPDF(validAutobrevRequest.copy(letterData = SampleLetterData(true)))
        }
    }

    @Test
    fun `fails renderHTML with invalid letterData`() {
        assertThrows<ParseLetterDataException> {
            autobrev.renderHTML(validAutobrevRequest.copy(letterData = SampleLetterData(true)))
        }
    }
}

private fun <T: Brevkode<T>> BestillBrevRequest<T>.copy(letterData: SampleLetterData): BestillBrevRequest<T> =
    BestillBrevRequest(
        kode = this.kode,
        letterData = letterData,
        felles = this.felles,
        language = this.language
    )

data class SampleLetterData(val v1: Boolean) : BrevbakerBrevdata