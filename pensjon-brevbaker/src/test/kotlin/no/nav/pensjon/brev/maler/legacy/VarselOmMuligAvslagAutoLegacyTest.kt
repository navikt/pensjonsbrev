package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselOmMuligAvslagAutoLegacyTest {

    @Test
    fun testPdf() {
        Letter(
            VarselOmMuligAvslagAutoLegacy.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(VarselOmMuligAvslagAutoLegacy.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            VarselOmMuligAvslagAutoLegacy.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml(VarselOmMuligAvslagAutoLegacy.kode.name)
    }
}