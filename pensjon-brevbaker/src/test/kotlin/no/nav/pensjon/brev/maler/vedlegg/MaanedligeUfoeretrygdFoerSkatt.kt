package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class MaanedligUfoeretrygdFoerSkatt {

    @Test
    fun testVedlegg() {
        val template = createTemplate(
            name = "test-template",
            base = PensjonLatex,
            letterDataType = Unit::class,
            title = newText(Bokmal to ""),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false
            ),
        ) {
            outline {

            }

            val ufoeretrygdPerMaaned =
                UfoeretrygdPerMaaned(
                    annetBelop = 1500,
                    barnetillegg = UfoeretrygdPerMaaned.Beloep(2500, 2800),
                    dekningFasteUtgifter = 1500,
                    garantitilleggNordisk27 = UfoeretrygdPerMaaned.Beloep(9000, 9999),
                    grunnbeloep = 91540,
                    ordinaerUTBeloep = UfoeretrygdPerMaaned.Beloep(15000, 12000),
                    totalUTBeloep = UfoeretrygdPerMaaned.Beloep(5500, 6600),
                    virkningFraOgMed = LocalDate.of(2017, 2, 3),
                    virkningTilOgMed = LocalDate.of(2017, 5, 5),
                    erAvkortet = true
                )

            includeAttachment(
                maanedligUfoeretrygdFoerSkatt,
                MaanedligUfoeretrygdFoerSkattDto(
                    ufoeretrygdPerMaaned,
                    LocalDate.of(2020,1,1),
                    2,
                    listOf(
                        ufoeretrygdPerMaaned,
                        ufoeretrygdPerMaaned.copy(erAvkortet = false),
                    )
                ).expr()
            )
        }

        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("MaanedligUfoeretrygdFoerSkatt", it) }
    }
}