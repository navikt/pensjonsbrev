package no.nav.pensjon.brev.locust

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.TestTags.MANUAL_TEST
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.time.LocalDate
import kotlin.io.path.createParentDirectories
import kotlin.io.path.writeText

/**
 * Genererer JSON-payloaden som locustfile.py poster mot brevbaker sin
 * `/letter/autobrev/pdf`-route. Datamodellene endrer seg over tid og en
 * håndskrevet JSON-fil blir fort utdatert (felter omdøpt, value classes
 * unwrap'es osv.). Denne testen produserer en frisk payload via samme
 * Jackson-konfig som brevbaker selv bruker, slik at det vi sender garantert
 * matcher det API-en forventer.
 *
 * Tagget som [MANUAL_TEST] fordi den endrer en fil under versjonskontroll
 * og ikke skal kjøre som del av vanlig CI/test-pipeline. Kjør manuelt med:
 *
 *   ./gradlew :pensjon:brevbaker:manualTest --tests "*GenerateLocustPayloadTest*"
 *
 * eller fra IDE.
 */
class GenerateLocustPayloadTest {

    @Test
    @Tag(MANUAL_TEST)
    fun `generer autobrev_request_json for locust`() {
        val request = BestillBrevRequest(
            kode = Pesysbrevkoder.AutoBrev.UT_UNG_UFOER_20_AAR_AUTO,
            letterData = createUngUfoerAutoDto(),
            felles = FellesFactory.fellesAuto,
            language = LanguageCode.BOKMAL,
        )

        val mapper = brevbakerJacksonObjectMapper().writerWithDefaultPrettyPrinter()
        val outputPath = Path.of("..", "..", "brevbaker", "locust", "autobrev_request.json")
            .toAbsolutePath()
            .normalize()

        outputPath.createParentDirectories()
        outputPath.writeText(mapper.writeValueAsString(request) + "\n")

        println("Skrev locust-payload til $outputPath")
    }

    private fun createUngUfoerAutoDto() = UngUfoerAutoDto(
        kravVirkningFraOgMed = LocalDate.of(2023, 10, 9),
        totaltUfoerePerMnd = Kroner(9_000),
        ektefelle = null,
        gjenlevende = UngUfoerAutoDto.InnvilgetTillegg(utbetalt = true),
        fellesbarn = UngUfoerAutoDto.Barnetillegg(
            utbetalt = false,
            inntektstak = Kroner(10_000),
            gjelderFlereBarn = false,
        ),
        saerkullsbarn = UngUfoerAutoDto.Barnetillegg(
            utbetalt = true,
            inntektstak = Kroner(10_000),
            gjelderFlereBarn = true,
        ),
        minsteytelseVedVirkSats = 2.91,
        maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
            ufoeretrygdPerioder = listOf(
                MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned(
                    annetBelop = Kroner(0),
                    barnetilleggSaerkullsbarnBrutto = Kroner(3),
                    barnetilleggSaerkullsbarnNetto = Kroner(4),
                    barnetilleggFellesbarnBrutto = Kroner(1),
                    barnetilleggFellesbarnNetto = Kroner(2),
                    gjenlevendetilleggBrutto = Kroner(10),
                    gjenlevendetilleggNetto = Kroner(11),
                    ektefelletilleggBrutto = Kroner(6),
                    ektefelletilleggNetto = Kroner(7),
                    dekningFasteUtgifter = Kroner(5),
                    erAvkortet = true,
                    garantitilleggNordisk27Brutto = Kroner(8),
                    garantitilleggNordisk27Netto = Kroner(9),
                    grunnbeloep = Kroner(12),
                    ordinaerUTBeloepBrutto = Kroner(13),
                    ordinaerUTBeloepNetto = Kroner(14),
                    totalUTBeloepBrutto = Kroner(15),
                    totalUTBeloepNetto = Kroner(16),
                    virkningFraOgMed = LocalDate.of(2020, 1, 1),
                    virkningTilOgMed = LocalDate.of(2020, 1, 2),
                ),
            ),
        ),
        orienteringOmRettigheterUfoere = OrienteringOmRettigheterUfoereDto(
            bruker_borINorge = true,
            harTilleggForFlereBarn = false,
            harInnvilgetBarnetilleggFellesBarn = false,
            harInnvilgetBarnetilleggSaerkullsbarn = false,
            institusjon_gjeldende = Institusjon.INGEN,
        ),
    )
}

