package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OversettelseAvDokumenterTest {

    @Test
    fun testPdf() {
        Letter(
            OversettelseAvDokumenter.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(OversettelseAvDokumenter.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            OversettelseAvDokumenter.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(OversettelseAvDokumenter.kode.name)
    }
}