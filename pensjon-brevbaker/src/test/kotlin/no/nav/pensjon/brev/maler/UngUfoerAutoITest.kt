package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.newText
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class UngUfoerAutoITest {

    @Test
    fun test() {
        Letter(
            UngUfoerAuto.template,
            UngUfoerAutoDto(
                kravVirkningFraOgMed = LocalDate.of(2022, 1, 1),
                totaltUfoerePerMnd = 9000,
                ektefelle = UngUfoerAutoDto.InnvilgetTillegg(true),
                gjenlevende = null,
                saerkullsbarn = null, //UngUfoerAutoDto.InnvilgetBarnetillegg(true, 2, 10_000),
                fellesbarn = null, //UngUfoerAutoDto.InnvilgetBarnetillegg(false, 1, 10_000),
                minsteytelseVedVirkSats = 2.91,
            ),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UNG_UFOER_AUTO_BOKMAL", it) }
    }

}