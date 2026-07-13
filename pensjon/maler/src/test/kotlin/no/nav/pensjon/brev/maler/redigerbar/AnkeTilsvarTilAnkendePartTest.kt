package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.redigerbar.AnkeTilsvarTilAnkendePartDto
import no.nav.pensjon.brev.maler.klageOgAnke.AnkeTilsvarTilAnkendePart
import org.junit.jupiter.api.Tag

@Tag(TestTags.MANUAL_TEST)
class AnkeTilsvarTilAnkendePartTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AnkeTilsvarTilAnkendePart.template,
            Fixtures.create<AnkeTilsvarTilAnkendePartDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(AnkeTilsvarTilAnkendePart.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AnkeTilsvarTilAnkendePart.template,
            Fixtures.create<AnkeTilsvarTilAnkendePartDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(AnkeTilsvarTilAnkendePart.kode.name)
    }
}