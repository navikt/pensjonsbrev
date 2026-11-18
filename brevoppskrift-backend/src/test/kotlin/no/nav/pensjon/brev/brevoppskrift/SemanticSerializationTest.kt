package no.nav.pensjon.brev.brevoppskrift

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class SemanticSerializationTest {
    data class Envelope(
        val id: DraftId,
        val revision: DraftRevision,
        val field: FieldId,
        val nullable: DraftId?,
        val fields: List<FieldId>,
        val values: Map<FieldId, DraftRevision>,
        val optional: FieldId? = null,
    )

    data class PrimitiveEnvelope(
        val id: UUID,
        val revision: Long,
        val field: String,
        val nullable: UUID?,
        val fields: List<String>,
        val values: Map<String, Long>,
        val optional: String? = null,
    )

    @Test
    fun `semantic scalars preserve JSON shape nullability map keys and strict input`() {
        val json = """{"id":"00000000-0000-0000-0000-000000000001","revision":2,"field":"field","nullable":null,"fields":["field"],"values":{"field":3}}"""
        val result = draftObjectMapper.readValue<Envelope>(json)
        assertThat(result.id.value).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"))
        assertThat(result.values[FieldId("field")]).isEqualTo(DraftRevision(3))
        assertThat(draftObjectMapper.readTree(draftObjectMapper.writeValueAsBytes(result)))
            .isEqualTo(draftObjectMapper.readTree(json.dropLast(1) + ""","optional":null}"""))
        listOf(
            json.replace("\"revision\":2", "\"revision\":\"2\""),
            json.replace("\"revision\":2", "\"revision\":null"),
            json.replace("\"field\":\"field\"", "\"field\":2"),
            json.replace("\"revision\":2,", ""),
            "$json {}",
        ).forEach { input ->
            val before = runCatching { draftObjectMapper.readValue<PrimitiveEnvelope>(input) }
            val after = runCatching { draftObjectMapper.readValue<Envelope>(input) }
            assertThat(after.isFailure).describedAs(input).isEqualTo(before.isFailure)
            if (before.isSuccess) {
                assertThat(draftObjectMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(after.getOrThrow()))
                    .isEqualTo(draftObjectMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(before.getOrThrow()))
            }
        }
    }
}
