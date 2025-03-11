package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class EndretBarnetilleggUfoerertrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndretBarnetilleggUfoerertrygd.template,
            Fixtures.create<EndretBarnetilleggUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRET_BARNETILLEGG")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndretBarnetilleggUfoerertrygd.template,
            Fixtures.create<EndretBarnetilleggUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRET_BARNETILLEGG")
    }
}