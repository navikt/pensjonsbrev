package no.nav.pensjon.brev.brevoppskrift.openapi

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.openapi.JsonSchema
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

class JacksonSchemaInferenceTest {
    private inline fun <reified T> schema(): JsonNode = draftObjectMapper.readTree(
        Json.encodeToString(JsonSchema.serializer(), JacksonSchemaInference(draftObjectMapper).buildSchema(typeOf<T>())),
    )

    @Test
    fun `semantic identifiers revisions and numbers use the actual Jackson scalar representation`() {
        val draft = exampleDraft()
        val field = draft.fields.first()
        val wire = draftObjectMapper.valueToTree<JsonNode>(field)
        val fieldSchema = schema<DataField>()
        assertThat(wire["id"].isTextual).isTrue()
        assertThat(fieldSchema.at("/properties/id/type").asText()).isEqualTo("string")
        assertThat(schema<DraftRevision>()["type"].asText()).isEqualTo("integer")
        assertThat(schema<DraftId>()["format"].asText()).isEqualTo("uuid")
        assertThat(schema<Literal.Number>().at("/properties/value/type").asText()).isEqualTo("number")
        val nullableRevision = schema<DraftRevision?>()
        assertThat(nullableRevision.at("/oneOf/0/type").asText()).isEqualTo("integer")
        assertThat(nullableRevision.at("/oneOf/1/type").asText()).isEqualTo("null")
    }

    @Test
    fun `defaulted fields are optional without being nullable`() {
        val draft = exampleDraft()
        val minimal = draftObjectMapper.valueToTree<com.fasterxml.jackson.databind.node.ObjectNode>(draft)
        listOf("fields", "choices", "outline", "scenarios", "comments", "attachments", "source", "baseline").forEach(minimal::remove)
        val decoded = draftObjectMapper.treeToValue(minimal, Draft::class.java)
        assertThat(decoded.fields).isEmpty()
        assertThat(decoded.baseline).isNull()
        val schema = schema<Draft>()
        assertThat(schema["required"].map { it.asText() }).containsExactly("formatVersion", "metadata", "title")
        assertThat(schema.at("/properties/fields/type").asText()).isEqualTo("array")
        assertThat(schema.at("/properties/baseline/oneOf/1/type").asText()).isEqualTo("null")
        assertThat(schema.at("/properties/fields/oneOf").isMissingNode).isTrue()
    }

    @Test
    fun `polymorphic discriminator literals agree with Jackson including empty data objects`() {
        val values = listOf(
            Inline.Text(mapOf(DraftLanguage.NB to "synthetic")),
            Inline.Field(FieldId("field")),
            Inline.NewLine,
            Inline.Conditional(Predicate.Choice(ChoiceId("choice"), true), emptyList(), emptyList()),
            Inline.FreeText(mapOf(DraftLanguage.NB to "synthetic")),
        )
        val schema = schema<Inline>()
        assertThat(schema.at("/discriminator/propertyName").asText()).isEqualTo("type")
        for (value in values) {
            val wire = draftObjectMapper.valueToTree<JsonNode>(value)
            val variant = schema["oneOf"].first { it.at("/properties/type/enum/0").asText() == wire["type"].asText() }
            assertThat(variant["required"].map { it.asText() }).contains("type")
            assertThat(variant["properties"].fieldNames().asSequence().toSet()).isEqualTo(wire.fieldNames().asSequence().toSet())
        }
        val nullable = schema<Inline?>()
        assertThat(nullable.at("/oneOf/1/type").asText()).isEqualTo("null")
        assertThat(nullable.has("type")).isFalse()
    }

    @Test
    fun `recursive drafts and typed maps preserve references keys and arbitrary scenario JSON`() {
        val schema = schema<Draft>()
        assertThat(schema.at("/properties/baseline/oneOf/0/properties/draft/\$ref").asText()).isEqualTo("#/components/schemas/Draft")
        assertThat(schema.at("/properties/attachments/items/properties/draft/\$ref").asText()).isEqualTo("#/components/schemas/Draft")
        assertThat(schema.at("/properties/title/properties").fieldNames().asSequence().toList()).containsExactly("NB", "NN", "EN")
        assertThat(schema.at("/properties/title/additionalProperties").asBoolean()).isFalse()
        assertThat(schema.at("/properties/source/oneOf/0/properties/files/additionalProperties/type").asText()).isEqualTo("string")
        assertThat(schema.at("/properties/scenarios/items/properties/choices/additionalProperties/type").asText()).isEqualTo("boolean")
        assertThat(schema.at("/properties/scenarios/items/properties/values/additionalProperties").isEmpty).isTrue()
    }
}
