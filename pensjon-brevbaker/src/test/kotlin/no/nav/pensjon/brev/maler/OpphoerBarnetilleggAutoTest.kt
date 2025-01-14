package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpphoerBarnetilleggAutoTest {

    @Test
    fun test() {
        renderTestPDF(
            OpphoerBarnetilleggAuto.template,
            Fixtures.create<OpphoerBarnetilleggAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "UT_OPPHOERER_BARNETILLEGG"
        )
    }
}
