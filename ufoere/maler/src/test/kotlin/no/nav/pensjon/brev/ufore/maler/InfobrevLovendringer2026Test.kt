package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.info.InfobrevLovendringer2026
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InfobrevLovendringer2026Test {

    @Test
    fun testHtml() {
        LetterTestImpl(
            template = InfobrevLovendringer2026.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(InfobrevLovendringer2026.kode.name)
    }
}
