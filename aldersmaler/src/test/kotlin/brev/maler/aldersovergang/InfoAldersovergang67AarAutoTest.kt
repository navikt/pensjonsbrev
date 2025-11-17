package no.nav.pensjon.brev.maler.aldersovergang

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.aldersovergang.InfoAldersovergang67AarAuto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InfoAldersovergang67AarAutoTest {
    @Test
    fun testPdfNB() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }

    @Test
    fun testPdfNN() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_NYNORSK")
    }

    @Test
    fun testPdfEN() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto,
        ).renderTestPDF("INFO_ALDERSOVERGANG_67_AAR_AUTO_ENGLISH")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InfoAldersovergang67AarAuto.template,
            Fixtures.create<InfoAlderspensjonOvergang67AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestHtml("INFO_ALDERSOVERGANG_67_AAR_AUTO_BOKMAL")
    }
}
