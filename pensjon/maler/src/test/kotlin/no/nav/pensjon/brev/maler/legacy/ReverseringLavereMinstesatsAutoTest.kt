package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.LopendeYtelse
import no.nav.pensjon.brev.api.model.maler.legacy.OpphortYtelse
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.MANUAL_TEST)
class ReverseringLavereMinstesatsAutoTest {

    /**
     * Ikke opphørt sak: viser tabellen "Dette er din uføretrygd", barnetillegg, gjenlevendetillegg,
     * avkortetPgaRedusertTrygdetid, harGradertUfoeretrygd, og "Endring i barnetillegg"-seksjonen.
     */
    private fun ikkeOpphoerDto() =
        ReverseringLavereMinstesatsAutoDto(
            data = ReverseringLavereMinstesatsDto(
                opphortYtelse = OpphortYtelse(
                    opphorsdato = LocalDate.of(2026, 10, 1)
                ),
                lopendeYtelse = LopendeYtelse(
                    nettoTotal = Kroner(32000),
                    nettoUforetrygd = Kroner(26000),
                    nettoBarnetillegg = Kroner(4000),
                    nettoGjenlevendetillegg = Kroner(2000),
                    reduksjonsprosent = 50.0,
                    brukersMinstesats = 300000.0,
                    avkortetPgaRedusertTrygdetid = true,
                    harGradertUfoeretrygd = true
                ),
                etterbetaling = Kroner(10000),
                hjemmeltekst = "§§ 12-13 til 12-16, 12-18 og 22-12",
                pe = createPEgruppe10(),
                maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
                orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
            )
        )

    /**
     * Opphørt sak: viser opphørs-spesifikk tekst med opphorsdato, "Etteroppgjør"-seksjon,
     * og nettoBarnetillegg = null (skjuler "Endring i barnetillegg").
     */
    private fun opphoerDto() =
        ReverseringLavereMinstesatsAutoDto(
            data = ReverseringLavereMinstesatsDto(
                lopendeYtelse = null,
                opphortYtelse = OpphortYtelse(
                    opphorsdato = LocalDate.of(2026, 8, 15)
                ),
                etterbetaling = Kroner(5000),
                hjemmeltekst = "§§ 12-13 til 12-16, 12-18 og 22-12",
                pe = createPEgruppe10(),
                maanedligUfoeretrygdFoerSkatt = null,
                orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
            )
        )

    @Test
    fun `ikke opphoer - bokmaal - pdf`() {
        LetterTestImpl(
            ReverseringLavereMinstesatsAuto.template,
            ikkeOpphoerDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("reverseringLavereMinstesatsAuto_ikkeOpphoer")
    }

    @Test
    fun `ikke opphoer - bokmaal - html`() {
        LetterTestImpl(
            ReverseringLavereMinstesatsAuto.template,
            ikkeOpphoerDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("reverseringLavereMinstesatsAuto_ikkeOpphoer")
    }

    @Test
    fun `opphoer - bokmaal - pdf`() {
        LetterTestImpl(
            ReverseringLavereMinstesatsAuto.template,
            opphoerDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("reverseringLavereMinstesatsAuto_opphoer")
    }

    @Test
    fun `opphoer - bokmaal - html`() {
        LetterTestImpl(
            ReverseringLavereMinstesatsAuto.template,
            opphoerDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("reverseringLavereMinstesatsAuto_opphoer")
    }
}
