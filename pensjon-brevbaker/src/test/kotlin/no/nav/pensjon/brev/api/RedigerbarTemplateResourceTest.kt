package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.Testmaler
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.junit.jupiter.api.Test
import java.util.Base64

class RedigerbarTemplateResourceTest {
    private val pdfInnhold = "generert redigerbar pdf"
    private val base64PDF = Base64.getEncoder().encodeToString(pdfInnhold.toByteArray())
    private val latexMock = mockk<LaTeXCompilerService> {
        coEvery { producePDF(any()) } returns PDFCompilationOutput(base64PDF)
    }
    private val redigerbar = RedigerbarTemplateResource("autobrev", Testmaler.hentRedigerbareMaler(), latexMock)

    private val validRedigertBrevRequest = BestillRedigertBrevRequest(
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
    fun `renderHTML redigertBrev uses letterMarkup from argument and includes attachments`() {
        val result = String(redigerbar.renderHTML(validRedigertBrevRequest).file)
        val anAttachmentTitle = Letter2Markup.renderAttachmentsOnly(
            validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
            EksempelbrevRedigerbart.template
        ).first().title.joinToString { it.text }

        assertThat(result, containsSubstring(validRedigertBrevRequest.letterMarkup.title))

        assertThat(result, containsSubstring(anAttachmentTitle))
    }
}