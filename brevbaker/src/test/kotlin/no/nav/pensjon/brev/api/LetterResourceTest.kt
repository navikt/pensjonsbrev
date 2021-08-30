package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.features.*
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.something.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LetterResourceTest {

    val returAdresse = Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed", 22)
    val template = EksempelBrev.template
    private val templateArgs: Map<String, JsonNode> = with(jacksonObjectMapper()) {
        mapOf(
            ReturAdresse.name to valueToTree(returAdresse),
            SaksNr.name to valueToTree(1234),
            LetterTitle.name to valueToTree("Tittel"),
            PensjonInnvilget.name to valueToTree(true),
            Mottaker.name to valueToTree(
                Fagdelen.Mottaker(
                    "FornavnMottaker",
                    "EtternavnMottaker",
                    "GatenavnMottaker",
                    "21 A",
                    "0123",
                    "PoststedMottaker"
                )
            ),
            NorskIdentifikator.name to valueToTree(13374212345),
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
        assertEquals(returAdresse, letter.requiredArg(ReturAdresse))
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