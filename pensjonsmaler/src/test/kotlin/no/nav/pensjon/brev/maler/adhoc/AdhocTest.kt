package no.nav.pensjon.brev.maler.adhoc

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDto
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmaktsgiver
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmektig
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AdhocTest {
    fun <T : BrevbakerBrevdata> testHtml(template: LetterTemplate<*, T>, htmlName: String, vararg language: Language) {
        language.forEach {
            LetterTestImpl(template, EmptyAutobrevdata, it, Fixtures.fellesAuto).renderTestHtml(htmlName + "_${it}")
        }
    }

    fun <T : BrevbakerBrevdata> testAdhocPdf(template: LetterTemplate<*, T>, pdfName: String, vararg language: Language) {
        language.forEach {
            LetterTestImpl(template, EmptyAutobrevdata, it, Fixtures.fellesAuto).renderTestPDF(pdfName + "_${it}")
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
    fun `test adhoc hvilende rett 4 aar pdf`() {
        testAdhocPdf(
            AdhocInformasjonHvilendeRett4Aar.template,
            pdfName = "PE_UT_ADHOC_2024_INFO_HVILENDE_RETT_4_AAR",
            Bokmal
        )
    }

    @Test
    fun `test adhoc hvilende rett 10 aar pdf`() {
        testAdhocPdf(
            AdhocMidlertidigOpphoerHvilenderett10Aar.template,
            pdfName = "PE_UT_ADHOC_2024_MIDL_OPPHOER_HVILENDE_RETT_10_AAR",
            Bokmal
        )
    }

    @Test
    fun `test adhoc hvilende rett 10 aar varsel pdf`() {
        testAdhocPdf(
            AdhocVarselOpphoerMedHvilendeRett.template,
            pdfName = "UT_ADHOC_VARSEL_OPPHOER_MED_HVILENDE_RETT",
            Bokmal
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
    fun `testAdHocVarselUgyldiggjoringFullmaktsgiver pdf`() {
        LetterTestImpl(
            AdHocVarselUgyldiggjoringFullmaktsgiver.template,
            Fixtures.create<FullmaktsgiverBprofAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(AdHocVarselUgyldiggjoringFullmaktsgiver.kode.name)


    }

    @Test
    fun `testAdHocVarselUgyldiggjoringFullmektig pdf`() {
        LetterTestImpl(
            AdHocVarselUgyldiggjoringFullmektig.template,
            Fixtures.create<FullmektigBprofAutoDto>(),
            Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(AdHocVarselUgyldiggjoringFullmektig.kode.name)

    }

}