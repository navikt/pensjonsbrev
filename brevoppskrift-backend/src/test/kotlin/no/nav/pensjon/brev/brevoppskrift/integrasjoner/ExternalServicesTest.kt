package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.*
import io.ktor.http.content.TextContent
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import java.security.KeyPairGenerator
import java.util.Base64
import java.util.UUID

class ExternalServicesTest {
    private val metrics = createMetrics()

    @AfterEach
    fun closeMetrics() { metrics.close() }

    @Test
    fun `AI proposals preserve placeholders and formatting and reject unsafe responses`(): Unit = runBlocking {
        val request = AiRequest("Du får {0} <b>beløp</b>", DraftLanguage.NB)
        for ((text, accepted) in listOf("Du får {0} <b>beløp</b>" to true, "Du får beløp" to false, "<b>{0}</i>" to false, "{0} <b>beløp</b><script>alert(1)</script>" to false)) {
            val engine = MockEngine { call ->
                val response = if (call.url.host == "metadata.google.internal") {
                    assertThat(call.headers["Metadata-Flavor"]).isEqualTo("Google")
                    """{"access_token":"synthetic-token"}"""
                } else {
                    assertThat(call.url.host).isEqualTo("aiplatform.eu.rep.googleapis.com")
                    assertThat(call.headers[HttpHeaders.Authorization]).isEqualTo("Bearer synthetic-token")
                    draftObjectMapper.writeValueAsString(mapOf("candidates" to listOf(mapOf("content" to mapOf("parts" to listOf(mapOf("text" to text)))))))
                }
                respond(response, headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }
            engine.use {
                VertexAi(it, metrics, VertexProjectId("synthetic-project"), VertexLocation("europe-west4"), VertexModelId("synthetic-model")).use { service ->
                    val result = runCatching { service.suggest(request, true) }
                    assertThat(result.isSuccess).isEqualTo(accepted)
                    if (!accepted) assertThat(result.exceptionOrNull()).isInstanceOf(IntegrationFailure::class.java)
                }
            }
        }
    }

    @Test
    fun `Slack does not receive document text names or comment content`(): Unit = runBlocking {
        val engine = MockEngine { call ->
            assertThat((call.body as TextContent).text).doesNotContain("secret-input")
            respond("ok")
        }
        engine.use {
            SlackNotifications(it, metrics, SlackWebhookUrl("https://hooks.slack.com/services/synthetic")).use { service ->
                service.send(Notification("Nytt utkast", "secret-input", 1))
            }
        }
    }

    @Test
    fun `DI closes nullable integration services without closing an externally owned engine`(): Unit = runBlocking {
        var requests = 0
        MockEngine { requests++; respond("ok") }.use { engine ->
            val ai = VertexAi(engine, metrics, VertexProjectId("synthetic-project"), VertexLocation("europe-west4"), VertexModelId("synthetic-model"))
            val slack = SlackNotifications(engine, metrics, SlackWebhookUrl("https://hooks.slack.com/services/synthetic"))
            val github = adapter(engine)
            testApplication {
                lateinit var app: Application
                application {
                    app = this
                    dependencies {
                        provide<AiService?> { ai }
                        provide<NotificationService?> { slack }
                        provide<PrService?> { github }
                    }
                }
                startApplication()
                assertThat(app.dependencies.resolve<AiService?>()).isSameAs(ai)
                assertThat(app.dependencies.resolve<NotificationService?>()).isSameAs(slack)
                assertThat(app.dependencies.resolve<PrService?>()).isSameAs(github)
            }

            assertThat(runCatching { ai.suggest(AiRequest("synthetic", DraftLanguage.NB), true) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java)
            assertThat(runCatching { slack.send(Notification("synthetic", "synthetic")) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java)
            assertThat(runCatching { github.create(ticket()) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java)
            assertThat(requests).isZero()
            assertThat(engine.coroutineContext[Job]?.isActive).isTrue()

            SlackNotifications(engine, metrics, SlackWebhookUrl("https://hooks.slack.com/services/synthetic")).use {
                it.send(Notification("synthetic", "synthetic"))
            }
            assertThat(requests).isEqualTo(1)
        }
    }

    @Test
    fun `PR adapter creates only a draft PR and restricts installation permissions`(): Unit = runBlocking {
        val requests = mutableListOf<HttpRequestData>()
        val engine = MockEngine { call ->
            requests.add(call)
            val body = when {
                call.url.encodedPath.endsWith("/access_tokens") -> {
                    val payload = draftObjectMapper.readTree((call.body as TextContent).text)
                    assertThat(payload["repositories"].map { it.asText() }).containsExactly("pensjonsbrev")
                    assertThat(payload["permissions"].fieldNames().asSequence().toList()).containsExactlyInAnyOrder("contents", "pull_requests")
                    """{"token":"synthetic-token"}"""
                }
                call.url.encodedPath.endsWith("/pulls") && call.method == HttpMethod.Get -> "[]"
                call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                call.url.encodedPath.contains("/git/ref/heads/brevoppskrift/") -> return@MockEngine respond("{}", HttpStatusCode.NotFound)
                call.url.encodedPath.contains("/contents/") -> return@MockEngine respond("{}", HttpStatusCode.NotFound)
                call.url.encodedPath.endsWith("/git/commits/base") -> """{"tree":{"sha":"base-tree"}}"""
                call.url.encodedPath.endsWith("/git/commits") -> {
                    val payload = draftObjectMapper.readTree((call.body as TextContent).text)
                    assertThat(payload.path("parents").map { it.asText() }).containsExactly("base")
                    """{"sha":"synthetic-commit"}"""
                }
                call.url.encodedPath.endsWith("/pulls") -> {
                    val payload = draftObjectMapper.readTree((call.body as TextContent).text)
                    assertThat(payload["draft"].asBoolean()).isTrue()
                    assertThat(payload["base"].asText()).isEqualTo("main")
                    """{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}"""
                }
                else -> """{"sha":"synthetic-sha"}"""
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        engine.use {
            adapter(it).use { service ->
                assertThat(service.create(ticket()).value).isEqualTo("https://github.com/navikt/pensjonsbrev/pull/1")
            }
            assertThat(requests.map { request -> request.url.encodedPath }).noneMatch { path -> path.endsWith("/merge") }
        }
    }

    @Test
    fun `PAT creates a draft PR directly without App authentication`(): Unit = runBlocking {
        val generator = KotlinDraftGenerator()
        val draft = exampleDraft()
        val submitted = ticket().copy(draft = draft.copy(metadata = draft.metadata.copy(brevkode = "EXAMPLE")))
        val generated = generator.generate(submitted.draft)
        assertThat(generated.issues).isEmpty()
        val paths = mutableListOf<String>()
        MockEngine { call ->
            paths.add(call.url.encodedPath)
            assertThat(call.url.host).isEqualTo("api.github.com")
            assertThat(call.url.encodedPath).startsWith("/repos/navikt/pensjonsbrev/")
            assertThat(call.headers[HttpHeaders.Authorization]).isEqualTo("Bearer synthetic-pat")
            val body = when {
                call.url.encodedPath.endsWith("/pulls") && call.method == HttpMethod.Get -> "[]"
                call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                call.url.encodedPath.contains("/git/ref/heads/brevoppskrift/") || call.url.encodedPath.contains("/contents/") ->
                    return@MockEngine respond("{}", HttpStatusCode.NotFound)
                call.url.encodedPath.endsWith("/git/commits/base") -> """{"tree":{"sha":"base-tree"}}"""
                call.url.encodedPath.endsWith("/git/trees") -> {
                    val tree = draftObjectMapper.readTree((call.body as TextContent).text)["tree"]
                    assertThat(tree.associate { it["path"].asText() to it["content"].asText() })
                        .isEqualTo(generated.files.associate { it.path.value to it.content })
                    assertThat(tree.map { it["path"].asText() }).anyMatch { it.endsWith("-sjekkliste.md") }
                    """{"sha":"synthetic-tree"}"""
                }
                call.url.encodedPath.endsWith("/pulls") -> {
                    val payload = draftObjectMapper.readTree((call.body as TextContent).text)
                    assertThat(payload["draft"].asBoolean()).isTrue()
                    assertThat(payload["base"].asText()).isEqualTo("main")
                    """{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}"""
                }
                else -> """{"sha":"synthetic-sha"}"""
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }.use { engine ->
            adapter(engine, config = GitHubConfig(personalAccessToken = "synthetic-pat"), generator = generator).use { service ->
                assertThat(service.create(submitted).value).endsWith("/pull/1")
            }
        }
        assertThat(paths).anyMatch { it.endsWith("/git/refs") }
        assertThat(paths).noneMatch { it.startsWith("/app/") || it.endsWith("/merge") }
    }

    @Test
    fun `PAT authentication failures and redirects do not retry leak tokens or fall back to App authentication`(): Unit = runBlocking {
        for (status in listOf(HttpStatusCode.Unauthorized, HttpStatusCode.Found)) {
            val paths = mutableListOf<String>()
            MockEngine { call ->
                paths.add(call.url.encodedPath)
                respond("synthetic-pat", status, headersOf(HttpHeaders.Location, "https://untrusted.invalid"))
            }.use { engine ->
                adapter(engine, config = GitHubConfig(personalAccessToken = "synthetic-pat")).use { service ->
                    val failure = runCatching { service.create(ticket()) }.exceptionOrNull() as IntegrationFailure
                    assertThat(failure.code).isEqualTo("GITHUB_${status.value}")
                    assertThat(failure.toString()).doesNotContain("synthetic-pat")
                }
            }
            assertThat(paths).containsExactly("/repos/navikt/pensjonsbrev/pulls")
        }
    }

    @Test
    fun `completed PR retry returns existing PR without creating a new branch`(): Unit = runBlocking {
        val paths = mutableListOf<String>()
        val engine = MockEngine { call ->
            paths.add(call.url.encodedPath)
            respond(if (call.url.encodedPath.endsWith("/access_tokens")) """{"token":"synthetic-token"}""" else """[{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}]""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        engine.use {
            adapter(it).use { service ->
                assertThat(service.create(ticket()).value).endsWith("/pull/1")
            }
            assertThat(paths).hasSize(2)
        }
    }

    @Test
    fun `unapproved output paths fail before any GitHub request`(): Unit = runBlocking {
        for (config in listOf(GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), pem), GitHubConfig(personalAccessToken = "synthetic-pat"))) {
            for (path in listOf(
                ".github/workflows/injected.kt",
                "ufoere/maler/src/main/kotlin/Synthetic.md",
                "ufoere/maler/src/test/resources/brevoppskrift/Synthetic.md",
                "ufoere/maler/src/test/resources/brevoppskrift/nested/Synthetic-sjekkliste.md",
                "ufoere/maler/src/test/resources/brevoppskrift/../Synthetic-sjekkliste.md",
                "ufoere/ufoere-api-model/src/test/resources/brevoppskrift/Synthetic-sjekkliste.md",
                "ufoere/maler/src/build/Synthetic.kt",
                "ufoere/maler/src/generated/Synthetic.kt",
            )) {
                MockEngine { error("No network request expected") }.use {
                    adapter(it, path, config).use { service ->
                        val result = runCatching { service.create(ticket()) }
                        assertThat((result.exceptionOrNull() as IntegrationFailure).code).isEqualTo("INVALID_OUTPUT_PATH")
                    }
                }
            }
        }

    }

        @Test
        fun `new drafts cannot overwrite existing unimported Kotlin files`(): Unit = runBlocking {
            val engine = MockEngine { call ->
                val body = when {
                    call.url.encodedPath.endsWith("/access_tokens") -> """{"token":"synthetic-token"}"""
                    call.url.encodedPath.endsWith("/pulls") -> "[]"
                    call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                    call.url.encodedPath.contains("/contents/") -> """{"sha":"already-exists"}"""
                    else -> error("No branch or PR creation expected")
                }
                respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }
            engine.use {
                adapter(it).use { service ->
                    val result = runCatching { service.create(ticket()) }
                    assertThat((result.exceptionOrNull() as IntegrationFailure).code).isEqualTo("OUTPUT_PATH_EXISTS")
                }
            }
        }
    @Test
    fun `ambiguous creation conflict recovers the matching existing PR`(): Unit = runBlocking {
        var lookups = 0
        var creations = 0
        val engine = MockEngine { call ->
            val body = when {
                call.url.encodedPath.endsWith("/access_tokens") -> """{"token":"synthetic-token"}"""
                call.url.encodedPath.endsWith("/pulls") && call.method == HttpMethod.Get -> if (++lookups == 1) "[]" else """[{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}]"""
                call.url.encodedPath.endsWith("/pulls") -> { creations++; return@MockEngine respond("{}", HttpStatusCode.UnprocessableEntity) }
                call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                call.url.encodedPath.contains("/git/ref/heads/brevoppskrift/") || call.url.encodedPath.contains("/contents/") -> return@MockEngine respond("{}", HttpStatusCode.NotFound)
                call.url.encodedPath.endsWith("/git/commits/base") -> """{"tree":{"sha":"base-tree"}}"""
                else -> """{"sha":"synthetic-sha"}"""
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        engine.use { adapter(it).use { service -> assertThat(service.create(ticket()).value).endsWith("/pull/1") } }
        assertThat(creations).isEqualTo(1)
        assertThat(lookups).isEqualTo(2)
    }

    @Test
    fun `lost creation response retries without creating a second PR`(): Unit = runBlocking {
        var lookups = 0
        var creations = 0
        val engine = MockEngine { call ->
            val body = when {
                call.url.encodedPath.endsWith("/access_tokens") -> """{"token":"synthetic-token"}"""
                call.url.encodedPath.endsWith("/pulls") && call.method == HttpMethod.Get -> if (++lookups == 1) "[]" else """[{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}]"""
                call.url.encodedPath.endsWith("/pulls") -> { creations++; return@MockEngine respond("", HttpStatusCode.Created) }
                call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                call.url.encodedPath.contains("/git/ref/heads/brevoppskrift/") || call.url.encodedPath.contains("/contents/") -> return@MockEngine respond("{}", HttpStatusCode.NotFound)
                call.url.encodedPath.endsWith("/git/commits/base") -> """{"tree":{"sha":"base-tree"}}"""
                else -> """{"sha":"synthetic-sha"}"""
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        engine.use {
            adapter(it).use { service ->
                val submitted = ticket()
                val first = runCatching { service.create(submitted) }
                assertThat((first.exceptionOrNull() as IntegrationFailure).code).isEqualTo("GITHUB_UNAVAILABLE")
                assertThat(service.create(submitted).value).endsWith("/pull/1")
            }
        }
        assertThat(creations).isEqualTo(1)
    }

    @Test
    fun `imported PRs use the trusted source parent even when main has conflicting changes`(): Unit = runBlocking {
        for (commits in listOf(listOf("source"), listOf("base"), listOf("source", "source", "attachment"))) {
            val original = sourceDraft("0".repeat(64))
            val reference = requireNotNull(original.source)
            val files = reference.files + (1..999).associate {
                RepositoryPath("ufoere/maler/src/main/kotlin/Dependency$it.kt") to Sha256Digest("0".repeat(64))
            }
            val imported = original.copy(
                source = reference.copy(commit = GitCommit(commits.first()), files = files),
                attachments = commits.drop(1).mapIndexed { index, commit ->
                    AttachmentCopy(AttachmentReference("copy-$index"), DraftRevision(1), original.copy(source = reference.copy(commit = GitCommit(commit), files = files)))
                },
            )
            val trees = mutableListOf<String>()
            MockEngine { call ->
                val body = when {
                    call.url.encodedPath.endsWith("/pulls") && call.method == HttpMethod.Get -> "[]"
                    call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                    call.url.encodedPath.contains("/git/trees/") -> {
                        assertThat(call.url.parameters["recursive"]).isEqualTo("1")
                        trees.add(call.url.encodedPath)
                        val entries = files.keys.map { mapOf("path" to it.value, "type" to "blob", "sha" to "a".repeat(40)) } +
                            mapOf("path" to "README.md", "type" to "blob", "sha" to (if (call.url.encodedPath.endsWith("/base")) "b" else "c").repeat(40))
                        draftObjectMapper.writeValueAsString(mapOf("truncated" to false, "tree" to entries))
                    }
                    call.url.encodedPath.contains("/contents/") -> error("Source contents must not be downloaded individually")
                    call.url.encodedPath.contains("/git/ref/heads/brevoppskrift/") -> return@MockEngine respond("{}", HttpStatusCode.NotFound)
                    call.url.encodedPath.contains("/git/commits/") -> {
                        assertThat(call.url.encodedPath.substringAfterLast('/')).isEqualTo(commits.first())
                        """{"tree":{"sha":"source-tree"}}"""
                    }
                    call.url.encodedPath.endsWith("/git/commits") && call.method == HttpMethod.Post -> {
                        val payload = draftObjectMapper.readTree((call.body as TextContent).text)
                        assertThat(payload.path("parents").map { it.asText() }).containsExactly(commits.first())
                        """{"sha":"synthetic-commit"}"""
                    }
                    call.url.encodedPath.endsWith("/pulls") -> """{"html_url":"https://github.com/navikt/pensjonsbrev/pull/1"}"""
                    else -> """{"sha":"synthetic-sha"}"""
                }
                respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }.use { engine ->
                adapter(engine, config = GitHubConfig(personalAccessToken = "synthetic-pat")).use { service ->
                    assertThat(service.create(ticket().copy(draft = imported)).value).endsWith("/pull/1")
                }
            }
            assertThat(trees).hasSize(if (commits.distinct().size == 1) 0 else commits.distinct().size)
            assertThat(trees).doesNotHaveDuplicates()
            assertThat(trees).noneMatch { it.endsWith("/base") }
        }
    }

    @Test
    fun `missing incomplete and malformed source trees fail before branch creation`(): Unit = runBlocking {
        val entry = """{"path":"ufoere/maler/src/main/kotlin/Synthetic.kt","type":"blob","sha":"${"a".repeat(40)}"}"""
        val intact = """{"truncated":false,"tree":[$entry]}"""
        val invalid = listOf(
            """{"truncated":false,"tree":[]}""" to "SOURCE_BASE_MISMATCH",
            """{"truncated":true,"tree":[]}""" to "GITHUB_TREE_INCOMPLETE",
            """{"tree":[]}""" to "GITHUB_TREE_INCOMPLETE",
            """{"truncated":false,"tree":{}}""" to "GITHUB_TREE_INVALID",
            """{"truncated":false,"tree":[{"path":"ufoere/maler/src/main/kotlin/Synthetic.kt","type":"blob","sha":""}]}""" to "GITHUB_TREE_INVALID",
            """{"truncated":false,"tree":[$entry,$entry]}""" to "GITHUB_TREE_INVALID",
            intact.replace("\"blob\"", "\"tree\"") to "SOURCE_BASE_MISMATCH",
        )
        val source = sourceDraft("0".repeat(64))
        val attachment = AttachmentCopy(AttachmentReference("other-source"), DraftRevision(1),
            source.copy(source = requireNotNull(source.source).copy(commit = GitCommit("attachment"))))
        val draft = source.copy(attachments = listOf(attachment))
        for (commit in listOf("attachment", "source")) {
            for ((response, code) in invalid) {
                MockEngine { call ->
                    assertThat(call.method).isEqualTo(HttpMethod.Get)
                    val body = when {
                        call.url.encodedPath.endsWith("/pulls") -> "[]"
                        call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                        call.url.encodedPath.contains("/git/trees/") -> if (call.url.encodedPath.endsWith("/$commit")) response else intact
                        else -> error("No branch or PR creation expected")
                    }
                    respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
                }.use { engine ->
                    adapter(engine, config = GitHubConfig(personalAccessToken = "synthetic-pat")).use { service ->
                        val failure = runCatching { service.create(ticket().copy(draft = draft)) }.exceptionOrNull()
                        assertThat((failure as IntegrationFailure).code).isEqualTo(code)
                    }
                }
            }
        }
    }

    @Test
    fun `copied attachments must share a compatible source base`(): Unit = runBlocking {
        val engine = MockEngine { call ->
            val body = when {
                call.url.encodedPath.endsWith("/access_tokens") -> """{"token":"synthetic-token"}"""
                call.url.encodedPath.endsWith("/pulls") -> "[]"
                call.url.encodedPath.endsWith("/git/ref/heads/main") -> """{"object":{"sha":"base"}}"""
                call.url.encodedPath.contains("/git/trees/") -> draftObjectMapper.writeValueAsString(mapOf(
                    "truncated" to false,
                    "tree" to listOf(mapOf("path" to "ufoere/maler/src/main/kotlin/Synthetic.kt", "type" to "blob",
                        "sha" to (if (call.url.encodedPath.endsWith("/attachment")) "b" else "a").repeat(40))),
                ))
                else -> error("No branch or PR creation expected")
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val imported = sourceDraft("0".repeat(64))
        val attachmentDraft = imported.copy(source = requireNotNull(imported.source).copy(commit = GitCommit("attachment")))
        val attachment = AttachmentCopy(AttachmentReference(UUID.randomUUID().toString()), DraftRevision(1), attachmentDraft)
        engine.use {
            adapter(it).use { service ->
                val result = runCatching { service.create(ticket().copy(draft = imported.copy(attachments = listOf(attachment)))) }
                assertThat((result.exceptionOrNull() as IntegrationFailure).code).isEqualTo("SOURCE_BASE_MISMATCH")
            }
        }
    }

    @Test
    fun `conflicting hashes across attachment copies fail before GitHub requests`(): Unit = runBlocking {
        val copies = listOf("0".repeat(64), "1".repeat(64)).map {
            AttachmentCopy(AttachmentReference(UUID.randomUUID().toString()), DraftRevision(1), sourceDraft(it))
        }
        MockEngine { error("No network request expected") }.use {
            adapter(it).use { service ->
                val result = runCatching { service.create(ticket().copy(draft = exampleDraft().copy(attachments = copies))) }
                assertThat((result.exceptionOrNull() as IntegrationFailure).code).isEqualTo("SOURCE_CHANGED")
            }
        }
    }

    private fun sourceDraft(hash: String): Draft {
        return exampleDraft().copy(source = SourceReference(
            SourceId("synthetic"), GitCommit("source"),
            mapOf(RepositoryPath("ufoere/maler/src/main/kotlin/Synthetic.kt") to Sha256Digest(hash)),
        ))
    }

    private fun ticket() = PrTicket(PrJobId(UUID.randomUUID()), DraftId(UUID.randomUUID()), DraftRevision(1), exampleDraft(), PrLeaseToken(UUID.randomUUID()), 1)
    private fun adapter(
        engine: HttpClientEngine,
        path: String = "ufoere/maler/src/main/kotlin/Synthetic.kt",
        config: GitHubConfig = GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), pem),
        generator: DraftGenerator = object : DraftGenerator { override suspend fun generate(draft: Draft) = GenerationResult(listOf(GeneratedFile(RepositoryPath(path), "synthetic output")), emptyList()) },
    ): GitHubPr {
        val catalogue = object : SourceCatalogue {
            override fun list() = emptyList<TemplateSource>()
            override fun import(sourceId: SourceId): Draft? = null
            override suspend fun checkSource(draft: Draft) = emptyList<ValidationIssue>()
        }
        return GitHubPr(engine, metrics, config, generator, catalogue)
    }

    companion object {
        private val pem = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair().private.encoded.let {
            "-----BEGIN PRIVATE KEY-----\n${Base64.getEncoder().encodeToString(it)}\n-----END PRIVATE KEY-----"
        }
    }
}
