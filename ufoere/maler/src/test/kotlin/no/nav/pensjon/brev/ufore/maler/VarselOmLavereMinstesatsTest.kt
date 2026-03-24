package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmLavereMinstesats
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselOmLavereMinstesatsTest {

    @Test
    fun testHtml() {
        LetterTestImpl(
            template = VarselOmLavereMinstesats.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmLavereMinstesats.kode.name)
    }
}
