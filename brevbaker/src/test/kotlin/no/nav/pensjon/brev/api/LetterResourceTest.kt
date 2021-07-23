package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.something.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LetterResourceTest {

    val returAdresse = Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed", 22)
    val template = ExperimentTemplates.eksempelBrev
    private val templateArgs: Map<String, JsonNode> = mapOf(
        ReturAdresse.name to jacksonObjectMapper().valueToTree(returAdresse),
        SaksNr.name to jacksonObjectMapper().valueToTree(1234),
        LetterTitle.name to jacksonObjectMapper().valueToTree("Tittel"),
        PensjonInnvilget.name to jacksonObjectMapper().valueToTree(true),
        FornavnMottaker.name to jacksonObjectMapper().valueToTree("FornavnMottaker"),
        EtternavnMottaker.name to jacksonObjectMapper().valueToTree("EtternavnMottaker"),
        GatenavnMottaker.name to jacksonObjectMapper().valueToTree("GatenavnMottaker"),
        HusnummerMottaker.name to jacksonObjectMapper().valueToTree(1234),
        PostnummerMottaker.name to jacksonObjectMapper().valueToTree(1337),
        PoststedMottaker.name to jacksonObjectMapper().valueToTree("PoststedMottaker"),
        NorskIdentifikator.name to jacksonObjectMapper().valueToTree(13374212345),
    )

    @Test
    fun test() {
        println(jacksonObjectMapper().writeValueAsString(LetterRequest(template.name, templateArgs)))
    }

    @Test
    fun `create finds correct template`() {
        val letter = LetterResource.create(LetterRequest(template.name, templateArgs))

        assertEquals(template, letter.template)
    }

    @Test
    fun `create fails when template doesnt exist`() {
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest("non existing", templateArgs))
        }
    }

    @Test
    fun `create requires arguments`() {
        assertThrows<IllegalArgumentException> {
            LetterResource.create(LetterRequest(template.name, emptyMap()))
        }
    }

    @Test
    fun `create parses arguments`() {
        val letter = LetterResource.create(LetterRequest(template.name, templateArgs))
        assertEquals(returAdresse, letter.requiredArg(ReturAdresse))
    }

    @Test
    fun `create parses arguments but does not add null values`() {
        val argsWithoutPensjonInnvilget = templateArgs.filterKeys { it != PensjonInnvilget.name }
        val letter = LetterResource.create(LetterRequest(template.name, argsWithoutPensjonInnvilget))

        assertFalse(
            letter.arguments.containsKey(PensjonInnvilget)
        )
    }
}