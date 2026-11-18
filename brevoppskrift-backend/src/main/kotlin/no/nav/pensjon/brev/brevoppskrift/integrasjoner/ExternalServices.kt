package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import no.nav.pensjon.brev.brevoppskrift.api.DraftGenerator
import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage
import no.nav.pensjon.brev.brevoppskrift.model.GitCommit
import no.nav.pensjon.brev.brevoppskrift.model.RepositoryPath
import no.nav.pensjon.brev.brevoppskrift.model.Sha256Digest
import no.nav.pensjon.brev.brevoppskrift.model.SourceReference
import no.nav.pensjon.brev.brevoppskrift.services.ClientOperation
import no.nav.pensjon.brev.brevoppskrift.services.ExternalService
import no.nav.pensjon.brev.brevoppskrift.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brev.brevoppskrift.services.metricsOperation
import java.io.Closeable
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.Base64
import java.util.Date

class IntegrationFailure(val code: String) : RuntimeException(code)
data class AiRequest(val text: String, val language: DraftLanguage, val instruction: String = "")
data class AiResult(val text: String)
data class Notification(val type: String, val name: String, val count: Int = 0, val url: String? = null)

interface AiService { suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult }
interface NotificationService { suspend fun send(notification: Notification) }
interface PrService { suspend fun create(ticket: PrTicket): PrUrl }

class SlackNotifications(engine: HttpClientEngine, metrics: MeterRegistry, private val webhook: SlackWebhookUrl) : NotificationService, Closeable {
    init { require(URIHost(webhook.value) == "hooks.slack.com" && webhook.value.startsWith("https://")) }
    private val client = lagHttpClient(engine, metrics, ExternalService.SLACK)
    override suspend fun send(notification: Notification) {
        val text = "${notification.type} (${notification.count})${notification.url?.let { " $it" } ?: ""}"
        val response = client.post(webhook.value) { metricsOperation(ClientOperation.NOTIFY); contentType(ContentType.Application.Json); setBody(draftObjectMapper.writeValueAsString(mapOf("text" to text))) }
        if (!response.status.isSuccess()) throw IntegrationFailure("SLACK_UNAVAILABLE")
    }

    override fun close() { client.close() }
}

class VertexAi(engine: HttpClientEngine, metrics: MeterRegistry, private val project: VertexProjectId, private val location: VertexLocation, private val model: VertexModelId) : AiService, Closeable {
    init { require(listOf(project.value, location.value, model.value).all { Regex("[a-zA-Z0-9_.-]+").matches(it) }) }
    private val client = lagHttpClient(engine, metrics, ExternalService.VERTEX)
    override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
        if (request.text.isBlank() || request.text.length > 100_000 || request.instruction.length > 1000) throw IntegrationFailure("INVALID_AI_REQUEST")
        val credentials = client.get("http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/token") { metricsOperation(ClientOperation.TOKEN); header("Metadata-Flavor", "Google") }
        if (!credentials.status.isSuccess()) throw IntegrationFailure("VERTEX_UNAVAILABLE")
        val token = draftObjectMapper.readTree(credentials.bodyAsText())["access_token"]?.asText() ?: throw IntegrationFailure("VERTEX_UNAVAILABLE")
        val instruction = if (nynorsk) "Oversett til nynorsk, med same faglege innhald." else "Forbedre klarspråket på ${request.language}. Ikke endre faglig innhold."
        val payload = mapOf(
            "systemInstruction" to mapOf("parts" to listOf(mapOf("text" to "$instruction Bevar alle {nummer}-plassholdere og <b>/<i>-markeringer. Returner bare forslagets tekst."))),
            "contents" to listOf(mapOf("role" to "user", "parts" to listOf(mapOf("text" to "${request.instruction}\n${request.text}")))),
        )
        val response = client.post("https://aiplatform.eu.rep.googleapis.com/v1/projects/${project.value}/locations/${location.value}/publishers/google/models/${model.value}:generateContent") {
            metricsOperation(ClientOperation.GENERATE)
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(draftObjectMapper.writeValueAsString(payload))
        }
        if (!response.status.isSuccess()) throw IntegrationFailure("VERTEX_UNAVAILABLE")
        val suggested = draftObjectMapper.readTree(response.bodyAsText()).path("candidates").path(0).path("content").path("parts").path(0).path("text").asText()
        if (suggested.isBlank() || suggested.length > 200_000 || markers(request.text) != markers(suggested) || !balanced(suggested) || Regex("</?[^>]+>").findAll(suggested).any { it.value !in setOf("<b>", "</b>", "<i>", "</i>") }) throw IntegrationFailure("INVALID_AI_RESPONSE")
        return AiResult(suggested)
    }

    private fun markers(text: String) = Regex("\\{\\d+}|</?[bi]>").findAll(text).map { it.value }.groupingBy { it }.eachCount()
    private fun balanced(text: String): Boolean {
        val stack = ArrayDeque<String>()
        Regex("</?([bi])>").findAll(text).forEach {
            if (!it.value.startsWith("</")) stack.addLast(it.groupValues[1])
            else if (stack.removeLastOrNull() != it.groupValues[1]) return false
        }
        return stack.isEmpty()
    }

    override fun close() { client.close() }
}

class GitHubPr(
    engine: HttpClientEngine,
    metrics: MeterRegistry,
    private val config: GitHubConfig,
    private val generator: DraftGenerator,
    private val catalogue: SourceCatalogue,
) : PrService, Closeable {
    private val key = config.privateKey?.let(::privateKey)
    init {
        require(config.appId != null || config.personalAccessToken != null) { "GitHub authentication is required" }
        config.appId?.let { require(Regex("[0-9]+").matches(it.value) && Regex("[0-9]+").matches(requireNotNull(config.installationId).value)) }
    }
    private val client = lagHttpClient(engine, metrics, ExternalService.GITHUB)
    private val prefix = "/repos/navikt/pensjonsbrev"
    override suspend fun create(ticket: PrTicket): PrUrl {
        if (catalogue.checkSource(ticket.draft).isNotEmpty()) throw IntegrationFailure("SOURCE_CHANGED")
        val generated = generator.generate(ticket.draft)
        if (generated.issues.isNotEmpty() || generated.files.isEmpty()) throw IntegrationFailure("GENERATION_FAILED")
        if (generated.files.any { !allowed(it.path) }) throw IntegrationFailure("INVALID_OUTPUT_PATH")
        val sources = sourceReferences(ticket.draft)
        val sourceFiles = sourceFiles(sources)
        if (sourceFiles.keys.any { !safePath(it) }) throw IntegrationFailure("INVALID_SOURCE_PATH")
        val token = accessToken()
        val branch = "brevoppskrift/${ticket.draftId.value}/r${ticket.revision.value}"
        val previous = api("$prefix/pulls?head=navikt:${branch.encodeURLParameter()}&state=all", token = token)
        previous.firstOrNull()?.let { return pullUrl(it) }
        val main = GitCommit(api("$prefix/git/ref/heads/main", token = token).path("object").path("sha").asText())
        val base = sources.firstOrNull()?.commit ?: main
        checkSourceFiles(sources, base, token)
        for (file in generated.files.filter { it.path !in sourceFiles }) {
            if (!api("$prefix/contents/${file.path.value}?ref=${base.value}", token = token, optional = true).isNull) throw IntegrationFailure("OUTPUT_PATH_EXISTS")
        }
        val existing = api("$prefix/git/ref/heads/$branch", token = token, optional = true)
        if (!existing.isNull) {
            val sha = GitCommit(existing.path("object").path("sha").asText())
            if (generated.files.any { digest(content(it.path, sha, token)) != digest(it.content.toByteArray()) }) throw IntegrationFailure("BRANCH_CHANGED")
            val changed = api("$prefix/compare/${base.value}...${sha.value}", token = token).path("files").map { RepositoryPath(it.path("filename").asText()) }
            if (changed.any { it !in generated.files.map { file -> file.path } }) throw IntegrationFailure("BRANCH_CHANGED")
        } else {
            val baseTree = api("$prefix/git/commits/${base.value}", token = token).path("tree").path("sha").asText()
            val tree = api("$prefix/git/trees", HttpMethod.Post, mapOf("base_tree" to baseTree, "tree" to generated.files.map { mapOf("path" to it.path.value, "mode" to "100644", "type" to "blob", "content" to it.content) }), token).path("sha").asText()
            val commit = api(
                "$prefix/git/commits", HttpMethod.Post,
                mapOf("message" to "Brevoppskrift: ${ticket.draft.metadata.name}", "tree" to tree, "parents" to listOf(base.value)), token,
            ).path("sha").asText()
            api("$prefix/git/refs", HttpMethod.Post, mapOf("ref" to "refs/heads/$branch", "sha" to commit), token)
        }
        val pull = try {
            api(
                "$prefix/pulls", HttpMethod.Post,
                mapOf("title" to "Brevoppskrift: ${ticket.draft.metadata.name}", "head" to branch, "base" to "main", "draft" to true, "body" to "Generert fra arbeidsutkast ${ticket.draftId.value}, revisjon ${ticket.revision.value}.\n\nKildegrunnlag: `${base.value}`. Eventuelle konflikter med main må håndteres av utvikler i PR-en.\n\nUtvikler må kontrollere registrering, fixture, feature toggle og API-kompatibilitet. Ingen automatisk merge."), token,
            )
        } catch (failure: IntegrationFailure) {
            if (failure.code != "GITHUB_422") throw failure
            api("$prefix/pulls?head=navikt:${branch.encodeURLParameter()}&state=all", token = token).firstOrNull() ?: throw failure
        }
        return pullUrl(pull)
    }

    private suspend fun accessToken(): String {
        config.personalAccessToken?.let { return it }
        val now = Instant.now()
        val assertion = JWT.create().withIssuer(requireNotNull(config.appId).value).withIssuedAt(Date.from(now.minusSeconds(60))).withExpiresAt(Date.from(now.plusSeconds(540))).sign(Algorithm.RSA256(null, requireNotNull(key)))
        return api("/app/installations/${requireNotNull(config.installationId).value}/access_tokens", HttpMethod.Post, mapOf("repositories" to listOf("pensjonsbrev"), "permissions" to mapOf("contents" to "write", "pull_requests" to "write")), assertion).path("token").asText()
            .takeIf { it.isNotBlank() } ?: throw IntegrationFailure("GITHUB_AUTH_FAILED")
    }

    private fun pullUrl(pull: JsonNode) = pull.path("html_url").asText().takeIf { Regex("https://github.com/navikt/pensjonsbrev/pull/[0-9]+").matches(it) }?.let(::PrUrl) ?: throw IntegrationFailure("INVALID_PR_RESPONSE")

    private fun sourceReferences(draft: Draft): List<SourceReference> =
        listOfNotNull(draft.source) + draft.attachments.flatMap { sourceReferences(it.draft) }

    private fun sourceFiles(sources: List<SourceReference>): Map<RepositoryPath, Sha256Digest> {
        val files = mutableMapOf<RepositoryPath, Sha256Digest>()
        sources.forEach { source ->
            source.files.forEach { (path, hash) ->
                val previous = files.put(path, hash)
                if (previous != null && previous != hash) throw IntegrationFailure("SOURCE_CHANGED")
            }
        }
        return files
    }

    private suspend fun checkSourceFiles(sources: List<SourceReference>, base: GitCommit, token: String) {
        // Catalogue validation binds the SHA-256 hashes to immutable source commits.
        val olderSources = sources.filter { it.commit != base }.groupBy { it.commit }
        if (olderSources.isEmpty()) return
        val current = sourceTree(base, token)
        for ((commit, references) in olderSources) {
            val original = sourceTree(commit, token)
            for (path in references.flatMap { it.files.keys }.toSet()) {
                if (original[path] == null || original[path] != current[path]) throw IntegrationFailure("SOURCE_BASE_MISMATCH")
            }
        }
    }

    private suspend fun sourceTree(commit: GitCommit, token: String): Map<RepositoryPath, String> {
        val response = api("$prefix/git/trees/${commit.value}?recursive=1", token = token)
        if (!response.path("truncated").isBoolean || response.path("truncated").asBoolean()) throw IntegrationFailure("GITHUB_TREE_INCOMPLETE")
        val entries = response.path("tree")
        if (!entries.isArray) throw IntegrationFailure("GITHUB_TREE_INVALID")
        val files = mutableMapOf<RepositoryPath, String>()
        val paths = mutableSetOf<String>()
        val blobId = Regex("(?:[a-f0-9]{40}|[a-f0-9]{64})")
        for (entry in entries) {
            val path = entry.path("path")
            val sha = entry.path("sha")
            if (!path.isTextual || path.asText().isBlank() || !sha.isTextual || !blobId.matches(sha.asText()) || !paths.add(path.asText())) {
                throw IntegrationFailure("GITHUB_TREE_INVALID")
            }
            when (entry.path("type").asText()) {
                "blob" -> files[RepositoryPath(path.asText())] = sha.asText()
                "tree", "commit" -> Unit
                else -> throw IntegrationFailure("GITHUB_TREE_INVALID")
            }
        }
        return files
    }

    private suspend fun content(path: RepositoryPath, ref: GitCommit, token: String): ByteArray {
        val response = api("$prefix/contents/${path.value}?ref=${ref.value}", token = token)
        return Base64.getMimeDecoder().decode(response.path("content").asText())
    }
    private suspend fun api(path: String, method: HttpMethod = HttpMethod.Get, body: Any? = null, token: String, optional: Boolean = false): JsonNode {
        val response = try {
            client.request("https://api.github.com$path") {
                metricsOperation(when {
                    path.startsWith("/app/") -> ClientOperation.TOKEN
                    path.startsWith("$prefix/pulls") -> ClientOperation.PULL_REQUEST
                    path.startsWith("$prefix/contents/") -> ClientOperation.CONTENT
                    path.startsWith("$prefix/git/ref") -> ClientOperation.REFERENCE
                    path.startsWith("$prefix/compare/") -> ClientOperation.COMPARE
                    path.startsWith("$prefix/git/commits") -> ClientOperation.COMMIT
                    path.startsWith("$prefix/git/trees") -> ClientOperation.TREE
                    else -> ClientOperation.REQUEST
                })
                this.method = method
                bearerAuth(token)
                header(HttpHeaders.Accept, "application/vnd.github+json")
                header("X-GitHub-Api-Version", "2022-11-28")
                if (body != null) { contentType(ContentType.Application.Json); setBody(draftObjectMapper.writeValueAsString(body)) }
            }
        } catch (_: java.io.IOException) {
            throw IntegrationFailure("GITHUB_UNAVAILABLE")
        }
        if (optional && response.status == HttpStatusCode.NotFound) return draftObjectMapper.nullNode()
        if (!response.status.isSuccess()) throw IntegrationFailure("GITHUB_${response.status.value}")
        return try {
            draftObjectMapper.readTree(response.bodyAsText())?.takeUnless { it.isMissingNode || it.isNull } ?: throw IntegrationFailure("GITHUB_UNAVAILABLE")
        } catch (_: java.io.IOException) {
            throw IntegrationFailure("GITHUB_UNAVAILABLE")
        }
    }
    private fun safePath(path: RepositoryPath) = Regex("[A-Za-z0-9_./-]+").matches(path.value) && path.value.split('/').none { it == ".." || it.isEmpty() }
    private fun allowed(path: RepositoryPath) = safePath(path) &&
        (path.value.endsWith(".kt") || Regex("(ufoere/maler|pensjon/maler|alder/maler|planlegge-pensjon-maler)/src/test/resources/brevoppskrift/[A-Za-z_][A-Za-z0-9_]*-sjekkliste\\.md").matches(path.value)) &&
        listOf("ufoere/maler/src/", "ufoere/ufoere-api-model/src/", "pensjon/maler/src/", "pensjon/pensjon-api-model/src/", "alder/maler/src/", "alder/alder-api-model/src/", "planlegge-pensjon-maler/src/").any(path.value::startsWith) &&
        !path.value.contains("/build/") && !path.value.contains("/generated/")
    private fun digest(bytes: ByteArray) = Sha256Digest(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })

    override fun close() { client.close() }
}

private fun URIHost(url: String): String? = java.net.URI(url).host

private fun privateKey(pem: String): RSAPrivateKey {
    val encoded = Base64.getMimeDecoder().decode(pem.lineSequence().filterNot { it.startsWith("-----") }.joinToString(""))
    val key = if (pem.contains("BEGIN RSA PRIVATE KEY")) {
        fun der(tag: Int, bytes: ByteArray): ByteArray {
            val length = if (bytes.size < 128) byteArrayOf(bytes.size.toByte()) else {
                val digits = generateSequence(bytes.size) { it ushr 8 }.takeWhile { it > 0 }.map(Int::toByte).toList().reversed().toByteArray()
                byteArrayOf((0x80 or digits.size).toByte()) + digits
            }
            return byteArrayOf(tag.toByte()) + length + bytes
        }
        val rsaIdentifier = byteArrayOf(0x30, 0x0d, 0x06, 0x09, 0x2a, 0x86.toByte(), 0x48, 0x86.toByte(), 0xf7.toByte(), 0x0d, 0x01, 0x01, 0x01, 0x05, 0x00)
        der(0x30, byteArrayOf(0x02, 0x01, 0x00) + rsaIdentifier + der(0x04, encoded))
    } else encoded
    return KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(key)) as RSAPrivateKey
}
