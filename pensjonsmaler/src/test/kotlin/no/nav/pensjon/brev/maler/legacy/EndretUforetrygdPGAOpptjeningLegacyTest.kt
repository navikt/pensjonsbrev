package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndretUforetrygdPGAOpptjeningLegacyTest {

    @Test
    fun testPdf() {
        Letter(
            EndretUforetrygdPGAOpptjeningLegacy.template,
            Fixtures.create<EndretUforetrygdPGAOpptjeningLegacy>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF(EndretUforetrygdPGAOpptjeningLegacy.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            EndretUforetrygdPGAOpptjeningLegacy.template,
            Fixtures.create<EndretUforetrygdPGAOpptjeningLegacy>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml(EndretUforetrygdPGAOpptjeningLegacy.kode.name)
    }
}