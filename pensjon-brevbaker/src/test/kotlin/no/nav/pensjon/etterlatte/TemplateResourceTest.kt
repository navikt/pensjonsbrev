package no.nav.pensjon.etterlatte

import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.objectMapper
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.writeTestPDF
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths

class TemplateResourceTest {

    @Tag(TestTags.INTEGRATION_TEST)
    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
        spraak: Language,
    ) {
        val letter = Letter(template, fixtures, spraak, Fixtures.felles, )

        Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter.language, letter.felles, letter.template.letterMetadata.brevtype) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(filnavn(etterlatteBrevKode, spraak), it) }
    }

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
        spraak: Language,
    ) {
        Letter(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).renderTestHtml(filnavn(etterlatteBrevKode, spraak))
    }

    private fun filnavn(etterlatteBrevKode: EtterlatteBrevKode, spraak: Language) =
        "${etterlatteBrevKode.name}_${spraak.javaClass.simpleName}"

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> jsontest(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
        spraak: Language,
    ) {
        val erHovedmal = fixtures.instanceOf(BrevDTO::class) && !listOf(
            Delmal::class,
            Vedlegg::class,
            ManueltBrevDTO::class
        ).any { fixtures.instanceOf(it) }
        // Hovedmalar skal ikkje redigerast i Gjenny, så dei treng vi ikkje å lage JSON av.
        // I tillegg er det per no ein mangel i brevbakeren at han ikkje klarer å lage JSON av tabellar, som vi bruker i ein del hovedmalar
        if (erHovedmal) {
            return
        }
        Letter(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).let { Letter2Markup.render(it) }
            .also { json ->
                Paths.get("build/test_json")
                    .also { Files.createDirectories(it) }
                    .resolve((Paths.get("${filnavn(etterlatteBrevKode, spraak)}.json")))
                    .let { Files.writeString(it, objectMapper.writeValueAsString(json)) }
            }
    }

    companion object {
        @JvmStatic
        fun alleMalene() = listOf(Language.Nynorsk, Language.Bokmal, Language.English)
            .flatMap { spraak ->
                prodAutobrevTemplates.map {
                    Arguments.of(
                        it.template,
                        it.kode,
                        Fixtures.create(it.template.letterDataType),
                        spraak,
                    )
                }
            }
    }
}
