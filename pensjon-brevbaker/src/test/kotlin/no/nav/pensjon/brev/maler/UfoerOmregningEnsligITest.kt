package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        renderTestPDF(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "UT_DOD_ENSLIG_AUTO_BOKMAL"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "UT_DOD_ENSLIG_AUTO_BOKMAL"
        )
    }
}
