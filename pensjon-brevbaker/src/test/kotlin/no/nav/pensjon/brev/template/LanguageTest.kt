package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LanguageTest {

    @Test
    fun `BaseLanguages contains all languages`() {
        val baseLang = LanguageCombination.Triple(Language.Bokmal, Language.Nynorsk, Language.English)

        // Verifies that BaseLanguages contains all supported languages (won't compile)
        @Suppress("UNUSED_VARIABLE") val verify: BaseLanguages = baseLang

        assertEquals(Language::class.findSealedObjects(), with(baseLang) { setOf(first, second, third) })
    }
}