package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.UTTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Felles testklasse for brev om endring i IFU (inntektsfradrag for uføretrygd) og reduksjonsprosent fom 1. juli 2026.
 * Dekker tre brevvarianter:
 *  - Økt minste IFU
 *  - Lavere reduksjonsprosent
 *  - Økt minste IFU + lavere reduksjonsprosent
 *
 * Kjør med @Tag(TestTags.MANUAL_TEST) for å generere HTML/PDF i build/test_html / build/test_pdf.
 */
@Tag(TestTags.MANUAL_TEST)
class VedtakOmIFUReduksjonsprosentAutoTest {

    // --- Testdata ---

    /** Maksimal data: alle tillegg, alle endringer, med barnetillegg og månedlig utbetaling. */
    private fun fullDataDto() =
        VedtakOmIFUReduksjonsprosentAutoDto(
            vedtakData = VedtakOmIFUReduksjonsprosentData(
                nettoUforetrygdUtenTillegg = Kroner(28066),
                nettoBarnetillegg = Kroner(4000),
                totalbelop = Kroner(33527),
                etterbetalingJuli = Kroner(1500),
                reduksjonsprosent = 62.65,
                inntektstak = Kroner(404174),
                ifu = Kroner(53886),
                tillegg = listOf(UTTillegg.BT),
                endringNettoUforetrygdUtenTillegg = true,
                endringNettoBarnetillegg = true,
                endringInntektstak = true,
                erInntektsavkortet = false,
                hjemler = setOf("12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14", "22-12"),
                pe = createPEgruppe10(),
                maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
                orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
            )
        )

    /** Data med inntektsavkorting aktivert. */
    private fun inntektsavkortetDto() =
        VedtakOmIFUReduksjonsprosentAutoDto(
            vedtakData = VedtakOmIFUReduksjonsprosentData(
                nettoUforetrygdUtenTillegg = Kroner(20000),
                nettoBarnetillegg = Kroner(3000),
                totalbelop = Kroner(23000),
                etterbetalingJuli = Kroner(800),
                reduksjonsprosent = 62.65,
                inntektstak = Kroner(350000),
                ifu = Kroner(40000),
                tillegg = listOf(UTTillegg.BT),
                endringNettoUforetrygdUtenTillegg = true,
                endringNettoBarnetillegg = true,
                endringInntektstak = true,
                erInntektsavkortet = true,
                hjemler = setOf("12-8", "12-13", "12-16", "22-12"),
                pe = createPEgruppe10(),
                maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
                orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
            )
        )

    // --- VedtakOmOktMinsteIFUAuto (visOktMinsteIFU=true, visReduksjonsprosent=false) ---

    @Test
    fun `OktMinsteIFU - full data - bokmål - HTML`() {
        LetterTestImpl(
            VedtakOmOktMinsteIFUAuto.template,
            fullDataDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OKT_MINSTE_IFU_FULL_BM")
    }

    @Test
    fun `OktMinsteIFU - full data - nynorsk - HTML`() {
        LetterTestImpl(
            VedtakOmOktMinsteIFUAuto.template,
            fullDataDto(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("OKT_MINSTE_IFU_FULL_NN")
    }

    @Test
    fun `LavereReduksjonsprosent - inntektsavkortet - bokmål - HTML`() {
        LetterTestImpl(
            VedtakOmLavereReduksjonsprosentAuto.template,
            inntektsavkortetDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("LAVERE_REDUKSJONSPROSENT_AVKORTET_BM")
    }

    @Test
    fun `LavereReduksjonsprosent - inntektsavkortet - nynorsk - HTML`() {
        LetterTestImpl(
            VedtakOmLavereReduksjonsprosentAuto.template,
            inntektsavkortetDto(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("LAVERE_REDUKSJONSPROSENT_AVKORTET_NN")
    }

    @Test
    fun `OktMinsteIFULavereReduksjonsprosent - full data - bokmål - HTML`() {
        LetterTestImpl(
            VedtakOmOktMinsteIFULavereReduksjonsprosentAuto.template,
            fullDataDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OKT_MINSTE_IFU_LAVERE_REDUKSJON_FULL_BM")
    }

    @Test
    fun `OktMinsteIFULavereReduksjonsprosent - full data - nynorsk - HTML`() {
        LetterTestImpl(
            VedtakOmOktMinsteIFULavereReduksjonsprosentAuto.template,
            fullDataDto(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("OKT_MINSTE_IFU_LAVERE_REDUKSJON_FULL_NN")
    }
}

