package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.Testmaler
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RedigerbarTemplateResourceTest {
    private val pdfInnhold = "generert redigerbar pdf"
    private val pdf = pdfInnhold.toByteArray()
    private val latexMock = mockk<LaTeXCompilerService> {
        coEvery { producePDF(any(), any()) } returns PDFCompilationOutput(pdf)
    }
    private val redigerbar = RedigerbarTemplateResource("autobrev", Testmaler.hentRedigerbareMaler(), latexMock)

    private val validRedigertBrevRequest = BestillRedigertBrevRequest(
        EksempelbrevRedigerbart.kode,
        createEksempelbrevRedigerbartDto(),
        Fixtures.felles,
        LanguageCode.BOKMAL,
        LetterMarkupImpl(
            title = "redigert markup",
            sakspart = LetterMarkupImpl.SakspartImpl(
                gjelderNavn = "gjelder bruker",
                gjelderFoedselsnummer = "123abc",
                vergeNavn = null,
                saksnummer = "001",
                dokumentDato = LocalDate.now()
            ),
            blocks = emptyList(),
            signatur = LetterMarkupImpl.SignaturImpl(
                hilsenTekst = "hilsen oss",
                saksbehandlerRolleTekst = "en rolle",
                saksbehandlerNavn = "Saksbehandlersen",
                attesterendeSaksbehandlerNavn = null,
                navAvsenderEnhet = "Akersgata"
            )
        )
    )

    @Test
    fun `renderHTML redigertBrev uses letterMarkup from argument and includes attachments`() {
        val result = String(redigerbar.renderHTML(validRedigertBrevRequest).file)
        val anAttachmentTitle = LetterTestRenderer.renderAttachmentsOnly(
            validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
            EksempelbrevRedigerbart.template
        ).first().title.joinToString { it.text }

        assertThat(result, containsSubstring(validRedigertBrevRequest.letterMarkup.title))

        assertThat(result, containsSubstring(anAttachmentTitle))
    }
}