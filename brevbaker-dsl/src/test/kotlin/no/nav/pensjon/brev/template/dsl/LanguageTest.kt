package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.ElementImpl
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSettings
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LanguageTest {

    @Test
    fun `languageSettings creates LanguageSettings`() {
        Assertions.assertEquals(LanguageSettings(emptyMap()), languageSettings {})
    }

    @Test
    fun `languageSettings_setting adds textblock to name`() {
        val name = "greeting"

        val expected = LanguageSettings(
            mapOf(
                name to ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(
                    Language.Bokmal to "123",
                    Language.Nynorsk to "234",
                    Language.English to "345",
                )
            )
        )

        val actual = languageSettings {
            setting(name) {
                ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(
                    Language.Bokmal to "123",
                    Language.Nynorsk to "234",
                    Language.English to "345",
                )
            }
        }

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `languageSettings_setting adds element to name`() {
        val name = "greeting"

        val element = ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(
            Language.Bokmal to "123",
            Language.Nynorsk to "234",
            Language.English to "345",
        )
        val expected = LanguageSettings(mapOf(name to element))

        val actual = languageSettings {
            setting(name, element)
        }

        Assertions.assertEquals(expected, actual)
    }
}