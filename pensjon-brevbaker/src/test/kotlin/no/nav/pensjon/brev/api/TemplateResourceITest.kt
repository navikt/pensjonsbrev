package no.nav.pensjon.brev.api

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.copy
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.brev.brevbaker.requestLetter
import no.nav.brev.brevbaker.writeTestPDF
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.*

@Tag(TestTags.INTEGRATION_TEST)
class TemplateResourceITest {

    @Test
    fun `can render and compile a template`() {
        testTemplate(LetterExample.kode, LetterExample.template)
    }

    private fun testTemplate(kode: Brevkode.Automatisk, template: LetterTemplate<*, *>?) {
        if (template == null) {
            fail { "TemplateResource.getTemplates() returned a template name that doesnt exist: $kode" }
        }
        val argument = createLetterExampleDto()
        try {
            testBrevbakerApp { client ->
                val rendered = requestLetter(
                    client,
                    BestillBrevRequest(
                        kode = kode,
                        letterData = argument,
                        felles = Fixtures.felles.copy(signerendeSaksbehandlere = null),
                        language = LanguageCode.BOKMAL
                    )
                )
                writeTestPDF(kode.kode(), rendered.file)
            }
        } catch (failedCompile: Exception) {
            fail("Failed to compile template($kode) with argument: $argument", failedCompile)
        }
    }
}