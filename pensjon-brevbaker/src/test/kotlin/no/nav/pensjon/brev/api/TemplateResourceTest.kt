package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.maler.example.createEksempelbrevRedigerbartDto
import no.nav.brev.brevbaker.maler.example.createLetterExampleDto
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PDFCompilationOutput
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.maler.example.Testmaler
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TemplateResourceTest {
    private val pdfInnhold = "generert pdf"
    private val base64PDF = Base64.getEncoder().encodeToString(pdfInnhold.toByteArray())
    private val latexMock =
        mockk<LaTeXCompilerService> {
            coEvery { producePDF(any()) } returns PDFCompilationOutput(base64PDF)
        }
    private val autobrev = TemplateResource("autobrev", Testmaler.hentAutobrevmaler(), latexMock)
    private val redigerbar = TemplateResource("autobrev", Testmaler.hentRedigerbareMaler(), latexMock)

    private val validAutobrevRequest =
        BestillBrevRequest(
            LetterExample.kode,
            createLetterExampleDto(),
            Fixtures.fellesAuto,
            LanguageCode.BOKMAL,
        )
    private val validRedigertBrevRequest =
        BestillRedigertBrevRequest(
            EksempelbrevRedigerbart.kode,
            createEksempelbrevRedigerbartDto(),
            Fixtures.felles,
            LanguageCode.BOKMAL,
            LetterMarkup(
                "redigert markup",
                LetterMarkup.Sakspart(
                    "gjelder bruker",
                    "123abc",
                    "001",
                    "en dato",
                ),
                emptyList(),
                LetterMarkup.Signatur(
                    "hilsen oss",
                    "en rolle",
                    "Saksbehandlersen",
                    null,
                    "Akersgata",
                ),
            ),
        )

    @Test
    fun `can renderPDF with valid letterData`(): Unit =
        runBlocking {
            val result = autobrev.renderPDF(validAutobrevRequest)
            assertEquals(
                LetterResponse(pdfInnhold.toByteArray(), ContentType.Application.Pdf.toString(), LetterExample.template.letterMetadata),
                result,
            )
        }

    @Test
    fun `can renderHTML with valid letterData`() {
        val result = autobrev.renderHTML(validAutobrevRequest)
        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(), result.contentType)
        assertEquals(LetterExample.template.letterMetadata, result.letterMetadata)
    }

    @Test
    fun `fails renderPDF with invalid letterData`(): Unit =
        runBlocking {
            assertThrows<ParseLetterDataException> {
                autobrev.renderPDF(validAutobrevRequest.copy(letterData = RandomLetterdata(true)))
            }
        }

    @Test
    fun `fails renderHTML with invalid letterData`() {
        assertThrows<ParseLetterDataException> {
            autobrev.renderHTML(validAutobrevRequest.copy(letterData = RandomLetterdata(true)))
        }
    }

    @Test
    fun `renderHTML redigertBrev uses letterMarkup from argument and includes attachments`() {
        val result = String(redigerbar.renderHTML(validRedigertBrevRequest).file)
        val anAttachmentTitle =
            Letter2Markup.renderAttachmentsOnly(
                validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
                EksempelbrevRedigerbart.template,
            ).first().title.joinToString { it.text }

        assertThat(result, containsSubstring(validRedigertBrevRequest.letterMarkup.title))

        assertThat(result, containsSubstring(anAttachmentTitle))
    }
}

data class RandomLetterdata(val v1: Boolean) : BrevbakerBrevdata
