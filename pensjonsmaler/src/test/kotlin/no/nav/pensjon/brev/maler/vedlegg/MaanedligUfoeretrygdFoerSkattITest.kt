package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.Kroner
import org.junit.jupiter.api.*
import java.time.LocalDate

@Tag(TestTags.MANUAL_TEST)
class MaanedligUfoeretrygdFoerSkattITest {
    val ufoeretrygdPerMaaned =
        Fixtures.create(UfoeretrygdPerMaaned::class)
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

    val template =
        createVedleggTestTemplate(
            vedleggMaanedligUfoeretrygdFoerSkatt,
            MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder =
                    listOf(
                        ufoeretrygdPerMaaned.copy(
                            virkningFraOgMed = LocalDate.of(2020, 3, 1),
                            virkningTilOgMed = null,
                        ),
                        ufoeretrygdPerMaaned.copy(
                            virkningFraOgMed = LocalDate.of(2020, 2, 1),
                            virkningTilOgMed = LocalDate.of(2020, 2, 28),
                        ),
                        ufoeretrygdPerMaaned.copy(
                            virkningFraOgMed = LocalDate.of(2020, 1, 1),
                            virkningTilOgMed = LocalDate.of(2020, 1, 31),
                        ),
                    ),
            ).expr(),
            languages(Bokmal, Nynorsk, English),
        )

    @Test
    fun testPdf() {
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("MaanedligUfoeretrygdFoerSkatt")
    }

    @Test
    fun testHtml() {
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto,
        ).renderTestHtml("MaanedligUfoeretrygdFoerSkatt")
    }
}
