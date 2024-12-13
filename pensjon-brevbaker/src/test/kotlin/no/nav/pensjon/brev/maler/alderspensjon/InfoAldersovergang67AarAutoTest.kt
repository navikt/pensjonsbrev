package no.nav.pensjon.brev.maler.alderspensjon

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class InfoAldersovergang67AarAutoTest {

    @Test
    fun testPdfNB() {
        Letter(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }

    @Test
    fun testPdfNN() {
        Letter(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_NYNORSK")
    }

    @Test
    fun testPdfEN() {
        Letter(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_ENGLISH")
    }


    @Test
    fun testHtml() {
        Letter(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }
}