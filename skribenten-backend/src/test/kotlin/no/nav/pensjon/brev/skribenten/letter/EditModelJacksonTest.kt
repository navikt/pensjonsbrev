package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.brevbaker.serialization.LetterMarkupV1JacksonModule
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.DecisionRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EditModelJacksonTest {

    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        registerModule(LetterMarkupV1JacksonModule)

        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Test
    fun `kan deserialisere gjeldende EditModel`() {
        val expected = editedLetter {
            title1 { literal(null, "Mitt avsnitt") }
            paragraph {
                literal(null, text = "hei")
                variable(null, "joda")
                itemList {
                    item { literal(null, "kanskje") }
                }
            }
        }
        val json = objectMapper.writeValueAsString(expected)
        val actual = objectMapper.readValue(json, Edit.Letter::class.java)

        assertThat(actual).isEqualTo(expected)
    }

    private fun paragraphJson(statusFields: String) =
        """{"type":"PARAGRAPH","id":1,"editable":true,"content":[]$statusFields}"""

    private fun readBlock(json: String) = objectMapper.readValue(json, Edit.Block::class.java)

    @Test
    fun `blokk uten missingFromTemplate trenger ingen beslutning`() {
        assertThat(readBlock(paragraphJson("")).decision).isNull()
        assertThat(readBlock(paragraphJson(""","missingFromTemplate":false""")).decision).isNull()
    }

    @Test
    fun `blokk med missingFromTemplate true gir PENDING`() {
        assertThat(readBlock(paragraphJson(""","missingFromTemplate":true""")).decision)
            .isEqualTo(DecisionRequest(DecisionRequest.Type.MISSING_FROM_RENDERED, DecisionRequest.Decision.PENDING))
    }

    @Test
    fun `missingFromTemplate true beholder eksplisitt beslutning`() {
        DecisionRequest.Decision.entries.forEach { status ->
            val json = paragraphJson(
                ""","decision":{"type":"MISSING_FROM_RENDERED","status":"$status"},"missingFromTemplate":true"""
            )
            assertThat(readBlock(json).decision)
                .isEqualTo(DecisionRequest(DecisionRequest.Type.MISSING_FROM_RENDERED, status))
        }
    }

    @Test
    fun `missingFromTemplate false med beslutning gir ACCEPTED`() {
        val json = paragraphJson(
            ""","decision":{"type":"MISSING_FROM_RENDERED","status":"PENDING"},"missingFromTemplate":false"""
        )
        assertThat(readBlock(json).decision)
            .isEqualTo(DecisionRequest(DecisionRequest.Type.MISSING_FROM_RENDERED, DecisionRequest.Decision.ACCEPTED))
    }

    @Test
    fun `serialiserer beslutninger og missingFromTemplate for alle blokktyper`() {
        DecisionRequest.Decision.entries.forEach { status ->
            val decision = DecisionRequest(DecisionRequest.Type.MISSING_FROM_RENDERED, status)
            val blocks = editLetterBlocks {
                paragraph(id = 1, decision = decision) {}
                title1(id = 2, decision = decision) {}
                title2(id = 3, decision = decision) {}
                title3(id = 4, decision = decision) {}
            }

            blocks.forEach { block ->
                val tree = objectMapper.valueToTree<ObjectNode>(block)

                assertThat(tree["decision"]["type"].asText()).isEqualTo(decision.type.name)
                assertThat(tree["decision"]["status"].asText()).isEqualTo(status.name)
                assertThat(tree["missingFromTemplate"].asBoolean()).isTrue()
                assertThat(readBlock(objectMapper.writeValueAsString(block)).decision)
                    .isEqualTo(decision)
                tree.put("missingFromTemplate", false)
                assertThat(readBlock(objectMapper.writeValueAsString(tree)).decision)
                    .isEqualTo(decision.copy(status = DecisionRequest.Decision.ACCEPTED))
                tree.remove("missingFromTemplate")
                assertThat(readBlock(objectMapper.writeValueAsString(tree)).decision).isEqualTo(decision)
            }
        }
    }
}
