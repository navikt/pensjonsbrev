package no.nav.pensjon.etterlatte

import io.ktor.util.reflect.instanceOf
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.brev.brevbaker.jacksonObjectMapper
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EtterlatteTemplatesTest : BrevmodulTest(
    templates = EtterlatteMaler,
    auto = EtterlatteBrevKode.entries,
    redigerbare = setOf(),
    fixtures = Fixtures,
    filterForPDF = listOf(),
) {

    @Test
    override fun `alle autobrev fins i templates`() {
        val brukteKoder = templates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = auto.filterNot { it == EtterlatteBrevKode.INGEN_REDIGERBAR_DEL }.filterNot { brukteKoder.contains(it) }
        assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    private val objectMapper = jacksonObjectMapper()

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
}