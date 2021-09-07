package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.features.*
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.something.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class LetterResourceTest {

    val returAdresse = Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed")
    val template = EksempelBrev.template
    private val templateArgs: Map<String, JsonNode> = with(jacksonObjectMapper()) {
        mapOf(
            SaksNr.name to valueToTree(1234),
            PensjonInnvilget.name to valueToTree(true),
            NorskIdentifikator.name to valueToTree(13374212345),
            Felles.name to Fagdelen.Felles(
                dokumentDato = LocalDate.now(),
                returAdresse = returAdresse,
                mottaker = Fagdelen.Mottaker(
                    "FornavnMottaker",
                    "EtternavnMottaker",
                    "GatenavnMottaker",
                    "21 A",
                    "0123",
                    "PoststedMottaker"
                )
            ).let { valueToTree(it) }
        )
    }

    @Test
    fun `create finds correct template`() {
        val letter = LetterResource.create(LetterRequest(template.name, templateArgs, Language.Bokmal))

        assertEquals(template, letter.template)
    }

    @Test
    fun `create fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource.create(LetterRequest("non existing", templateArgs, Language.Bokmal))
        }
    }

    @Test
    fun `create requires arguments`() {
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest(template.name, emptyMap(), Language.Bokmal))
        }
    }

    @Test
    fun `create parses arguments`() {
        val letter = LetterResource.create(LetterRequest(template.name, templateArgs, Language.Bokmal))
        assertEquals(returAdresse, letter.requiredArg(Felles).returAdresse)
    }

    @Test
    fun `create parses arguments but does not add null values`() {
        val argsWithoutPensjonInnvilget = templateArgs.filterKeys { it != PensjonInnvilget.name }
        val letter = LetterResource.create(LetterRequest(template.name, argsWithoutPensjonInnvilget, Language.Bokmal))

        assertFalse(
            letter.arguments.containsKey(PensjonInnvilget)
        )
    }
}