package no.nav.pensjon.etterlatte

import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
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
import no.nav.pensjon.etterlatte.maler.BrevDTO
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths

class TemplateResourceTest {

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}")
    @MethodSource("alleMalene")
    fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
    ) {
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(etterlatteBrevKode.name, it) }
    }

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
    ) {
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(etterlatteBrevKode.name, it) }
    }

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}")
    @MethodSource("alleMalene")
    fun <T : Any> jsontest(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
    ) {
        val erHovedmal = fixtures.instanceOf(BrevDTO::class)
        // Hovedmalar skal ikkje redigerast i Gjenny, så dei treng vi ikkje å lage JSON av.
        // I tillegg er det per no ein mangel i brevbakeren at han ikkje klarer å lage JSON av tabellar, som vi bruker i ein del hovedmalar
        if (erHovedmal) {
            return
        }
        val foreloepigIkkeStoettaIJSON = setOf(
            EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE,
            EtterlatteBrevKode.BARNEPENSJON_REVURDERING_SOESKENJUSTERING,
            EtterlatteBrevKode.OMS_INNVILGELSE_AUTO,
        )
        if (foreloepigIkkeStoettaIJSON.contains(etterlatteBrevKode)) {
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

    companion object {
        @JvmStatic
        fun alleMalene() =
            prodAutobrevTemplates.map {
                Arguments.of(
                    it.template,
                    it.kode,
                    Fixtures.create(it.template.letterDataType),
                )
            }
    }
}
