package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.VISUAL_TEST)
class OpphoerBarnetilleggAutoTest {

    @Test
    fun test() {
        Letter(
            OpphoerBarnetilleggAuto.template,
            Fixtures.create<OpphoerBarnetilleggAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("UT_OPPHOERER_BARNETILLEGG")
    }
}
