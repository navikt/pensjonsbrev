package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndretUforetrygdPGAOpptjeningLegacyTest {

    @Test
    fun testPdf() {
        renderTestPDF(
            EndretUforetrygdPGAOpptjeningLegacy.template,
            Fixtures.create<EndretUforetrygdPGAOpptjeningLegacy>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            EndretUforetrygdPGAOpptjeningLegacy.kode.name
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            EndretUforetrygdPGAOpptjeningLegacy.template,
            Fixtures.create<EndretUforetrygdPGAOpptjeningLegacy>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            EndretUforetrygdPGAOpptjeningLegacy.kode.name
        )
    }
}