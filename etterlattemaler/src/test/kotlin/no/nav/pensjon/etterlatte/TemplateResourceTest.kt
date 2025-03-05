package no.nav.pensjon.etterlatte

import io.ktor.util.reflect.instanceOf
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.TestTags.MANUAL_TEST
import no.nav.brev.brevbaker.jacksonObjectMapper
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.*
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

private val objectMapper = jacksonObjectMapper()

class TemplateResourceTest {

    @Tag(MANUAL_TEST)
    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: Brevkode.Automatisk,
        fixtures: T,
        spraak: Language,
    ) {
        val letter = LetterTestImpl(template, fixtures, spraak, Fixtures.felles)

        letter.renderTestPDF(filnavn(etterlatteBrevKode, spraak))
    }

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: Brevkode.Automatisk,
        fixtures: T,
        spraak: Language,
    ) {
        LetterTestImpl(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).renderTestHtml(filnavn(etterlatteBrevKode, spraak))
    }

    private fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}"

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("alleMalene")
    fun <T : Any> jsontest(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: Brevkode.Automatisk,
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
        LetterTestImpl(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).let { LetterTestRenderer.render(it) }
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
                EtterlatteMaler.hentAutobrevmaler().map {
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
