package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.brev.brevbaker.serialization.LetterMarkupV1JacksonModule
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @Test
    fun `legacy missingFromTemplate JSON is read as template status and written back for rollback`() {
        val json = """{"type":"PARAGRAPH","id":1,"editable":true,"content":[],"missingFromTemplate":true}"""

        val block = databaseObjectMapper.readValue(json, Edit.Block::class.java)
        val written = databaseObjectMapper.readTree(databaseObjectMapper.writeValueAsString(block))

        assertThat(block.templateStatus).isEqualTo(Edit.Block.TemplateStatus.MISSING_FROM_TEMPLATE)
        assertThat(written["templateStatus"].asText()).isEqualTo("MISSING_FROM_TEMPLATE")
        assertThat(written["missingFromTemplate"].asBoolean()).isTrue()
        assertThat(written.has("newFromTemplate")).isFalse()
    }

    @Test
    fun `new status and legacy fields round trip`() {
        val block = Edit.Block.Paragraph(
            id = 1,
            editable = true,
            content = emptyList(),
            templateStatus = Edit.Block.TemplateStatus.NEW_FROM_TEMPLATE,
        )

        val json = objectMapper.writeValueAsString(block)
        val restored = objectMapper.readValue(json, Edit.Block::class.java)
        val tree: JsonNode = objectMapper.readTree(json)

        assertThat(restored).isEqualTo(block)
        assertThat(tree["templateStatus"].asText()).isEqualTo("NEW_FROM_TEMPLATE")
        assertThat(tree["missingFromTemplate"].asBoolean()).isFalse()
        assertThat(tree.has("newFromTemplate")).isFalse()
    }

    @Test
    fun `legacy missing status can be cleared by existing editor payload`() {
        val json = """{"type":"PARAGRAPH","id":null,"editable":true,"content":[],"templateStatus":"MISSING_FROM_TEMPLATE","missingFromTemplate":false}"""

        val block = objectMapper.readValue(json, Edit.Block::class.java)

        assertThat(block.templateStatus).isEqualTo(Edit.Block.TemplateStatus.NONE)
    }

    @Test
    fun `missing status takes precedence when both legacy flags are true`() {
        val json = """{"type":"PARAGRAPH","id":1,"editable":true,"content":[],"templateStatus":"NEW_FROM_TEMPLATE","missingFromTemplate":true}"""

        val block = objectMapper.readValue(json, Edit.Block::class.java)

        assertThat(block.templateStatus).isEqualTo(Edit.Block.TemplateStatus.MISSING_FROM_TEMPLATE)
        assertThat(block.missingFromTemplate).isTrue()
    }

    @Test
    fun `missing legacy flags default to no template status`() {
        val json = """{"type":"PARAGRAPH","id":1,"editable":true,"content":[]}"""

        val block = databaseObjectMapper.readValue(json, Edit.Block::class.java)

        assertThat(block.templateStatus).isEqualTo(Edit.Block.TemplateStatus.NONE)
    }

    @Test
    fun `unknown template status is rejected`() {
        val json = """{"type":"PARAGRAPH","id":1,"editable":true,"content":[],"templateStatus":"UNKNOWN"}"""

        assertThatThrownBy { databaseObjectMapper.readValue(json, Edit.Block::class.java) }
            .isInstanceOf(JsonMappingException::class.java)
    }
}