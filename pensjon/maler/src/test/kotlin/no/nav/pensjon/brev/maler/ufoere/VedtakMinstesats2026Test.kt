package no.nav.pensjon.brev.maler.ufoere

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VedtakMinstesats2026Dto
import no.nav.pensjon.brev.maler.ufoereBrev.VedtakMinstesats2026
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakMinstesats2026Test {

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakMinstesats2026.template,
            Fixtures.create<VedtakMinstesats2026Dto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_VEDTAK_MINSTESATS_2026")
    }

}