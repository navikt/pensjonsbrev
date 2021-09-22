package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSettings
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LanguageTest {

    @Test
    fun `languageSettings creates LanguageSettings`() {
        assertEquals(LanguageSettings(emptyMap()), languageSettings {})
    }

    @Test
    fun `languageSettings_setting adds textblock to name`() {
        val name = "greeting"

        val expected = LanguageSettings(
            mapOf(
                name to listOf(
                    newText(
                        Language.Bokmal to "123",
                        Language.Nynorsk to "234",
                        Language.English to "345",
                    )
                )
            )
        )

        val actual = languageSettings {
            setting(name) {
                text(
                    Language.Bokmal to "123",
                    Language.Nynorsk to "234",
                    Language.English to "345",
                )
            }
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `languageSettings_setting adds element to name`() {
        val name = "greeting"

        val element = newText(
            Language.Bokmal to "123",
            Language.Nynorsk to "234",
            Language.English to "345",
        )
        val expected = LanguageSettings(mapOf(name to listOf(element)))

        val actual = languageSettings {
            setting(name, element)
        }

        assertEquals(expected, actual)
    }
}