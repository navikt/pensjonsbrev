package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.katalog.CatalogueSnapshot
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

class GitHubPrTest {
    private val metrics = createMetrics()
    private val files = listOf(GeneratedFile(RepositoryPath(outputPath), "synthetic output"))

    @AfterEach
    fun closeMetrics() { metrics.close() }

    @Test
    fun `App supports PKCS1 and PKCS8 keys with restricted installation permissions and draft PRs`(): Unit = runBlocking {
        val pair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        val pkcs8 = pair.private.encoded
        val pkcs1 = PrivateKeyInfo.getInstance(pkcs8).parsePrivateKey().toASN1Primitive().encoded
        for (pem in listOf(pem("PRIVATE KEY", pkcs8), pem("RSA PRIVATE KEY", pkcs1))) {
            val api = FakeGitHub()
            api.engine().use { engine ->
                adapter(engine, GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), pem)).use { service ->
                    val submitted = ticket()
                    assertThat(service.create(submitted).value).isEqualTo(prUrl)
                    val auth = api.requests.single { it.url.encodedPath.endsWith("/access_tokens") }
                    val jwt = JWT.require(Algorithm.RSA256(pair.public as RSAPublicKey, null)).withIssuer("1").build()
                        .verify(auth.headers[HttpHeaders.Authorization]!!.removePrefix("Bearer "))
                    assertThat(jwt.expiresAt.time - jwt.issuedAt.time).isEqualTo(600_000)
                    assertThat(payload(auth)["repositories"].map { it.asText() }).containsExactly("pensjonsbrev")
                    assertThat(payload(auth)["permissions"].fieldNames().asSequence().toList()).containsExactlyInAnyOrder("contents", "pull_requests")
                    assertThat(payload(auth)["permissions"]["contents"].asText()).isEqualTo("write")
                    val commit = api.requests.single { it.url.encodedPath.endsWith("/git/commits") }
                    assertThat(payload(commit)["parents"].map { it.asText() }).containsExactly(main)
                    val tree = payload(api.requests.single { it.url.encodedPath.endsWith("/git/trees") })
                    assertThat(tree["base_tree"].asText()).isEqualTo(baseTree)
                    assertThat(tree["tree"].single()["mode"].asText()).isEqualTo("100644")
                    val pull = api.requests.single { it.method == HttpMethod.Post && it.url.encodedPath.endsWith("/pulls") }
                    assertThat(payload(pull)["draft"].asBoolean()).isTrue()
                    assertThat(payload(pull)["base"].asText()).isEqualTo("main")
                    assertThat(payload(pull)["head"].asText()).isEqualTo("brevoppskrift/${submitted.draftId.value}/r${submitted.revision.value}")
                    assertThat(payload(pull)["body"].asText()).contains(main, "Ingen automatisk merge")
                    assertThat(api.requests).noneMatch { it.url.encodedPath.endsWith("/merge") }
                }
            }
        }
    }

    @Test
    fun `App uses only the fixed repository and API headers without leaking credentials into metrics`(): Unit = runBlocking {
        val api = FakeGitHub()
        api.engine().use { engine ->
            adapter(engine).use { assertThat(it.create(ticket()).value).isEqualTo(prUrl) }
        }
        for (request in api.requests) {
            assertThat(request.url.host).isEqualTo("api.github.com")
            if (request.url.encodedPath.startsWith("/repos/")) {
                assertThat(request.url.encodedPath).startsWith("/repos/navikt/pensjonsbrev/")
                assertThat(request.headers[HttpHeaders.Authorization]).isEqualTo("Bearer synthetic-token")
            } else {
                assertThat(request.url.encodedPath).isEqualTo("/app/installations/2/access_tokens")
            }
            assertThat(request.headers["X-GitHub-Api-Version"]).isEqualTo("2022-11-28")
        }
        val assertion = api.requests.single { it.url.encodedPath.endsWith("/access_tokens") }.headers[HttpHeaders.Authorization]!!
        assertThat(metrics.scrape()).doesNotContain("synthetic-token", assertion, outputPath, "api.github.com", "brevoppskrift/")
    }

    @Test
    fun `invalid credentials and unsafe outputs fail before transport`(): Unit = runBlocking {
        MockEngine { error("No transport expected") }.use { engine ->
            assertThatThrownBy { adapter(engine, GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), "synthetic-invalid-key")) }
                .hasMessage("GitHub private key must be an unencrypted RSA PEM key of at least 2048 bits")
            for (path in listOf(".github/workflows/injected.kt", "../injected.kt", "/ufoere/maler/src/main/kotlin/Test.kt",
                "ufoere/maler/src/../Test.kt", "ufoere/maler/src/./Test.kt", "ufoere/maler/src/generated/Test.kt",
                "ufoere/maler/src/build/Test.kt", "ufoere/maler/src/main/kotlin/Test.md", "ufoere/maler/src/test/resources/brevoppskrift/Test.md",
                "ufoere/maler/src/test/resources/brevoppskrift/nested/Test-sjekkliste.md")) {
                adapter(engine, generated = listOf(GeneratedFile(RepositoryPath(path), "synthetic"))).use {
                    assertThat(failure { it.create(ticket()) }).isEqualTo("INVALID_OUTPUT_PATH")
                }
            }
            adapter(engine, generated = files + files).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GENERATION_FAILED") }
            adapter(engine, rejectedSource = true).use { assertThat(failure { it.create(ticket()) }).isEqualTo("SOURCE_CHANGED") }
            val conflicting = sourced("b".repeat(40)).copy(attachments = listOf(
                AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), sourced("c".repeat(40), hash = "1".repeat(64))),
            ))
            adapter(engine).use { assertThat(failure { it.create(ticket(conflicting)) }).isEqualTo("SOURCE_CHANGED") }
        }
    }

    @Test
    fun `new output paths cannot overwrite unimported files but checklist exports are allowed`(): Unit = runBlocking {
        val api = FakeGitHub().apply { existingOutput = true }
        api.engine().use { engine ->
            adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("OUTPUT_PATH_EXISTS") }
        }
        assertThat(api.repositoryRequests).allMatch { it.method == HttpMethod.Get }
        val checklist = GeneratedFile(RepositoryPath("ufoere/maler/src/test/resources/brevoppskrift/Synthetic-sjekkliste.md"), "synthetic checklist")
        val clean = FakeGitHub()
        clean.engine().use { engine -> adapter(engine, generated = listOf(checklist)).use { assertThat(it.create(ticket()).value).isEqualTo(prUrl) } }
        assertThat(payload(clean.requests.single { it.url.encodedPath.endsWith("/git/trees") })["tree"].single()["content"].asText()).isEqualTo(checklist.content)
    }

    @Test
    fun `lost PR responses and creation conflicts recover without creating another PR`(): Unit = runBlocking {
        for (mode in listOf("empty", "io", "422")) {
            val api = FakeGitHub().apply { pullFailure = mode }
            api.engine().use { engine ->
                adapter(engine).use { service ->
                    val submitted = ticket()
                    if (mode == "422") assertThat(service.create(submitted).value).isEqualTo(prUrl)
                    else {
                        assertThat(failure { service.create(submitted) }).isEqualTo("GITHUB_UNAVAILABLE")
                        assertThat(service.create(submitted).value).isEqualTo(prUrl)
                    }
                }
            }
            assertThat(api.requests.count { it.method == HttpMethod.Post && it.url.encodedPath.endsWith("/pulls") }).isEqualTo(1)
            assertThat(api.requests.count { it.method == HttpMethod.Post && it.url.encodedPath.endsWith("/git/refs") }).isEqualTo(1)
        }
    }

    @Test
    fun `existing compatible branch and ref creation race never overwrite refs`(): Unit = runBlocking {
        for (race in listOf(false, true)) {
            val api = FakeGitHub().apply { branchExists = !race; refConflict = race }
            api.engine().use { engine -> adapter(engine).use { assertThat(it.create(ticket()).value).isEqualTo(prUrl) } }
            assertThat(api.requests.count { it.url.encodedPath.endsWith("/git/refs") }).isEqualTo(if (race) 1 else 0)
            assertThat(api.requests).noneMatch { it.method == HttpMethod.Patch || it.method == HttpMethod.Delete }
        }
    }

    @Test
    fun `modified unexpected renamed and truncated branch differences prevent PR creation`(): Unit = runBlocking {
        val invalidComparisons = listOf(
            """{"files":[{"filename":".github/workflows/private.yml","status":"added"}]}""",
            """{"files":[{"filename":"$outputPath","status":"renamed","previous_filename":"private.kt"}]}""",
            """{"files":{}}""",
            draftObjectMapper.writeValueAsString(mapOf("files" to List(300) { mapOf("filename" to outputPath, "status" to "modified") })),
        )
        for (comparison in invalidComparisons) {
            val api = FakeGitHub().apply { branchExists = true; compare = comparison }
            api.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("BRANCH_CHANGED") } }
            assertThat(api.repositoryRequests).allMatch { it.method == HttpMethod.Get }
        }
        val changed = FakeGitHub().apply { branchExists = true; branchContent = "changed" }
        changed.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("BRANCH_CHANGED") } }
        assertThat(changed.repositoryRequests).allMatch { it.method == HttpMethod.Get }
    }

    @Test
    fun `imported drafts keep their source parent and check each attachment commit using bounded trees`(): Unit = runBlocking {
        val original = "b".repeat(40)
        val other = "c".repeat(40)
        val dependencies = (1..999).associate { RepositoryPath("ufoere/maler/src/main/kotlin/Dependency$it.kt") to Sha256Digest("0".repeat(64)) }
        val imported = sourced(original, dependencies).copy(attachments = listOf(
            AttachmentCopy(AttachmentReference("first"), DraftRevision(1), sourced(original, dependencies)),
            AttachmentCopy(AttachmentReference("second"), DraftRevision(1), sourced(other, dependencies)),
        ))
        val api = FakeGitHub().apply {
            sourceFiles = requireNotNull(imported.source).files.keys
        }
        api.engine().use { engine -> adapter(engine).use { assertThat(it.create(ticket(imported)).value).isEqualTo(prUrl) } }
        val trees = api.requests.filter { it.url.encodedPath.contains("/git/trees/") }
        assertThat(trees.map { it.url.encodedPath.substringAfterLast('/') }).containsExactly(original, other)
        assertThat(trees).allMatch { it.url.parameters["recursive"] == "1" }
        assertThat(api.requests).noneMatch { it.url.encodedPath.endsWith("/git/ref/heads/main") || it.url.encodedPath.contains("/contents/") }
        assertThat(payload(api.requests.single { it.url.encodedPath.endsWith("/git/commits") })["parents"].map { it.asText() }).containsExactly(original)
        assertThat(payload(api.requests.single { it.method == HttpMethod.Post && it.url.encodedPath.endsWith("/pulls") })["base"].asText()).isEqualTo("main")
    }

    @Test
    fun `new template with imported attachments uses main and verifies attachment compatibility`(): Unit = runBlocking {
        val original = "b".repeat(40)
        val draft = exampleDraft().copy(attachments = listOf(
            AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), sourced(original)),
        ))
        val api = FakeGitHub()
        api.engine().use { engine -> adapter(engine).use { assertThat(it.create(ticket(draft)).value).isEqualTo(prUrl) } }
        assertThat(payload(api.requests.single { it.url.encodedPath.endsWith("/git/commits") })["parents"].map { it.asText() }).containsExactly(main)
        assertThat(api.requests.filter { it.url.encodedPath.contains("/git/trees/") }.map { it.url.encodedPath.substringAfterLast('/') }).containsExactly(main, original)
        val incompatible = FakeGitHub().apply {
            badTreeCommit = main
            badTree = """{"truncated":false,"tree":[]}"""
        }
        incompatible.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket(draft)) }).isEqualTo("SOURCE_BASE_MISMATCH") } }
        assertThat(incompatible.repositoryRequests).allMatch { it.method == HttpMethod.Get }
    }

    @Test
    fun `incomplete missing malformed and incompatible source trees fail before any write`(): Unit = runBlocking {
        val original = "b".repeat(40)
        val other = "c".repeat(40)
        val imported = sourced(original).copy(attachments = listOf(AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), sourced(other))))
        val entry = """{"path":"$outputPath","type":"blob","sha":"${"d".repeat(40)}"}"""
        val cases = listOf(
            """{"truncated":true,"tree":[]}""" to "GITHUB_TREE_INCOMPLETE",
            """{"tree":[]}""" to "GITHUB_TREE_INCOMPLETE",
            """{"truncated":false,"tree":[]}""" to "SOURCE_BASE_MISMATCH",
            """{"truncated":false,"tree":{}}""" to "GITHUB_TREE_INVALID",
            """{"truncated":false,"tree":[$entry,$entry]}""" to "GITHUB_TREE_INVALID",
            """{"truncated":false,"tree":[{"path":"$outputPath","type":"blob","sha":""}]}""" to "GITHUB_TREE_INVALID",
            """{"truncated":false,"tree":[{"path":"$outputPath","type":"blob","sha":"${"e".repeat(40)}"}]}""" to "SOURCE_BASE_MISMATCH",
        )
        for (commit in listOf(original, other)) {
            for (case in cases) {
                val api = FakeGitHub().apply { badTreeCommit = commit; badTree = case.first }
                api.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket(imported)) }).isEqualTo(case.second) } }
                assertThat(api.repositoryRequests).allMatch { it.method == HttpMethod.Get }
            }
        }
    }

    @Test
    fun `imported root keeps unchanged shared dependencies out of the PR write set`(): Unit = runBlocking {
        val catalogue = BuildSourceCatalogue()
        val draft = requireNotNull(catalogue.import(catalogue.list().single { it.id.value.contains("AdhocFeilEtteroppgjoer2023") }.id))
        val generator = KotlinDraftGenerator(catalogue)
        val generated = generator.generate(draft)
        assertThat(generated.issues).isEmpty()
        assertThat(generated.files).anyMatch { it.path.value.startsWith("brevbaker/brevdata/") }
        val api = FakeGitHub()
        api.engine().use { engine ->
            GitHubPr(engine, metrics, appConfig, generator, catalogue).use { service ->
                assertThat(service.create(ticket(draft)).value).isEqualTo(prUrl)
            }
        }
        val written = payload(api.requests.single { it.url.encodedPath.endsWith("/git/trees") })["tree"].map { it["path"].asText() }
        assertThat(written).noneMatch { it.startsWith("brevbaker/") }
        assertThat(written).contains(requireNotNull(catalogue.trustedEntry(requireNotNull(draft.source))).templatePath.value)
        val shared = generated.files.first { it.path.value.startsWith("brevbaker/brevdata/") }
        MockEngine { error("No transport expected") }.use { engine ->
            adapter(engine, generated = listOf(shared.copy(content = shared.content + "\nchanged"))).use { service ->
                assertThat(failure { service.create(ticket(draft)) }).isEqualTo("INVALID_OUTPUT_PATH")
            }
        }
    }

    @Test
    fun `reimported roots use distinct helper paths without overwriting existing helpers`(): Unit = runBlocking {
        val catalogue = BuildSourceCatalogue()
        val first = requireNotNull(catalogue.import(catalogue.list().single { it.id.value.contains("AdhocFeilEtteroppgjoer2023") }.id))
        val originalFiles = KotlinDraftGenerator(catalogue).generate(first).files
        val entry = requireNotNull(catalogue.trustedEntry(requireNotNull(first.source)))
        val reference = entry.reference.copy(commit = GitCommit("b".repeat(40)))
        val draft = first.copy(source = reference)
        val reimported = BuildSourceCatalogue.fromResource(
            draftObjectMapper.writeValueAsBytes(CatalogueSnapshot(reference.commit, listOf(entry.copy(reference = reference, draft = draft)))).inputStream(),
        )
        val api = FakeGitHub().apply { existingFiles = originalFiles.associate { it.path to it.content } }
        api.engine().use { engine ->
            GitHubPr(engine, metrics, appConfig, KotlinDraftGenerator(reimported), reimported).use { service ->
                assertThat(service.create(ticket(draft)).value).isEqualTo(prUrl)
            }
        }
        val helpers = originalFiles.filter { "/src/test/" in it.path.value }.map { it.path.value }
        val written = payload(api.requests.single { it.url.encodedPath.endsWith("/git/trees") })["tree"]
        assertThat(written.map { it["path"].asText() }).doesNotContainAnyElementsOf(helpers)
        val test = written.single { it["path"].asText().endsWith("AuthoringTest.kt") }
        assertThat(test["content"].asText()).contains("class ${test["path"].asText().substringAfterLast('/').removeSuffix(".kt")}")
        val collision = FakeGitHub().apply {
            existingFiles = KotlinDraftGenerator(reimported).generate(draft).files.associate { it.path to "unrelated existing file" }
        }
        collision.engine().use { engine ->
            GitHubPr(engine, metrics, appConfig, KotlinDraftGenerator(reimported), reimported).use { service ->
                assertThat(failure { service.create(ticket(draft)) }).isEqualTo("OUTPUT_PATH_EXISTS")
            }
        }
        assertThat(collision.repositoryRequests).allMatch { it.method == HttpMethod.Get }
    }

    @Test
    fun `rate limited forbidden responses are retryable but ordinary authorization failures are not`(): Unit = runBlocking {
        val cases = listOf(
            headersOf("X-RateLimit-Remaining", "0") to "GITHUB_429",
            headersOf(HttpHeaders.RetryAfter, "60") to "GITHUB_429",
            headersOf("X-RateLimit-Remaining", "1") to "GITHUB_403",
            headersOf() to "GITHUB_403",
        )
        for (case in cases) {
            var calls = 0
            MockEngine { calls++; respond("synthetic-private-details", HttpStatusCode.Forbidden, case.first) }.use { engine ->
                adapter(engine).use { service -> assertThat(failure { service.create(ticket()) }).isEqualTo(case.second) }
            }
            assertThat(calls).isEqualTo(1)
        }
    }

    @Test
    fun `address resolution failures are retryable without retrying transport itself`(): Unit = runBlocking {
        var calls = 0
        MockEngine { calls++; throw UnresolvedAddressException() }.use { engine ->
            adapter(engine).use { service -> assertThat(failure { service.create(ticket()) }).isEqualTo("GITHUB_UNAVAILABLE") }
        }
        assertThat(calls).isEqualTo(1)
    }

    @Test
    fun `upstream auth redirects malformed responses and response limits are sanitized without retries`(): Unit = runBlocking {
        for (status in listOf(HttpStatusCode.Unauthorized, HttpStatusCode.Found, HttpStatusCode.TooManyRequests, HttpStatusCode.BadGateway)) {
            var calls = 0
            MockEngine { calls++; respond("synthetic-private-details", status, headersOf(HttpHeaders.Location, "https://untrusted.invalid")) }.use { engine ->
                adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GITHUB_${status.value}") }
            }
            assertThat(calls).isEqualTo(1)
        }
        for (body in listOf("{private-details", "", "x".repeat(1024 * 1024 + 1))) {
            MockEngine { respond(body) }.use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GITHUB_UNAVAILABLE") } }
        }
        MockEngine { request ->
            respond(if (request.url.encodedPath.endsWith("/access_tokens")) """{"token":"synthetic-token"}""" else """[{"html_url":"https://untrusted.invalid/private"}]""")
        }.use { engine ->
            adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("INVALID_PR_RESPONSE") }
        }
        MockEngine { throw CancellationException("synthetic") }.use { engine ->
            adapter(engine).use { assertThat(runCatching { it.create(ticket()) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java) }
        }
    }

    @Test
    fun `GitHub request timeout is retryable and does not retry transport itself`(): Unit = runBlocking {
        var calls = 0
        MockEngine { calls++; delay(11.seconds); respond("unexpected") }.use { engine ->
            adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GITHUB_TIMEOUT") }
        }
        assertThat(calls).isEqualTo(1)
    }

    private fun adapter(
        engine: HttpClientEngine,
        config: GitHubConfig = appConfig,
        generated: List<GeneratedFile> = files,
        rejectedSource: Boolean = false,
    ): GitHubPr = GitHubPr(engine, metrics, config,
        object : DraftGenerator { override suspend fun generate(draft: Draft) = GenerationResult(generated, emptyList()) },
        object : SourceCatalogue {
            override fun list() = emptyList<TemplateSource>()
            override fun import(sourceId: SourceId): Draft? = null
            override suspend fun checkSource(draft: Draft) = if (rejectedSource) listOf(ValidationIssue("source", "SOURCE_CHANGED", "synthetic")) else emptyList()
        },
    )

    @Test
    fun `installation tokens and remote commit identities must be valid before writes`(): Unit = runBlocking {
        val privateKey = pem("PRIVATE KEY", KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair().private.encoded)
        val config = GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), privateKey)
        for (body in listOf("""{"token":""}""", """{"token":42}""", """{"token":"unsafe\nheader"}""",
            draftObjectMapper.writeValueAsString(mapOf("token" to "x".repeat(8193))))) {
            var calls = 0
            MockEngine { calls++; respond(body) }.use { engine ->
                adapter(engine, config).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GITHUB_AUTH_FAILED") }
            }
            assertThat(calls).isEqualTo(1)
        }
        for (body in listOf("""{"object":{}}""", """{"object":{"sha":"${"a".repeat(41)}"}}""")) {
            val api = FakeGitHub().apply { mainReference = body }
            api.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket()) }).isEqualTo("GITHUB_UNAVAILABLE") } }
            assertThat(api.repositoryRequests).allMatch { it.method == HttpMethod.Get }
        }
    }

    @Test
    fun `tree response byte limit and caller cancellation prevent writes`(): Unit = runBlocking {
        val imported = sourced("b".repeat(40)).copy(attachments = listOf(
            AttachmentCopy(AttachmentReference("copy"), DraftRevision(1), sourced("c".repeat(40))),
        ))
        val api = FakeGitHub().apply { badTreeCommit = "b".repeat(40); badTree = "x".repeat(8 * 1024 * 1024 + 1) }
        api.engine().use { engine -> adapter(engine).use { assertThat(failure { it.create(ticket(imported)) }).isEqualTo("GITHUB_UNAVAILABLE") } }
        assertThat(api.repositoryRequests).allMatch { it.method == HttpMethod.Get }
        MockEngine { delay(11.seconds); respond("unexpected") }.use { engine ->
            adapter(engine).use { service ->
                assertThat(runCatching { withTimeout(100.milliseconds) { service.create(ticket()) } }.exceptionOrNull())
                    .isInstanceOf(TimeoutCancellationException::class.java)
            }
        }
    }

    private suspend fun failure(block: suspend () -> Any): String = (runCatching { block() }.exceptionOrNull() as IntegrationFailure).code
    private fun ticket(draft: Draft = exampleDraft()) = PrTicket(PrJobId(UUID.randomUUID()), DraftId(UUID.randomUUID()), DraftRevision(1), draft, PrLeaseToken(UUID.randomUUID()), 1)
    private fun sourced(commit: String, dependencies: Map<RepositoryPath, Sha256Digest> = emptyMap(), hash: String = "0".repeat(64)) =
        exampleDraft().copy(source = SourceReference(SourceId("synthetic"), GitCommit(commit), dependencies + (RepositoryPath(outputPath) to Sha256Digest(hash))))

    private class FakeGitHub {
        val requests = mutableListOf<HttpRequestData>()
        val repositoryRequests get() = requests.filter { it.url.encodedPath.startsWith("/repos/") }
        var branchExists = false
        var pullExists = false
        var existingOutput = false
        var existingFiles: Map<RepositoryPath, String> = emptyMap()
        var refConflict = false
        var pullFailure: String? = null
        var branchContent = "synthetic output"
        var compare = """{"files":[{"filename":"$outputPath","status":"added"}]}"""
        var mainReference = """{"object":{"sha":"$main"}}"""
        var sourceFiles: Set<RepositoryPath> = setOf(RepositoryPath(outputPath))
        var badTreeCommit: String? = null
        var badTree: String? = null

        fun engine() = MockEngine { request ->
            requests.add(request)
            val path = request.url.encodedPath
            val body = when {
                path.endsWith("/access_tokens") -> """{"token":"synthetic-token"}"""
                path.endsWith("/pulls") && request.method == HttpMethod.Get -> if (pullExists) """[{"html_url":"$prUrl"}]""" else "[]"
                path.endsWith("/pulls") -> {
                    pullExists = true
                    when (pullFailure) {
                        "empty" -> return@MockEngine respond("", HttpStatusCode.Created)
                        "io" -> throw IOException("synthetic-private-details")
                        "422" -> return@MockEngine respond("private-details", HttpStatusCode.UnprocessableEntity)
                    }
                    """{"html_url":"$prUrl"}"""
                }
                path.endsWith("/git/ref/heads/main") -> mainReference
                path.contains("/git/ref/heads/brevoppskrift/") -> {
                    if (!branchExists) return@MockEngine respond("{}", HttpStatusCode.NotFound)
                    """{"object":{"sha":"$branchCommit"}}"""
                }
                path.contains("/contents/") -> {
                    if (request.url.parameters["ref"] == branchCommit) {
                        """{"encoding":"base64","content":"${Base64.getEncoder().encodeToString(branchContent.toByteArray())}"}"""
                    } else {
                        val existing = existingFiles[RepositoryPath(path.substringAfter("/contents/"))]
                        if (existing != null) {
                            draftObjectMapper.writeValueAsString(mapOf("encoding" to "base64", "content" to Base64.getEncoder().encodeToString(existing.toByteArray())))
                        } else {
                            if (!existingOutput) return@MockEngine respond("{}", HttpStatusCode.NotFound)
                            "{}"
                        }
                    }
                }
                path.contains("/compare/") -> compare
                path.contains("/git/trees/") -> {
                    if (path.endsWith("/$badTreeCommit")) requireNotNull(badTree) else draftObjectMapper.writeValueAsString(mapOf(
                        "truncated" to false,
                        "tree" to sourceFiles.map { mapOf("path" to it.value, "type" to "blob", "sha" to "d".repeat(40)) } +
                            mapOf("path" to "docs/Ærlig dokumentasjon.md", "type" to "blob", "sha" to "a".repeat(40)),
                    ))
                }
                path.contains("/git/commits/") -> """{"tree":{"sha":"$baseTree"}}"""
                path.endsWith("/git/trees") -> """{"sha":"${"f".repeat(40)}"}"""
                path.endsWith("/git/commits") -> """{"sha":"$branchCommit"}"""
                path.endsWith("/git/refs") -> {
                    branchExists = true
                    if (refConflict) return@MockEngine respond("private-details", HttpStatusCode.UnprocessableEntity)
                    "{}"
                }
                else -> error("Unexpected synthetic request")
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
    }

    companion object {
        internal val appConfig = GitHubConfig(
            GitHubAppId("1"), GitHubInstallationId("2"),
            pem("PRIVATE KEY", KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair().private.encoded),
        )
        private val main = "a".repeat(40)
        private val baseTree = "8".repeat(40)
        private val branchCommit = "9".repeat(40)
        private const val outputPath = "ufoere/maler/src/main/kotlin/Synthetic.kt"
        private const val prUrl = "https://github.com/navikt/pensjonsbrev/pull/1"
        private fun pem(type: String, bytes: ByteArray) = "-----BEGIN $type-----\n${Base64.getEncoder().encodeToString(bytes)}\n-----END $type-----"
        private fun payload(request: HttpRequestData) = draftObjectMapper.readTree((request.body as TextContent).text)
    }
}
