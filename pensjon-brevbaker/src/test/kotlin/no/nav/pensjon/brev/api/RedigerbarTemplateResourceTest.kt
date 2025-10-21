package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.Testmaler
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RedigerbarTemplateResourceTest {
    private val pdfInnhold = "generert redigerbar pdf"
    private val pdf = pdfInnhold.encodeToByteArray()
    private val fakePDFBygger = object : PDFByggerService {
        override suspend fun producePDF(
            pdfRequest: PDFRequest,
            path: String,
            shouldRetry: Boolean
        ): PDFCompilationOutput = PDFCompilationOutput(pdf)
    }

    private val redigerbar = RedigerbarTemplateResource("autobrev", Testmaler.hentRedigerbareMaler(), fakePDFBygger)

    private val validRedigertBrevRequest = BestillRedigertBrevRequest(
        EksempelbrevRedigerbart.kode,
        createEksempelbrevRedigerbartDto(),
        Fixtures.felles,
        LanguageCode.BOKMAL,
        LetterMarkupImpl(
            title = listOf(LiteralImpl(1, "redigert markup")),
            sakspart = LetterMarkupImpl.SakspartImpl(
                gjelderNavn = "gjelder bruker",
                gjelderFoedselsnummer = Foedselsnummer("123abc"),
                annenMottakerNavn = null,
                saksnummer = "001",
                dokumentDato = LocalDate.now()
            ),
            blocks = emptyList(),
            signatur = LetterMarkupImpl.SignaturImpl(
                hilsenTekst = "hilsen oss",
                saksbehandlerNavn = "Saksbehandlersen",
                attesterendeSaksbehandlerNavn = null,
                navAvsenderEnhet = "Akersgata"
            )
        )
    )

    @Test
    fun `renderHTML redigertBrev uses letterMarkup from argument and includes attachments`() {
        val result = String(redigerbar.renderHTML(validRedigertBrevRequest).file)
        val letterTitle = validRedigertBrevRequest.letterMarkup.title.joinToString("") { it.text }
        val anAttachmentTitle = LetterTestRenderer.renderAttachmentsOnly(
            validRedigertBrevRequest.let { ExpressionScope(it.letterData, it.felles, Language.Bokmal) },
            EksempelbrevRedigerbart.template
        ).first().title.joinToString { it.text }

        assertThat(result, containsSubstring(letterTitle))

        assertThat(result, containsSubstring(anAttachmentTitle))
    }
}