package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

class LanguageTest {
    @Test
    fun `BaseLanguages contains all languages`() {
        val baseLang = LanguageCombination.Triple(Language.Bokmal, Language.Nynorsk, Language.English)

        // Verifies that BaseLanguages contains all supported languages (won't compile)
        @Suppress("UNUSED_VARIABLE")
        val verify: BaseLanguages = baseLang

        assertEquals(Language::class.findSealedObjects(), with(baseLang) { setOf(first, second, third) })
    }
}

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }
