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
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class MaanedligUfoeretrygdFoerSkattITest {

    @Test
    fun testVedlegg() {
        val template = createTemplate(
            name = "test-template",
            letterDataType = Unit::class,
            languages = languages(Language.Bokmal, Language.Nynorsk,Language.English),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            ),
        ) {
            title {
                text(
                    Language.Bokmal to "tittel",
                        Language.Nynorsk to "tittel",
                        Language.English to "tittel",
                )
            }
            outline {

            }

            val ufoeretrygdPerMaaned = Fixtures.create(UfoeretrygdPerMaaned::class)
                .copy(
                    annetBelop = Kroner(1),
                    barnetilleggFellesbarnBrutto = Kroner(2),
                    barnetilleggFellesbarnNetto = Kroner(3000),
                    barnetilleggSaerkullsbarnBrutto = Kroner(4),
                    barnetilleggSaerkullsbarnNetto = Kroner(5),
                    dekningFasteUtgifter = Kroner(6),
                    ektefelletilleggBrutto = Kroner(7),
                    ektefelletilleggNetto = Kroner(8),
                    garantitilleggNordisk27Brutto = Kroner(10),
                    garantitilleggNordisk27Netto = Kroner(11),
                    gjenlevendetilleggBrutto = Kroner(12),
                    gjenlevendetilleggNetto = Kroner(13),
                    grunnbeloep = Kroner(14),
                    ordinaerUTBeloepBrutto = Kroner(15),
                    ordinaerUTBeloepNetto = Kroner(16),
                    totalUTBeloepBrutto = Kroner(17),
                    totalUTBeloepNetto = Kroner(18),
                )

            includeAttachment(
                vedleggMaanedligUfoeretrygdFoerSkatt,
                MaanedligUfoeretrygdFoerSkattDto(
                    ufoeretrygdPerMaaned,
                    LocalDate.of(2020, 1, 1),
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
            Language.English,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("MaanedligUfoeretrygdFoerSkatt", it) }
    }
}