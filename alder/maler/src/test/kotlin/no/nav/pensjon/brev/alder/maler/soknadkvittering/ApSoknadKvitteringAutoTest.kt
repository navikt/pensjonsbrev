package no.nav.pensjon.brev.alder.maler.soknadkvittering

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.alder.Fixtures
import no.nav.pensjon.brev.alder.maler.brev.soknadkvittering.ApSoknadKvitteringAuto
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class ApSoknadKvitteringAutoTest {

    @Test
    fun `ap soknad kvittering pdf`() {
        LetterTestImpl(
            ApSoknadKvitteringAuto.template,
            Fixtures.create<ApSoknadKvitteringAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("AP_SOKNAD_KVITTERING")
    }
}
