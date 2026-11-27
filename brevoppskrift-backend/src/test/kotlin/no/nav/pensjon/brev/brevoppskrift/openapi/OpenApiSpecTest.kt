package no.nav.pensjon.brev.brevoppskrift.openapi

import com.auth0.jwk.JwkProvider
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.brevoppskrift.AzureConfig
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.auth.EntraAudience
import no.nav.pensjon.brev.brevoppskrift.auth.EntraClientId
import no.nav.pensjon.brev.brevoppskrift.auth.EntraGroupId
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.testModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.parallel.Isolated
import java.nio.file.Files
import java.nio.file.Path

@Tag("openapi-spec")
@Isolated
class OpenApiSpecTest {
    @Test
    fun `export inferred contract from production routes`() = testApplication {
        val azure = AzureConfig(
            "https://issuer.invalid", EntraAudience("backend"), "https://issuer.invalid/jwks",
            listOf(AzureConfig.PreAuthorizedApp("synthetic", EntraClientId("bff"))), EntraGroupId("editors"),
        )
        application {
            testModule(azure, JwkProvider { error("Spec export does not authenticate a user") }, postgres.database(), productionAuthoring = true)
        }
        val response = client.get("/swagger-internal/documentation.json")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val json = response.bodyAsText()
        val spec = draftObjectMapper.readTree(json)
        Files.createDirectories(Path.of("build"))
        Files.writeString(Path.of("build/openapi-spec.json"), json)
        assertThat(spec["openapi"].asText()).startsWith("3.")
        val paths = spec["paths"]
        assertThat(paths.fieldNames().asSequence().toList()).isNotEmpty().allMatch { it.startsWith("/api/v1/") }
        assertThat(paths.has("/api/v1/utkast/{id}/pr")).isTrue()
        val requests = listOf(
            Triple("/api/v1/generer", "post", "GenerateRequest"),
            Triple("/api/v1/forhandsvisning", "post", "PreviewRequest"),
            Triple("/api/v1/ki/nynorsk", "post", "AiRequest"),
            Triple("/api/v1/ki/klarsprak", "post", "AiRequest"),
            Triple("/api/v1/utkast/valider", "post", "Draft"),
            Triple("/api/v1/utkast", "post", "CreateDraftRequest"),
            Triple("/api/v1/utkast/{id}", "put", "UpdateDraftRequest"),
            Triple("/api/v1/utkast/{id}/status", "post", "StatusRequest"),
            Triple("/api/v1/utkast/{id}/grunnlinje", "post", "RevisionRequest"),
            Triple("/api/v1/utkast/{id}/arkiver", "post", "ArchiveRequest"),
            Triple("/api/v1/utkast/{id}/pr", "post", "RevisionRequest"),
        )
        for (request in requests) {
            val body = paths[request.first][request.second]["requestBody"]
            assertThat(body["required"].asBoolean()).isTrue()
            assertThat(body.at("/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/${request.third}")
        }
        val operations = paths.flatMap { it.toList() }
        assertThat(operations).hasSize(19)
        for (operation in operations) {
            assertThat(operation["security"].toString()).isEqualTo("""[{"entra":[]}]""")
            assertThat(operation.at("/responses/401/content/application~1problem+json/schema/\$ref").asText()).isEqualTo("#/components/schemas/Problem")
            assertThat(operation["responses"].fieldNames().asSequence().any { it.startsWith("2") }).isTrue()
        }
        assertThat(paths["/api/v1/generer"]["post"].at("/responses/200/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/GenerationResult")
        assertThat(paths["/api/v1/forhandsvisning"]["post"].at("/responses/200/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/PreviewResult")
        assertThat(paths["/api/v1/utkast"]["post"].at("/responses/201/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/DraftRecord")
        val location = paths["/api/v1/utkast"]["post"].at("/responses/201/headers/Location")
        assertThat(location.at("/schema/type").asText()).isEqualTo("string")
        assertThat(location.has("content")).isFalse()
        assertThat(paths["/api/v1/utkast/{id}/arkiver"]["post"].at("/responses/204").has("content")).isFalse()
        assertThat(paths["/api/v1/utkast/{id}/pr"]["post"].at("/responses/202/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/PrJob")
        assertThat(paths["/api/v1/utkast/{id}/pr"]["get"].at("/responses/200/content/application~1json/schema/\$ref").asText()).isEqualTo("#/components/schemas/PrJob")
        assertThat(spec.at("/components/schemas/Draft/properties/formatVersion/type").asText()).isEqualTo("integer")
        fun checkReferences(node: com.fasterxml.jackson.databind.JsonNode) {
            if (node.isObject) {
                node["\$ref"]?.let { reference ->
                    assertThat(spec.at(reference.asText().removePrefix("#")).isMissingNode).describedAs(reference.asText()).isFalse()
                }
            }
            node.forEach(::checkReferences)
        }
        checkReferences(spec)
        assertThat(json).doesNotContain("SourceCatalogueSnapshot", "HikariDataSource", "AzureConfig", "SourceEditBinding", "PrTicket", "PrLeaseToken")
    }

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()
    }
}
