package no.nav.pensjon.brev.maler.alder

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class InfoAldersovergang67AarAutoTest {

    @Test
    fun testPdfNB() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }

    @Test
    fun testPdfNN() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_NYNORSK")
    }

    @Test
    fun testPdfEN() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_ENGLISH")
    }


    @Test
    fun testHtml() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }
}