package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LanguageTest {

    @Test
    fun `BaseLanguages contains all languages`() {
        // Verifies that BaseLanguages contains all supported languages
        val baseLang: BaseLanguages = LanguageCombination.Triple(Language.Bokmal, Language.Nynorsk, Language.English)

        assertEquals(Language::class.findSealedObjects(), with(baseLang) { setOf(first, second, third) })
    }
}