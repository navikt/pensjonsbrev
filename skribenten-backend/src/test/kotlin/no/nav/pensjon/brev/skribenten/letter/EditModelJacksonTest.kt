package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.brevbaker.serialization.LetterMarkupV1JacksonModule
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.MergeStatus
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
    fun `blokk lagret foer templateStatus fantes og uten missingFromTemplate er IN_TEMPLATE`() {
        assertThat(readBlock(paragraphJson("")).status).isEqualTo(MergeStatus.MATCHED_WITH_RENDERED)
    }

    @Test
    fun `blokk lagret foer templateStatus fantes med missingFromTemplate true er MISSING_FROM_RENDERED`() {
        assertThat(readBlock(paragraphJson(""","missingFromTemplate":true""")).status).isEqualTo(MergeStatus.MISSING_FROM_RENDERED)
    }

    @Test
    fun `missingFromTemplate false overstyrer MISSING_FROM_RENDERED slik at frontend kan beholde blokken`() {
        val json = paragraphJson(""","templateStatus":"MISSING_FROM_RENDERED","missingFromTemplate":false""")
        assertThat(readBlock(json).status).isEqualTo(MergeStatus.MATCHED_WITH_RENDERED)
    }

    @Test
    fun `serialiserer baade templateStatus og missingFromTemplate`() {
        val block = Edit.Block.Paragraph(
            id = 1,
            editable = true,
            content = emptyList(),
            status = MergeStatus.MISSING_FROM_RENDERED
        )
        val tree = objectMapper.valueToTree<JsonNode>(block)

        assertThat(tree["templateStatus"].asText()).isEqualTo(MergeStatus.MISSING_FROM_RENDERED.name)
        assertThat(tree["missingFromTemplate"].asBoolean()).isTrue()
        assertThat(readBlock(objectMapper.writeValueAsString(block))).isEqualTo(block)
    }
}
