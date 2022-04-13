package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligeUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligeUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.maanedligeUfoeretrygdFoerSkatt
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
class MaanedligeUfoeretrygdFoerSkatt {

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

            val ufoeretrygdGjeldende = UfoeretrygdPerMaaned(
                annetBelop = 1503,
                barnetillegg = 2503,
                dekningFasteUtgifter = 1503,
                garantitilleggNordisk27 = 9003,
                grunnbeloep = 91543,
                ordinaerUTBeloep = 15003,
                totalUTBeloep = 5503,
                virkningFraOgMed = LocalDate.of(2017, 2, 3),
                virkningTilOgMed = LocalDate.of(2017, 5, 5),
                avkortning = null
            )

            val ufoeretrygdPerMaaned = UfoeretrygdPerMaaned(
                annetBelop = 1500,
                barnetillegg = 2500,
                dekningFasteUtgifter = 1500,
                garantitilleggNordisk27 = 9000,
                grunnbeloep = 91540,
                ordinaerUTBeloep = 15000,
                totalUTBeloep = 5500,
                virkningFraOgMed = LocalDate.of(2017, 2, 3),
                virkningTilOgMed = LocalDate.of(2017, 5, 5),
                avkortning = UfoeretrygdPerMaaned.Avkortning(
                    barnetilleggFoerAvkort = 2800,
                    garantitilleggNordisk27FoerAvkort = 9999,
                    ordinaerUTBeloepFoerAvkort = 12000,
                    totalUTBeloepFoerAvkort = 6600,
                )
            )

            includeAttachment(
                maanedligeUfoeretrygdFoerSkatt,
                MaanedligeUfoeretrygdFoerSkattDto(
                    ufoeretrygdGjeldende,
                    LocalDate.now(),
                    2,
                    listOf(
                        ufoeretrygdPerMaaned,
                        ufoeretrygdPerMaaned.copy(avkortning = null),
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
            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO", it) }
    }
}