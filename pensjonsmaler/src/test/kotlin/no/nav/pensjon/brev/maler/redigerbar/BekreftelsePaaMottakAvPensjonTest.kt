package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class BekreftelsePaaMottakAvPensjonTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            BekreftelsePaaMottakAvPensjon.template,
            Fixtures.create<BekreftelsePaaMottakAvPensjonDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(BekreftelsePaaMottakAvPensjon.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            BekreftelsePaaMottakAvPensjon.template,
            Fixtures.create<BekreftelsePaaMottakAvPensjonDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml(BekreftelsePaaMottakAvPensjon.kode.name)
    }
}