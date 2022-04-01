package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.base.DummyBase
import no.nav.pensjon.brev.template.dsl.*
import org.junit.jupiter.api.*

class LetterTest {

    object TestMaster : DummyBase()
    data class TestData(val s: String)

    val title = newText(Language.Bokmal to "test")
    val template = createTemplate(
        name = "test",
        base = TestMaster,
        letterDataType = TestData::class,
        title = title,
        letterMetadata = LetterMetadata("Test", false),
    ) { }

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