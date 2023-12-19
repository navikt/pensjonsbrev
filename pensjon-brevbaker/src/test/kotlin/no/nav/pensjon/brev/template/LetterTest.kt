package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.*

class LetterTest {

    data class TestData(val s: String)

    val template = createTemplate(
        name = "test",
        letterDataType = TestData::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata("Test", false, LetterMetadata.Distribusjonstype.ANNET, brevtype = LetterMetadata.Brevtype.VEDTAKSBREV),
    ) {
        title {
            text(Language.Bokmal to "test")
        }
    }

    @Test
    fun `constructor validates that template supports language`() {
        assertThrows<IllegalArgumentException> {
            Letter(template, TestData("jada"), Language.Nynorsk, felles)
        }
    }

    @Test
    fun `can construct letter with supported language`() {
        Letter(template, TestData("jada"), Language.Bokmal, felles)
    }
}