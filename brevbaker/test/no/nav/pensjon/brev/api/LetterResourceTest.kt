package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.something.ExperimentTemplates
import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.ReturAdresse
import no.nav.pensjon.brev.template.SaksNr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LetterResourceTest {

    val returAdresse = Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed", 22)
    val template = ExperimentTemplates.eksempelBrev
    private val templateArgs: Map<String, JsonNode> = mapOf(
        ReturAdresse.name to jacksonObjectMapper().valueToTree(returAdresse),
        SaksNr.name to jacksonObjectMapper().valueToTree(1234),
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
}