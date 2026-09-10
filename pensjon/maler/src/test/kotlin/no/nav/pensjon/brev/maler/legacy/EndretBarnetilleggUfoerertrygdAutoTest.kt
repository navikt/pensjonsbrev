package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.barnetillegg.EndretBarnetilleggUfoerertrygdAuto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class EndretBarnetilleggUfoerertrygdAutoTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndretBarnetilleggUfoerertrygdAuto.template,
            Fixtures.create(EndretBarnetilleggUfoerertrygdAuto::class),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRET_BARNETILLEGG")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndretBarnetilleggUfoerertrygdAuto.template,
            Fixtures.create(EndretBarnetilleggUfoerertrygdAuto::class),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRET_BARNETILLEGG")
    }
}