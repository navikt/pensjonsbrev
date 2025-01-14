package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselOmMuligAvslagTest {

    @Test
    fun testPdf() {
        renderTestPDF(
            VarselOmMuligAvslag.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            VarselOmMuligAvslag.kode.name
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            VarselOmMuligAvslag.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            VarselOmMuligAvslag.kode.name
        )
    }
}