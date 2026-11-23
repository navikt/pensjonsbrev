package no.nav.pensjon.brev.brevoppskrift

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respondText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.utils.io.writeFully
import no.nav.pensjon.brev.brevoppskrift.auth.ENTRA_AUTH
import no.nav.pensjon.brev.brevoppskrift.api.Problem
import no.nav.pensjon.brev.brevoppskrift.auth.requireEditor
import no.nav.pensjon.brev.brevoppskrift.model.exampleDraft
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.auth.EntraAudience
import no.nav.pensjon.brev.brevoppskrift.auth.EntraClientId
import no.nav.pensjon.brev.brevoppskrift.auth.EntraGroupId
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.parallel.Isolated
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Base64
import java.util.Date

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
class ApplicationTest {
    private val azure = AzureConfig("https://issuer.invalid", EntraAudience("backend"), "https://issuer.invalid/jwks", listOf(AzureConfig.PreAuthorizedApp("dev-gcp:pensjonsbrev:brevoppskrift-web", EntraClientId("bff"))), EntraGroupId("editors"))

    private fun io.ktor.server.application.Application.brevoppskriftModule(azure: AzureConfig, jwks: JwkProvider) {
        testModule(azure, jwks, postgres.database())
    }

    @Test
    fun `health is public but API requires an authenticated Nav user`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        assertThat(client.get("/isAlive").status).isEqualTo(HttpStatusCode.OK)
        assertThat(client.get("/isReady").status).isEqualTo(HttpStatusCode.OK)
        val unauthenticated = client.get("/api/v1/meg")
        assertThat(unauthenticated.status).isEqualTo(HttpStatusCode.Unauthorized)
        assertThat(unauthenticated.headers[HttpHeaders.WWWAuthenticate]).isEqualTo("Bearer")
        assertThat(unauthenticated.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
        assertThat(client.get("/metrics").bodyAsText()).contains("ktor_http_server_requests")
    }

    @Test
    fun `problem responses use ContentNegotiation and preserve media type regardless of Accept`() = testApplication {
        application {
            brevoppskriftModule(azure, jwks)
            routing {
                authenticate(ENTRA_AUTH) {
                    get("/problem-type-check") {
                        call.respond(HttpStatusCode.Conflict, Problem(
                            title = "Conflict", status = 409, code = "REVISION_CONFLICT",
                            detail = "Utkastet er endret.", currentRevision = DraftRevision(7),
                        ))
                    }
                }
            }
        }
        for (accept in listOf(null, "*/*", "application/json", "application/problem+json", "text/html")) {
            val unauthorized = client.get("/api/v1/meg") {
                if (accept != null) header(HttpHeaders.Accept, accept)
            }
            assertThat(unauthorized.status).isEqualTo(HttpStatusCode.Unauthorized)
            assertThat(unauthorized.headers[HttpHeaders.WWWAuthenticate]).isEqualTo("Bearer")
            assertThat(unauthorized.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
            val body = draftObjectMapper.readTree(unauthorized.bodyAsText())
            assertThat(body["status"].asInt()).isEqualTo(401)
            assertThat(body["code"].asText()).isEqualTo("UNAUTHORIZED")
            val conflict = client.get("/problem-type-check") {
                bearerAuth(token())
                if (accept != null) header(HttpHeaders.Accept, accept)
            }
            assertThat(conflict.status).isEqualTo(HttpStatusCode.Conflict)
            assertThat(conflict.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
            assertThat(draftObjectMapper.readTree(conflict.bodyAsText())["currentRevision"].asInt()).isEqualTo(7)
            val invalid = client.post("/api/v1/utkast/valider") {
                bearerAuth(token())
                if (accept != null) header(HttpHeaders.Accept, accept)
                contentType(ContentType.Application.Json)
                setBody(draftObjectMapper.writeValueAsString(exampleDraft().copy(formatVersion = 9)))
            }
            assertThat(invalid.status).isEqualTo(HttpStatusCode.UnprocessableEntity)
            assertThat(invalid.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
            assertThat(draftObjectMapper.readTree(invalid.bodyAsText())["errors"].first()["code"].asText()).isEqualTo("UNSUPPORTED_VERSION")
        }
        for (accept in listOf(null, "*/*", "application/json")) {
            val user = client.get("/api/v1/meg") {
                bearerAuth(token())
                if (accept != null) header(HttpHeaders.Accept, accept)
            }
            assertThat(user.status).isEqualTo(HttpStatusCode.OK)
            assertThat(user.headers[HttpHeaders.ContentType]).startsWith("application/json")
        }
    }

    @Test
    fun `wrong issuer audience caller signature and expiry are rejected`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val tokens = listOf(
            token(issuer = "https://wrong.invalid"),
            token(audience = "other-backend"),
            token(caller = "other-app"),
            token(caller = null),
            token(expires = Instant.now().minusSeconds(60)),
            token(algorithm = Algorithm.HMAC256("not-the-signing-key")),
        )
        tokens.forEach { value ->
            assertThat(client.get("/api/v1/meg") { bearerAuth(value) }.status).isEqualTo(HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `required token claims and Nav user context cannot be omitted`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val tokens = listOf("sub", "exp", "nbf", "iat").map { token(omittedClaims = setOf(it)) } +
            listOf(token(navIdent = null), token(navIdent = ""), token(name = null), token(name = ""))
        for (value in tokens) {
            assertThat(client.get("/api/v1/meg") { bearerAuth(value) }.status).isEqualTo(HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `disabled integrations do not claim capabilities or contact external services`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val response = client.get("/api/v1/integrasjoner") { bearerAuth(token()) }
        val capabilities = draftObjectMapper.readTree(response.bodyAsText())
        assertThat(capabilities["persistence"].asBoolean()).isTrue()
        assertThat(capabilities["generation"].asBoolean()).isTrue()
        listOf("ai", "slack", "pullRequests").forEach { assertThat(capabilities[it].asBoolean()).isFalse() }
        for (path in listOf("/ki/nynorsk", "/ki/klarsprak")) {
            val suggestion = client.post("/api/v1$path") {
                bearerAuth(token(groups = listOf("editors")))
                contentType(ContentType.Application.Json)
                setBody("""{"text":"synthetic","language":"NB"}""")
            }
            assertThat(suggestion.status).isEqualTo(HttpStatusCode.ServiceUnavailable)
            assertThat(draftObjectMapper.readTree(suggestion.bodyAsText())["code"].asText()).isEqualTo("UNAVAILABLE")
        }
    }

    @Test
    fun `configured Entra GUIDs accept canonical lowercase claims`() = testApplication {
        val bff = "ABCDEF01-2345-6789-ABCD-EF0123456789"
        val group = "FEDCBA98-7654-3210-FEDC-BA9876543210"
        application { brevoppskriftModule(azure.copy(preAuthApps = listOf(AzureConfig.PreAuthorizedApp("dev-gcp:pensjonsbrev:brevoppskrift-web", EntraClientId(bff))), editorGroupId = EntraGroupId(group)), jwks) }
        val response = client.get("/api/v1/meg") { bearerAuth(token(caller = bff.lowercase(), groups = listOf(group.lowercase()))) }
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(draftObjectMapper.readTree(response.bodyAsText())["canEdit"].asBoolean()).isTrue()
    }

    @Test
    fun `all preauthorized apps can call with user context but still need the editor group to write`() = testApplication {
        val apps = azure.preAuthApps + AzureConfig.PreAuthorizedApp("dev-gcp:pensjonsbrev:another-app", EntraClientId("another-client"))
        application { brevoppskriftModule(azure.copy(preAuthApps = apps), jwks) }
        for (app in apps) {
            val reader = client.get("/api/v1/meg") { bearerAuth(token(caller = app.clientId.value)) }
            assertThat(reader.status).isEqualTo(HttpStatusCode.OK)
            assertThat(draftObjectMapper.readTree(reader.bodyAsText())["canEdit"].asBoolean()).isFalse()
            val editor = client.get("/api/v1/meg") { bearerAuth(token(caller = app.clientId.value, groups = listOf("editors"))) }
            assertThat(draftObjectMapper.readTree(editor.bodyAsText())["canEdit"].asBoolean()).isTrue()
        }
    }

    @Test
    fun `an empty preauthorized app list rejects all callers`() = testApplication {
        application { brevoppskriftModule(azure.copy(preAuthApps = emptyList()), jwks) }
        assertThat(client.get("/api/v1/meg") { bearerAuth(token(groups = listOf("editors"))) }.status).isEqualTo(HttpStatusCode.Unauthorized)
    }

    @Test
    fun `readers retain access but only the configured group can write`() = testApplication {
        application {
            brevoppskriftModule(azure, jwks)
            routing {
                authenticate(ENTRA_AUTH) {
                    post("/editor-check") {
                        call.requireEditor()
                        call.respondText("Allowed")
                    }

                }
            }
        }
        val reader = client.get("/api/v1/meg") { bearerAuth(token(groups = listOf("unrelated-group"))) }
        assertThat(reader.status).isEqualTo(HttpStatusCode.OK)
        assertThat(draftObjectMapper.readTree(reader.bodyAsText())["canEdit"].asBoolean()).isFalse()
        assertThat(client.post("/editor-check") { bearerAuth(token()) }.status).isEqualTo(HttpStatusCode.Forbidden)
        assertThat(client.post("/editor-check") { bearerAuth(token(groups = listOf("editors"))) }.status).isEqualTo(HttpStatusCode.OK)
    }

    @Test
    fun `readers can validate all languages without changing state`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val response = client.post("/api/v1/utkast/valider") {
            bearerAuth(token())
            contentType(ContentType.Application.Json)
            setBody(draftObjectMapper.writeValueAsString(exampleDraft()))
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("""{"valid":true}""")
    }

        @Test
        fun `all mutation routes reject readers`() = testApplication {
            application { brevoppskriftModule(azure, jwks) }
            val id = "00000000-0000-0000-0000-000000000001"
            listOf("/utkast", "/utkast/$id/status", "/utkast/$id/grunnlinje", "/utkast/$id/arkiver", "/ki/nynorsk", "/ki/klarsprak").forEach { path ->
                assertThat(client.post("/api/v1$path") { bearerAuth(token()); contentType(ContentType.Application.Json); setBody("invalid") }.status).isEqualTo(HttpStatusCode.Forbidden)
            }
            assertThat(client.put("/api/v1/utkast/$id") { bearerAuth(token()); contentType(ContentType.Application.Json); setBody("invalid") }.status).isEqualTo(HttpStatusCode.Forbidden)
        }

        @Test
        fun `baseline approval and stale saves preserve review history`() = testApplication {
                    application { testModule(azure, jwks, postgres.database()) }
                    val created = client.post("/api/v1/utkast") {
                        bearerAuth(token(groups = listOf("editors")))
                        contentType(ContentType.Application.Json)
                        setBody(draftObjectMapper.writeValueAsString(CreateDraftRequest(draft = exampleDraft())))
                    }
                    assertThat(created.status).isEqualTo(HttpStatusCode.Created)
                    val record = draftObjectMapper.readValue(created.bodyAsText(), DraftRecord::class.java)
                    val approved = client.post("/api/v1/utkast/${record.id}/grunnlinje") {
                        bearerAuth(token(groups = listOf("editors")))
                        contentType(ContentType.Application.Json)
                        setBody("""{"revision":1}""")
                    }
                    assertThat(approved.status).isEqualTo(HttpStatusCode.OK)
                    val latest = draftObjectMapper.readValue(approved.bodyAsText(), DraftRecord::class.java)
                    assertThat(latest.draft.baseline?.approvedBy?.value).isEqualTo("Synthetic User")
                    val stale = client.put("/api/v1/utkast/${record.id}") {
                        bearerAuth(token(groups = listOf("editors")))
                        contentType(ContentType.Application.Json)
                        setBody(draftObjectMapper.writeValueAsString(UpdateDraftRequest(DraftRevision(1), record.draft)))
                    }
                    assertThat(stale.status).isEqualTo(HttpStatusCode.Conflict)
                    assertThat(draftObjectMapper.readTree(stale.bodyAsText())["currentRevision"].asLong()).isEqualTo(2)
                    val rewritten = client.put("/api/v1/utkast/${record.id}") {
                        bearerAuth(token(groups = listOf("editors")))
                        contentType(ContentType.Application.Json)
                        setBody(draftObjectMapper.writeValueAsString(UpdateDraftRequest(DraftRevision(2), latest.draft.copy(baseline = null))))
                    }
                    assertThat(rewritten.status).isEqualTo(HttpStatusCode.UnprocessableEntity)
                    val unchanged = client.get("/api/v1/utkast/${record.id}") { bearerAuth(token()) }
                    assertThat(draftObjectMapper.readValue(unchanged.bodyAsText(), DraftRecord::class.java).revision.value).isEqualTo(2)
        }
    @Test
    fun `semantic errors return structured issues`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val response = client.post("/api/v1/utkast/valider") {
            bearerAuth(token())
            contentType(ContentType.Application.Json)
            setBody(draftObjectMapper.writeValueAsString(exampleDraft().copy(formatVersion = 9)))
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.UnprocessableEntity)
        assertThat(response.bodyAsText()).contains("UNSUPPORTED_VERSION")
        assertThat(response.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
    }

    @Test
    fun `semantic IDs and revisions preserve route and validation errors`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val created = client.post("/api/v1/utkast") {
            bearerAuth(token(groups = listOf("editors")))
            contentType(ContentType.Application.Json)
            setBody(draftObjectMapper.writeValueAsString(CreateDraftRequest(draft = exampleDraft())))
        }
        assertThat(created.status).isEqualTo(HttpStatusCode.Created)
        val id = draftObjectMapper.readValue(created.bodyAsText(), DraftRecord::class.java).id
        val invalidRevision = client.post("/api/v1/utkast/$id/status") {
            bearerAuth(token(groups = listOf("editors")))
            contentType(ContentType.Application.Json)
            setBody("""{"revision":0,"status":"WORK"}""")
        }
        assertThat(invalidRevision.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(draftObjectMapper.readTree(invalidRevision.bodyAsText())["code"].asText()).isEqualTo("INVALID_REVISION")
        val invalidId = client.get("/api/v1/utkast/not-a-uuid") { bearerAuth(token()) }
        assertThat(invalidId.status).isEqualTo(HttpStatusCode.NotFound)
        assertThat(draftObjectMapper.readTree(invalidId.bodyAsText())["code"].asText()).isEqualTo("DRAFT_NOT_FOUND")
    }

    @Test
    fun `invalid JSON unknown properties missing version and scalar coercion are rejected without echoing input`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val valid = draftObjectMapper.writeValueAsString(exampleDraft())
        val invalid = listOf(
            """{"secret-input":""",
            valid.dropLast(1) + ""","author":"secret-input"}""",
            valid.replace(""""formatVersion":1,""", ""),
            valid.replace(""""formatVersion":1""", """"formatVersion":"1""""),
            valid.replace(""""formatVersion":1""", """"formatVersion":null"""),
        )
        invalid.forEach { body ->
            val response = client.post("/api/v1/utkast/valider") {
                bearerAuth(token())
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(response.bodyAsText()).doesNotContain("secret-input")
            assertThat(draftObjectMapper.readTree(response.bodyAsText())["code"].asText()).isEqualTo("INVALID_DRAFT")
        }
    }

    @Test
    fun `document size is enforced while reading the body`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val response = client.post("/api/v1/utkast/valider") {
            bearerAuth(token())
            contentType(ContentType.Application.Json)
            setBody(" ".repeat(MAX_DRAFT_BYTES + 1))
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.PayloadTooLarge)
    }

    @Test
    fun `body limits reject oversized requests with and without content length`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val endpoints = mapOf(
            "/utkast/valider" to MAX_DRAFT_BYTES,
            "/ki/nynorsk" to 200 * 1024,
            "/ki/klarsprak" to 200 * 1024,
        )
        for (endpoint in endpoints) {
            for (streamed in listOf(false, true)) {
                val bytes = " ".repeat(endpoint.value + 1).toByteArray()
                val response = client.post("/api/v1${endpoint.key}") {
                    bearerAuth(token(groups = listOf("editors")))
                    contentType(ContentType.Application.Json)
                    if (streamed) {
                        setBody(object : io.ktor.http.content.OutgoingContent.WriteChannelContent() {
                            override val contentType = ContentType.Application.Json
                            override suspend fun writeTo(channel: io.ktor.utils.io.ByteWriteChannel) {
                                channel.writeFully(bytes)
                            }
                        })
                    } else {
                        setBody(bytes)
                    }
                }
                assertThat(response.status).describedAs("${endpoint.key}, streamed=$streamed").isEqualTo(HttpStatusCode.PayloadTooLarge)
                assertThat(response.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
                assertThat(draftObjectMapper.readTree(response.bodyAsText())["code"].asText()).isEqualTo("DRAFT_TOO_LARGE")
            }
        }
    }

    @Test
    fun `valid JSON at exact request body limits is accepted`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val draft = draftObjectMapper.writeValueAsString(exampleDraft())
        val validated = client.post("/api/v1/utkast/valider") {
            bearerAuth(token())
            contentType(ContentType.Application.Json)
            setBody(draft + " ".repeat(MAX_DRAFT_BYTES - draft.toByteArray().size))
        }
        assertThat(validated.status).isEqualTo(HttpStatusCode.OK)
        val aiRequest = """{"text":"synthetic","language":"NB"}"""
        for (path in listOf("/ki/nynorsk", "/ki/klarsprak")) {
            val response = client.post("/api/v1$path") {
                bearerAuth(token(groups = listOf("editors")))
                contentType(ContentType.Application.Json)
                setBody(aiRequest + " ".repeat(200 * 1024 - aiRequest.length))
            }
            assertThat(response.status).isEqualTo(HttpStatusCode.ServiceUnavailable)
            assertThat(draftObjectMapper.readTree(response.bodyAsText())["code"].asText()).isEqualTo("UNAVAILABLE")
        }
    }

    @Test
    fun `wrong media type and unknown API paths never return frontend HTML`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        assertThat(client.post("/api/v1/utkast/valider") { bearerAuth(token()); setBody("{}") }.status).isEqualTo(HttpStatusCode.UnsupportedMediaType)
        val missing = client.get("/api/v1/missing") { bearerAuth(token()) }
        assertThat(missing.status).isEqualTo(HttpStatusCode.NotFound)
        assertThat(missing.headers[HttpHeaders.ContentType]).startsWith("application/problem+json")
    }

    @Test
    fun `correlation preserves nonempty IDs and metrics use route patterns without probes`() = testApplication {
        application { brevoppskriftModule(azure, jwks) }
        val id = "00000000-0000-0000-0000-000000000001"
        val response = client.get("/api/v1/utkast/$id?secret-input=synthetic") {
            bearerAuth(token())
            header(HttpHeaders.XRequestId, "safe-request_42")
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.NotFound)
        assertThat(response.headers[HttpHeaders.XRequestId]).isEqualTo("safe-request_42")
        val upstreamId = "upstream/" + "x".repeat(100)
        val inherited = client.get("/api/v1/meg") { header(HttpHeaders.XRequestId, upstreamId) }
        assertThat(inherited.status).isEqualTo(HttpStatusCode.Unauthorized)
        assertThat(inherited.headers[HttpHeaders.XRequestId]).isEqualTo(upstreamId)
        val generated = client.get("/api/v1/meg")
        assertThat(generated.status).isEqualTo(HttpStatusCode.Unauthorized)
        assertThat(generated.headers[HttpHeaders.XRequestId]).isNotBlank()
        client.get("/isAlive")
        client.get("/isReady")
        val scrape = client.get("/metrics").bodyAsText()
        assertThat(scrape).contains("ktor_http_server_requests_seconds_bucket", "/api/v1/utkast/{id}")
        assertThat(scrape).doesNotContain(id, "secret-input", "address=", "/isAlive", "/isReady", "/metrics")
        val capabilities = client.get("/api/v1/integrasjoner") { bearerAuth(token()) }
        assertThat(draftObjectMapper.readTree(capabilities.bodyAsText())["pullRequests"].asBoolean()).isFalse()
        assertThat(client.post("/api/v1/utkast/$id/pr") {
            bearerAuth(token(groups = listOf("editors")))
            contentType(ContentType.Application.Json)
            setBody("""{"revision":1}""")
        }.status).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `imported text profile works through persistence generation status and preview API`() = testApplication {
                application {
                    testModule(azure, jwks, postgres.database(), productionAuthoring = true)
                }
                val catalogue = client.get("/api/v1/katalog") { bearerAuth(token()) }
                assertThat(catalogue.status).isEqualTo(HttpStatusCode.OK)
                val source = draftObjectMapper.readValue(catalogue.bodyAsText(), Array<no.nav.pensjon.brev.brevoppskrift.api.TemplateSource>::class.java).first { it.editable }
                val imported = client.post("/api/v1/utkast") {
                    bearerAuth(token(groups = listOf("editors")))
                    contentType(ContentType.Application.Json)
                    setBody(draftObjectMapper.writeValueAsString(CreateDraftRequest(sourceId = source.id)))
                }
                assertThat(imported.status).isEqualTo(HttpStatusCode.Created)
                val record = draftObjectMapper.readValue(imported.bodyAsText(), DraftRecord::class.java)
                val generated = client.post("/api/v1/generer") {
                    bearerAuth(token())
                    contentType(ContentType.Application.Json)
                    setBody(draftObjectMapper.writeValueAsString(no.nav.pensjon.brev.brevoppskrift.api.GenerateRequest(record.draft)))
                }
                assertThat(generated.status).isEqualTo(HttpStatusCode.OK)
                val code = draftObjectMapper.readValue(generated.bodyAsText(), no.nav.pensjon.brev.brevoppskrift.api.GenerationResult::class.java)
                assertThat(code.issues).isEmpty()
                assertThat(code.files).isNotEmpty()
                val preview = client.post("/api/v1/forhandsvisning") {
                    bearerAuth(token())
                    contentType(ContentType.Application.Json)
                    setBody(draftObjectMapper.writeValueAsString(no.nav.pensjon.brev.brevoppskrift.api.PreviewRequest(record.draft, record.draft.metadata.languages.first())))
                }
                assertThat(preview.status).isEqualTo(HttpStatusCode.OK)
                assertThat(draftObjectMapper.readValue(preview.bodyAsText(), no.nav.pensjon.brev.brevoppskrift.api.PreviewResult::class.java).warnings).isNotEmpty()
                val ready = client.post("/api/v1/utkast/${record.id}/status") {
                    bearerAuth(token(groups = listOf("editors")))
                    contentType(ContentType.Application.Json)
                    setBody(draftObjectMapper.writeValueAsString(StatusRequest(DraftRevision(1), DraftStatus.READY)))
                }
                assertThat(ready.status).isEqualTo(HttpStatusCode.OK)
    }

    private fun token(
        issuer: String = azure.issuer,
        audience: String = azure.audience.value,
        caller: String? = azure.preAuthApps.first().clientId.value,
        groups: List<String> = emptyList(),
        expires: Instant = Instant.now().plusSeconds(300),
        algorithm: Algorithm = signingAlgorithm,
        omittedClaims: Set<String> = emptySet(),
        navIdent: String? = "Z000000",
        name: String? = "Synthetic User",
    ): String = JWT.create()
        .withKeyId("test-key")
        .withIssuer(issuer)
        .withAudience(audience)
        .apply {
            if ("sub" !in omittedClaims) withSubject("synthetic-user")
            if ("iat" !in omittedClaims) withIssuedAt(Date.from(Instant.now().minusSeconds(10)))
            if ("nbf" !in omittedClaims) withNotBefore(Date.from(Instant.now().minusSeconds(10)))
            if ("exp" !in omittedClaims) withExpiresAt(Date.from(expires))
        }
        .withClaim("azp", caller)
        .withClaim("NAVident", navIdent)
        .withClaim("name", name)
        .withArrayClaim("groups", groups.toTypedArray())
        .sign(algorithm)

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()

        private val keys = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        private val publicKey = keys.public as RSAPublicKey
        private val signingAlgorithm = Algorithm.RSA256(publicKey, keys.private as RSAPrivateKey)
        private fun unsigned(value: java.math.BigInteger): String =
            Base64.getUrlEncoder().withoutPadding().encodeToString(value.toByteArray().dropWhile { it == 0.toByte() }.toByteArray())

        private val jwks = JwkProvider {
            Jwk.fromValues(
                mapOf(
                    "kid" to "test-key",
                    "kty" to "RSA",
                    "alg" to "RS256",
                    "use" to "sig",
                    "n" to unsigned(publicKey.modulus),
                    "e" to unsigned(publicKey.publicExponent),
                ),
            )
        }
    }
}
