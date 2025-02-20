package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpphoerBarnetilleggAutoTest {

    @Test
    fun test() {
        LetterTestImpl(
            OpphoerBarnetilleggAuto.template,
            Fixtures.create<OpphoerBarnetilleggAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("UT_OPPHOERER_BARNETILLEGG")
    }
}
