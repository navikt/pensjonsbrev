package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PDFCompilationOutput
import no.nav.pensjon.brev.maler.OpphoerBarnetilleggAuto
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.render.DocumentFile
import no.nav.pensjon.brev.template.render.LatexDocument
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
    private val latexMock = mockk<LaTeXCompilerService> {
        coEvery { producePDF(any()) } returns PDFCompilationOutput(base64PDF)
    }
    private val autobrev = TemplateResource("autobrev", ProductionTemplates.hentAutobrevmaler(), latexMock)
    private val redigerbar = TemplateResource("autobrev", ProductionTemplates.hentRedigerbareMaler(), latexMock)

    private val validAutobrevRequest = BestillBrevRequest(
        Brevkode.AutoBrev.UT_OPPHOER_BT_AUTO,
        Fixtures.create<OpphoerBarnetilleggAutoDto>(),
        Fixtures.fellesAuto,
        LanguageCode.BOKMAL
    )
    private val validRedigertBrevRequest = BestillRedigertBrevRequest(
        Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
        Fixtures.create<InformasjonOmSaksbehandlingstidDto>(),
        Fixtures.felles,
        LanguageCode.BOKMAL,
        LetterMarkup(
            "redigert markup",
            LetterMarkup.Sakspart(
                "gjelder bruker",
                "123abc",
                "001",
                "en dato"
            ),
            emptyList(),
            LetterMarkup.Signatur(
                "hilsen oss",
                "en rolle",
                "Saksbehandlersen",
                null,
                "Akersgata"
            )
        )
    )

    @Test
    fun `can renderPDF with valid letterData`(): Unit = runBlocking {
        val result = autobrev.renderPDF(validAutobrevRequest)
        assertEquals(
            LetterResponse(pdfInnhold.toByteArray(), ContentType.Application.Pdf.toString(), OpphoerBarnetilleggAuto.template.letterMetadata),
            result
        )
    }

    @Test
    fun `can renderHTML with valid letterData`() {
        val result = autobrev.renderHTML(validAutobrevRequest)
        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(), result.contentType)
        assertEquals(OpphoerBarnetilleggAuto.template.letterMetadata, result.letterMetadata)
    }

    @Test
    fun `fails renderPDF with invalid letterData`(): Unit = runBlocking {
        assertThrows<ParseLetterDataException> {
            autobrev.renderPDF(validAutobrevRequest.copy(letterData = Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>()))
        }
    }

    @Test
    fun `fails renderHTML with invalid letterData`() {
        assertThrows<ParseLetterDataException> {
            autobrev.renderHTML(validAutobrevRequest.copy(letterData = Fixtures.create<ForhaandsvarselEtteroppgjoerUfoeretrygdDto>()))
        }
    }

    @Test
    fun `renderHTML redigertBrev uses letterMarkup from argument and includes attachments`() {
        val result = String(redigerbar.renderHTML(validRedigertBrevRequest).file)
        val anAttachmentTitle = Letter2Markup.renderAttachmentsOnly(
            validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
            InformasjonOmSaksbehandlingstid.template
        ).firstOrNull()?.title?.joinToString { it.text }

        assertThat(result, containsSubstring(validRedigertBrevRequest.letterMarkup.title))

        // TODO: Vi har ingen redigerbare maler med vedlegg, if kan fjernes når vi har en mal med vedlegg.
        if (anAttachmentTitle != null) {
            assertThat(result, containsSubstring(anAttachmentTitle))
        }
    }

    @Test
    fun `renderPDF redigertBrev uses letterMarkup from argument and includes attachments`() = runBlocking {
        val anAttachment = Letter2Markup.renderAttachmentsOnly(
            validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
            InformasjonOmSaksbehandlingstid.template
        ).firstOrNull()

        val capturedLatex = slot<LatexDocument>()
        redigerbar.renderPDF(validRedigertBrevRequest)
        coVerify { latexMock.producePDF(capture(capturedLatex)) }

        val letterLatexContent = capturedLatex.captured.files.filterIsInstance<DocumentFile.PlainText>().first { it.fileName == "letter.tex" }.content
        assertThat(
            letterLatexContent,
            containsSubstring(validRedigertBrevRequest.letterMarkup.title)
        )

        // TODO: Vi har ingen redigerbare maler med vedlegg, if kan fjernes når vi har en mal med vedlegg.
        if (anAttachment != null) {
            assertThat(letterLatexContent, containsSubstring(anAttachment.title.joinToString { it.text }))
        }
    }

}