package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AdhocTest {
    fun testHtml(template: LetterTemplate<*, *>, htmlName: String, vararg language: Language) {
        language.forEach {
            Letter(template, Unit, it, Fixtures.fellesAuto).renderTestHtml(htmlName + "_${it}")
        }
    }

    fun testAdhocPdf(template: LetterTemplate<*, *>, pdfName: String, vararg language: Language) {
        language.forEach {
            Letter(template, Unit, it, Fixtures.fellesAuto).renderTestPDF(pdfName + "_${it}")
        }
    }

    @Test
    fun `testAdhocUfoeretrygdVarselOpphoerEktefelletillegg pdf`() {
        testAdhocPdf(
            AdhocUfoeretrygdVarselOpphoerEktefelletillegg.template,
            pdfName = "UT_VARSEL_OPPHOER_EKTEFELLETILLEGG",
            Bokmal
        )
    }

    @Test
    fun `testAdhocGjenlevendEtter1970 pdf`() {
        testAdhocPdf(
            AdhocGjenlevendEtter1970.template,
            pdfName = "PE_ADHOC_2024_VEDTAK_GJENLEVENDETTER1970",
            Bokmal, Nynorsk, English
        )
    }

    @Test
    fun `testAdhocUfoeretrygdEtterbatalingDagpenger pdf`() {
        testAdhocPdf(
            AdhocUfoeretrygdEtterbetalingDagpenger.template,
            pdfName = "UT_ADHOC_UFOERETRYGD_ETTERBETALING_DAGPENGER",
            Bokmal
        )
    }

    @Test
    fun `testAdhocUfoeretrygdKombiDagpenger pdf`() {
        testAdhocPdf(
            AdhocUfoeretrygdKombiDagpenger.template,
            pdfName = "UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER",
            Bokmal
        )
    }

    @Test
    fun `testAdhocUfoeretrygdKombiDagpengerInntektsavkorting pdf`() {
        testAdhocPdf(
            AdhocUfoeretrygdKombiDagpengerInntektsavkorting.template,
            pdfName = "UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER_AVKORTNING",
            Bokmal
        )
    }

    @Test
    fun `testAdhocVarselOpphoerMedHvilendeRett pdf`() {
        testAdhocPdf(
            AdhocVarselOpphoerMedHvilendeRett.template,
            pdfName = "UT_ADHOC_VARSEL_OPPHOER_MED_HVILENDE_RETT",
            Bokmal
        )
    }

    @Test
    fun `testAdhocAlderspensjonFraFolketrygden pdf`() {
        testAdhocPdf(
            AdhocAlderspensjonFraFolketrygden.template,
            pdfName = "PE_AP_ADHOC_2024_REGLERENDRET_GJR_AP_MNTINDV",
            Bokmal,
            Nynorsk,
            English
        )
    }

    @Test
    fun `testAdhocFeilUtsendingAvGjenlevenderett pdf`() {
        testAdhocPdf(
            FeilUtsendingAvGjenlevenderett.template,
            pdfName = "PE_2024_FEIL_INFOBREV_AP_SENDT_BRUKER",
            Bokmal,
            Nynorsk,
            English
        )
    }
}