package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.VISUAL_TEST)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        Letter(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL")
    }

    @Test
    fun testHtml() {
        Letter(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        )
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}
