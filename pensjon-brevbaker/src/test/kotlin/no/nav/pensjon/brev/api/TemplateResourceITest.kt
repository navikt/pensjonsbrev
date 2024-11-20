package no.nav.pensjon.brev.api

import io.mockk.mockk
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.*
import kotlin.reflect.KClass

@Tag(TestTags.INTEGRATION_TEST)
class TemplateResourceITest {
    private val templateResource = TemplateResource("autobrev", ProductionTemplates.autobrev, mockk())

    @Test
    fun `can render and compile a template`() {
        requestTemplates()
            .associateWith { templateResource.getTemplate(it)!! }
            .firstNotNullOf { it }
            .let { testTemplate(it.key, it.value.template) }
    }

    private fun testTemplate(kode: Brevkode.AutoBrev, template: LetterTemplate<*, *>?) {
        if (template == null) {
            fail { "TemplateResource.getTemplates() returned a template name that doesnt exist: $kode" }
        }
        val argument = createArgument(template.letterDataType)
        try {
            val rendered = requestLetter(
                BestillBrevRequest(
                    kode = kode,
                    letterData = argument,
                    felles = Fixtures.felles.copy(signerendeSaksbehandlere = null),
                    language = LanguageCode.BOKMAL
                )
            )
            writeTestPDF(kode.name, rendered.file)
        } catch (failedCompile: Exception) {
            fail("Failed to compile template($kode) with argument: $argument", failedCompile)
        }
    }

    private fun createArgument(letterDataType: KClass<out Any>): BrevbakerBrevdata {
        @Suppress("UNCHECKED_CAST")
        return Fixtures.create(letterDataType as KClass<out BrevbakerBrevdata>)
    }

}