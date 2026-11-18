package no.nav.pensjon.brev.brevoppskrift

import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.getAs
import kotlinx.serialization.Serializable
import no.nav.pensjon.brev.brevoppskrift.auth.EntraGroupId
import no.nav.pensjon.brev.brevoppskrift.kotlin.ApiContractGenerator
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRecord
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

    @Serializable
    data class GroupConfig(val group: EntraGroupId)

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

    @Test
    fun `semantic types stay primitive in generated contracts and HOCON`() {
        val contract = ApiContractGenerator().generate(listOf(Envelope::class))
        assertThat(contract.typescript).contains("\"id\": string;", "\"revision\": number;", "\"nullable\": string | null;", "\"values\": Record<string, number>;")
        assertThat(contract.typescript).doesNotContain("export interface DraftId", "export interface FieldId", "export interface DraftRevision")
        assertThat(contract.openapi.toString()).contains("format=uuid", "type=integer")
        val config = HoconApplicationConfig(ConfigFactory.parseString("""group = "synthetic-group""""))
        assertThat(config.getAs<GroupConfig>().group.value).isEqualTo("synthetic-group")
    }

    @Test
    fun `version one primitive document remains readable and byte shape compatible`() {
        val json = requireNotNull(javaClass.getResourceAsStream("/semantic-draft-v1.json")).use { it.readBytes() }
        val record = draftObjectMapper.readValue<DraftRecord>(json)
        assertThat(draftObjectMapper.readTree(draftObjectMapper.writeValueAsBytes(record))).isEqualTo(draftObjectMapper.readTree(json))
        assertThat(record.draft.scenarios.single().values[FieldId("field")]?.intValue()).isEqualTo(10)
        assertThat(record.draft.scenarios.single().choices[ChoiceId("choice")]).isTrue()
        assertThat(record.draft.source?.files?.get(RepositoryPath("ufoere/maler/Synthetic.kt"))?.value).isEqualTo("b".repeat(64))
        assertThat(record.draft.attachments.single().id.value).isEqualTo("bound-copy")
    }
}
