package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class UfoeretrygdEndretPgaInntektITest {

    @Test
    fun testPdf() {
        Letter(
            UfoeretrygdEndretPgaInntekt.template,
            Fixtures.create<UfoeretrygdEndretPgaInntektDto>().copy(
                virkningFraOgMed = LocalDate.of(2020,1,1),
                harInnvilgetBarnetilleggSaerkullsBarn = true,
                harInnvilgetBarnetilleggFellesBarn = true,
                beloepGammelBarnetillegFellesBarn = Kroner(0),
                beloepNyBarnetillegFellesBarn = Kroner(10),
                beloepGammelUfoeretrygd = Kroner(110),
                beloepNyUfoeretrygd = Kroner(10),
                antallSaerkullsbarn = 5
            ),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UFOER_ENDRET_PGA_INNTEKT", it) }
    }
}