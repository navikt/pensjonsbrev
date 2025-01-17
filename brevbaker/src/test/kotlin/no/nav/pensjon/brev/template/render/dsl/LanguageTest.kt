package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.template.dsl.languageSettings
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
                name to Literal.create(
                        Language.Bokmal to "123",
                        Language.Nynorsk to "234",
                        Language.English to "345",
                )
            )
        )

        val actual = languageSettings {
            setting(name) {
                Literal.create(
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

        val element = Literal.create(
            Language.Bokmal to "123",
            Language.Nynorsk to "234",
            Language.English to "345",
        )
        val expected = LanguageSettings(mapOf(name to element))

        val actual = languageSettings {
            setting(name, element)
        }

        assertEquals(expected, actual)
    }
}