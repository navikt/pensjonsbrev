package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import com.typesafe.config.ConfigFactory
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.fagsystem.domain.Tema
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Arkivsak
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Arkivsaksystem
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Bruker
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Foerstesidetype
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideRequest
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideResponse
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Loepenummer
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Postboks
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.httpClientTest
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path

class FoerstesidegeneratorClientTest {

    private val config = ConfigFactory.parseMap(
        mapOf(
            "url" to "http://foerstesidegenerator.test",
            "scope" to "test-scope",
        )
    )

    @Test
    fun `genererer foersteside og lagrer resulterende pdf`() = httpClientTest(
        GenererFoerstesideResponse(
            foersteside = lagPdf("Dette er en generert førsteside"),
            loepenummer = Loepenummer("123456789"),
        )
    ) { engine ->
        val client = FoerstesidegeneratorClient(
            config = config,
            authService = FakeAuthService,
            clientEngine = engine,
        )

        val response = client.genererFoersteside(
            GenererFoerstesideRequest(
                spraakkode = SpraakKode.NB,
                netsPostboks = Postboks("1400"),
                bruker = Bruker(
                    brukerId = BrevbakerType.Pid("12345678910"),
                    brukerType = Bruker.BrukerType.PERSON,
                ),
                tema = Tema("PEN"),
                behandlingstema = null,
                arkivtittel = "Testbrev",
                vedleggsliste = listOf(),
                overskriftstittel = "Testbrev",
                dokumentlisteFoersteside = listOf(),
                foerstesidetype = Foerstesidetype.LOESPOST,
                enhetsnummer = EnhetId("4812"),
                arkivsak = Arkivsak(
                    arkivsaksystem = Arkivsaksystem.PSAK,
                    arkivsaksnummer = SaksId(1234L),
                ),
            )
        )

        assertThat(response.loepenummer).isEqualTo(Loepenummer("123456789"))
        assertThat(response.foersteside).isNotEmpty()

        // Skriver den genererte pdf-en til disk slik at den kan åpnes og inspiseres manuelt
        val outputPath = Path.of("build/test-pdf/foersteside.pdf")
        Files.createDirectories(outputPath.parent)
        Files.write(outputPath, response.foersteside)
    }

    private fun lagPdf(tekst: String): ByteArray =
        PDDocument().use { document ->
            val page = PDPage()
            document.addPage(page)
            PDPageContentStream(document, page).use { content ->
                content.beginText()
                content.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
                content.newLineAtOffset(50f, 700f)
                content.showText(tekst)
                content.endText()
            }
            val output = ByteArrayOutputStream()
            document.save(output)
            return output.toByteArray()
        }
}
