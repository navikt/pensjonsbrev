package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
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
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false
            ),
        ) {
            title{
                text(Bokmal to "tittel")
            }
            outline {

            }

            val ufoeretrygdPerMaaned =
                UfoeretrygdPerMaaned(
                    annetBelop = Kroner(1500),
                    barnetillegg = UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(2500), Kroner(2800)),
                    dekningFasteUtgifter = Kroner(1500),
                    garantitilleggNordisk27 = UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(9000), Kroner(9999)),
                    grunnbeloep = Kroner(91540),
                    ordinaerUTBeloep = UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(15000), Kroner(12000)),
                    totalUTBeloep = UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(5500), Kroner(6600)),
                    virkningFraOgMed = LocalDate.of(2017, 2, 3),
                    virkningTilOgMed = LocalDate.of(2017, 5, 5),
                    erAvkortet = true
                )

            includeAttachment(
                maanedligUfoeretrygdFoerSkatt,
                MaanedligUfoeretrygdFoerSkattDto(
                    ufoeretrygdPerMaaned,
                    LocalDate.of(2020,1,1),
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