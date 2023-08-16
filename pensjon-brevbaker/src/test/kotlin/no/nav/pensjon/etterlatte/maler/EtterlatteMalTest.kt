package no.nav.pensjon.etterlatte.maler

import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.objectMapper
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.template.render.PensjonJsonRenderer
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestHTML
import no.nav.pensjon.brev.writeTestPDF
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

@Tag(TestTags.INTEGRATION_TEST)
abstract class EtterlatteMalTest<T : Any>(
    val template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
    private val etterlatteBrevKode: EtterlatteBrevKode,
    val fixtures: T,
) {

    @Test
    fun testPdf() {
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(etterlatteBrevKode.name, it) }
    }

    @Test
    fun htmltest() {
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(etterlatteBrevKode.name, it) }
    }

    @Test
    fun jsontest() {
        val erHovedmal = this.fixtures.instanceOf(BrevDTO::class)
        // Hovedmalar skal ikkje redigerast i Gjenny, så dei treng vi ikkje å lage JSON av.
        // I tillegg er det per no ein mangel i brevbakeren at han ikkje klarer å lage JSON av tabellar, som vi bruker i ein del hovedmalar
        if (erHovedmal) {
            return
        }
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonJsonRenderer.render(it) }
            .also { json ->
                Paths.get("build/test_json")
                    .also { Files.createDirectories(it) }
                    .resolve((Paths.get("${etterlatteBrevKode.name}.json")))
                    .let { Files.writeString(it, objectMapper.writeValueAsString(json)) }
            }
    }
}
