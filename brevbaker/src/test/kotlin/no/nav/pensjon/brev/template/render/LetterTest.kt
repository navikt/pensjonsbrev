package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.Fixtures.felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LetterTest {

    data class TestData(val s: String)

    val template = createTemplate(
        name = "test",
        letterDataType = TestData::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            "Test",
            false,
            LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    ) {
        title {
            text(Language.Bokmal to "test")
        }
    }

    @Test
    fun `constructor validates that template supports language`() {
        assertThrows<IllegalArgumentException> {
            LetterImpl(template, TestData("jada"), Language.Nynorsk, felles)
        }
    }

    @Test
    fun `can construct letter with supported language`() {
        LetterImpl(template, TestData("jada"), Language.Bokmal, felles)
    }
}