package no.nav.pensjon.brev.maler.vedlegg

import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class MaanedligUfoeretrygdFoerSkattITest {
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

    val template = createVedleggTestTemplate(
        vedleggMaanedligUfoeretrygdFoerSkatt,
        MaanedligUfoeretrygdFoerSkattDto(
            ufoeretrygdPerioder = listOf(
                ufoeretrygdPerMaaned.copy(
                    virkningFraOgMed = LocalDate.of(2020, 3, 1),
                    virkningTilOgMed = null,
                ),
                ufoeretrygdPerMaaned.copy(
                    virkningFraOgMed = LocalDate.of(2020, 2, 1),
                    virkningTilOgMed = LocalDate.of(2020, 2, 28)
                ),
                ufoeretrygdPerMaaned.copy(
                    virkningFraOgMed = LocalDate.of(2020, 1, 1),
                    virkningTilOgMed = LocalDate.of(2020, 1, 31)
                ),
            ),
        ).expr()
    )

    @Test
    fun testPdf() {
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("MaanedligUfoeretrygdFoerSkatt", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("MaanedligUfoeretrygdFoerSkatt", it) }
    }
}