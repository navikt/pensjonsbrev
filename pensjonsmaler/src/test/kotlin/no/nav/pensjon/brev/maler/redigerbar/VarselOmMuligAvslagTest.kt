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
class VarselOmMuligAvslagTest {

    @Test
    fun testPdf() {
        Letter(
            VarselOmMuligAvslag.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(VarselOmMuligAvslag.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            VarselOmMuligAvslag.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml(VarselOmMuligAvslag.kode.name)
    }
}