package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class InfoAldersovergang67AarAutoTest {

    @Test
    fun testPdfNB() {
        renderTestPDF(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL"
        )
    }

    @Test
    fun testPdfNN() {
        renderTestPDF(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            "INFO_ALDERSOVERGANG_67_AAR_AUTO_NYNORSK"
        )
    }

    @Test
    fun testPdfEN() {
        renderTestPDF(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto,
            "INFO_ALDERSOVERGANG_67_AAR_AUTO_ENGLISH"
        )
    }


    @Test
    fun testHtml() {
        renderTestHtml(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL"
        )
    }
}