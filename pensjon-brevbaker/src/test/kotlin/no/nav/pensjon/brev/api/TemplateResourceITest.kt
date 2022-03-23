package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Tag(TestTags.PDF_BYGGER)
class TemplateResourceITest {
    private val templateResource = TemplateResource()

    @Test
    fun `all templates can render and compile`() {
        requestTemplates()
            .associateWith { templateResource.getTemplate(it) }
            .forEach { testTemplate(it.key, it.value) }
    }

    private fun testTemplate(name: String, template: LetterTemplate<*, *>?) {
        if (template == null) {
            fail { "TemplateResource.getTemplates() returned a template name that doesnt exist: $name" }
        }
        val argument = createArgument(template.letterDataType)
        val rendered = requestLetter(
            LetterRequest(
                template = name,
                letterData = argument,
                felles = Fixtures.felles,
                language = LanguageCode.BOKMAL
            )
        )
        try {
            writeTestPDF(name, rendered.base64pdf)
        } catch (failedCompile: Exception) {
            fail("Failed to compile template($name) with argument: $argument", failedCompile)
        }
    }

    private fun createArgument(letterDataType: KClass<out Any>): Any {
        try {
            return letterDataType.createInstance()
        } catch (noDefaultConstruct: IllegalArgumentException) {
            fail(
                "LetterData data class doesn't have no-arg constructor for test data: ${letterDataType.qualifiedName}",
                noDefaultConstruct
            )
        }
    }

}