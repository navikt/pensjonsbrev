package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.*
import io.ktor.utils.io.readBuffer
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.io.readByteArray
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.currentCoroutineContext
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import no.nav.pensjon.brev.brevoppskrift.api.DraftGenerator
import no.nav.pensjon.brev.brevoppskrift.api.GeneratedFile
import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.services.ClientOperation
import no.nav.pensjon.brev.brevoppskrift.services.ExternalService
import no.nav.pensjon.brev.brevoppskrift.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brev.brevoppskrift.services.metricsOperation
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import java.io.Closeable
import java.io.IOException
import java.io.StringReader
import java.nio.channels.UnresolvedAddressException
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Clock
import java.time.Instant
import java.util.Base64
import java.util.Date

class GitHubPr(
    engine: HttpClientEngine,
    metrics: MeterRegistry,
    private val config: GitHubConfig,
    private val generator: DraftGenerator,
    private val catalogue: SourceCatalogue,
    private val clock: Clock = Clock.systemUTC(),
) : PrService, Closeable {
    init { require(config.enabled) { "GitHub authentication is required" } }
    private val key = privateKey(requireNotNull(config.privateKey))
    private val client = lagHttpClient(engine, metrics, ExternalService.GITHUB)
    private val prefix = "/repos/navikt/pensjonsbrev"

    override suspend fun create(ticket: PrTicket): PrUrl {
        client.coroutineContext.ensureActive()
        if (catalogue.checkSource(ticket.draft).isNotEmpty()) throw IntegrationFailure("SOURCE_CHANGED")
        val generated = generator.generate(ticket.draft)
        if (generated.issues.isNotEmpty() || generated.files.isEmpty() || generated.files.map { it.path }.distinct().size != generated.files.size) {
            throw IntegrationFailure("GENERATION_FAILED")
        }
        val sources = sourceReferences(ticket.draft)
        val sourceFiles = sourceFiles(sources)
        if (sourceFiles.keys.any { !safePath(it) } || sources.any { !gitId.matches(it.commit.value) }) throw IntegrationFailure("INVALID_SOURCE_PATH")
        val files = generated.files.filter { file ->
            when {
                allowed(file.path) -> true
                sourceFiles[file.path] == digest(file.content.toByteArray(Charsets.UTF_8)) -> false
                else -> throw IntegrationFailure("INVALID_OUTPUT_PATH")
            }
        }
        if (files.isEmpty()) throw IntegrationFailure("GENERATION_FAILED")

        val token = accessToken()
        val branch = "brevoppskrift/${ticket.draftId.value}/r${ticket.revision.value}"
        existingPull(branch, token)?.let { return pullUrl(it) }
        val base = ticket.draft.source?.commit ?: sha(api("$prefix/git/ref/heads/main", token = token).path("object"))
        checkSourceFiles(sources, base, token)
        for (file in files.filter { it.path !in sourceFiles }) {
            if (!api("$prefix/contents/${file.path.value}?ref=${base.value}", token = token, optional = true).isNull) {
                throw IntegrationFailure("OUTPUT_PATH_EXISTS")
            }
        }
        if (!checkBranch(branch, files, base, token)) {
            val baseTree = sha(api("$prefix/git/commits/${base.value}", token = token).path("tree"))
            val tree = sha(api(
                "$prefix/git/trees", HttpMethod.Post,
                mapOf("base_tree" to baseTree.value, "tree" to files.map { mapOf("path" to it.path.value, "mode" to "100644", "type" to "blob", "content" to it.content) }), token,
            ))
            val commit = sha(api(
                "$prefix/git/commits", HttpMethod.Post,
                mapOf("message" to "Brevoppskrift: ${ticket.draft.metadata.name}", "tree" to tree.value, "parents" to listOf(base.value)), token,
            ))
            try {
                api("$prefix/git/refs", HttpMethod.Post, mapOf("ref" to "refs/heads/$branch", "sha" to commit.value), token)
            } catch (failure: IntegrationFailure) {
                if (failure.code != "GITHUB_422" || !checkBranch(branch, generated.files, base, token)) throw failure
            }
        }
        val pull = try {
            api(
                "$prefix/pulls", HttpMethod.Post,
                mapOf(
                    "title" to "Brevoppskrift: ${ticket.draft.metadata.name}", "head" to branch, "base" to "main", "draft" to true,
                    "body" to "Generert fra arbeidsutkast ${ticket.draftId.value}, revisjon ${ticket.revision.value}.\n\nKildegrunnlag: `${base.value}`. Eventuelle konflikter med main må håndteres av utvikler i PR-en.\n\nUtvikler må kontrollere registrering, fixture, feature toggle og API-kompatibilitet. Ingen automatisk merge.",
                ), token,
            )
        } catch (failure: IntegrationFailure) {
            if (failure.code != "GITHUB_422") throw failure
            existingPull(branch, token) ?: throw failure
        }
        return pullUrl(pull)
    }

    private suspend fun existingPull(branch: String, token: String): JsonNode? {
        val pulls = api("$prefix/pulls?head=navikt:${branch.encodeURLParameter()}&state=all&per_page=1", token = token)
        if (!pulls.isArray || pulls.size() > 1) throw IntegrationFailure("INVALID_PR_RESPONSE")
        return pulls.firstOrNull()
    }

    private suspend fun checkBranch(branch: String, files: List<GeneratedFile>, base: GitCommit, token: String): Boolean {
        val reference = api("$prefix/git/ref/heads/$branch", token = token, optional = true)
        if (reference.isNull) return false
        val commit = sha(reference.path("object"))
        if (files.any { digest(content(it.path, commit, token)) != digest(it.content.toByteArray(Charsets.UTF_8)) }) throw IntegrationFailure("BRANCH_CHANGED")
        val changed = api("$prefix/compare/${base.value}...${commit.value}", token = token).path("files")
        // GitHub avgrenser compare til 300 filer; et fullt resultat kan ikke bevise hele differansen.
        if (!changed.isArray || changed.size() >= 300) throw IntegrationFailure("BRANCH_CHANGED")
        val paths = files.map { it.path.value }.toSet()
        val seen = mutableSetOf<String>()
        if (changed.any {
                !it.path("filename").isTextual || it.path("filename").asText() !in paths ||
                    !seen.add(it.path("filename").asText()) || it.path("status").asText() !in setOf("added", "modified")
            }) throw IntegrationFailure("BRANCH_CHANGED")
        return true
    }

    private suspend fun accessToken(): String {
        val now = Instant.now(clock)
        val assertion = JWT.create().withIssuer(requireNotNull(config.appId).value).withIssuedAt(Date.from(now.minusSeconds(60)))
            .withExpiresAt(Date.from(now.plusSeconds(540))).sign(Algorithm.RSA256(null, key))
        return api(
            "/app/installations/${requireNotNull(config.installationId).value}/access_tokens", HttpMethod.Post,
            mapOf("repositories" to listOf("pensjonsbrev"), "permissions" to mapOf("contents" to "write", "pull_requests" to "write")), assertion,
        ).path("token").takeIf { it.isTextual }?.asText()?.takeIf { it.length <= 8192 && bearerToken.matches(it) }
            ?: throw IntegrationFailure("GITHUB_AUTH_FAILED")
    }

    private fun pullUrl(pull: JsonNode): PrUrl =
        pull.path("html_url").takeIf { it.isTextual }?.asText()?.takeIf { Regex("https://github.com/navikt/pensjonsbrev/pull/[1-9][0-9]*").matches(it) }
            ?.let(::PrUrl) ?: throw IntegrationFailure("INVALID_PR_RESPONSE")

    private fun sourceReferences(draft: Draft, depth: Int = 0): List<SourceReference> {
        if (depth > 20) throw IntegrationFailure("SOURCE_CHANGED")
        return listOfNotNull(draft.source) + draft.attachments.flatMap { sourceReferences(it.draft, depth + 1) }
    }

    private fun sourceFiles(sources: List<SourceReference>): Map<RepositoryPath, Sha256Digest> {
        val files = mutableMapOf<RepositoryPath, Sha256Digest>()
        for (source in sources) {
            for (file in source.files) {
                val previous = files.put(file.key, file.value)
                if (previous != null && previous != file.value) throw IntegrationFailure("SOURCE_CHANGED")
            }
        }
        return files
    }

    private suspend fun checkSourceFiles(sources: List<SourceReference>, base: GitCommit, token: String) {
        val olderSources = sources.filter { it.commit != base }.groupBy { it.commit }
        if (olderSources.isEmpty()) return
        val current = sourceTree(base, token)
        for (source in olderSources) {
            val original = sourceTree(source.key, token)
            for (path in source.value.flatMap { it.files.keys }.toSet()) {
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
        for (entry in entries) {
            val path = entry.path("path")
            val sha = entry.path("sha")
            if (!path.isTextual || path.asText().isBlank() || !sha.isTextual || !gitId.matches(sha.asText()) || !paths.add(path.asText())) {
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
        if (response.path("encoding").asText() != "base64" || !response.path("content").isTextual) throw IntegrationFailure("BRANCH_CHANGED")
        return try {
            Base64.getDecoder().decode(response.path("content").asText().replace("\n", "").replace("\r", ""))
        } catch (_: IllegalArgumentException) {
            throw IntegrationFailure("BRANCH_CHANGED")
        }
    }

    private suspend fun api(path: String, method: HttpMethod = HttpMethod.Get, body: Any? = null, token: String, optional: Boolean = false): JsonNode {
        try {
            return client.prepareRequest("https://api.github.com$path") {
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
                if (body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(draftObjectMapper.writeValueAsString(body))
                }
            }.execute { response ->
                if (optional && response.status == HttpStatusCode.NotFound) return@execute draftObjectMapper.nullNode()
                if (response.status == HttpStatusCode.Forbidden &&
                    (response.headers["X-RateLimit-Remaining"] == "0" || response.headers[HttpHeaders.RetryAfter] != null)
                ) throw IntegrationFailure("GITHUB_429")
                if (!response.status.isSuccess()) throw IntegrationFailure("GITHUB_${response.status.value}")
                val maxBytes = if (path.startsWith("$prefix/git/trees/")) 8 * 1024 * 1024 else 1024 * 1024
                val bytes = response.bodyAsChannel().readBuffer(maxBytes.toLong() + 1).readByteArray()
                if (bytes.size > maxBytes) throw IntegrationFailure("GITHUB_UNAVAILABLE")
                draftObjectMapper.readTree(bytes)?.takeUnless { it.isNull || it.isMissingNode } ?: throw IntegrationFailure("GITHUB_UNAVAILABLE")
            }
        } catch (_: HttpRequestTimeoutException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("GITHUB_TIMEOUT")
        } catch (_: ConnectTimeoutException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("GITHUB_TIMEOUT")
        } catch (_: SocketTimeoutException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("GITHUB_TIMEOUT")
        } catch (_: IOException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("GITHUB_UNAVAILABLE")
        } catch (_: UnresolvedAddressException) {
            currentCoroutineContext().ensureActive()
            throw IntegrationFailure("GITHUB_UNAVAILABLE")
        }
    }

    private fun sha(node: JsonNode): GitCommit =
        node.path("sha").takeIf { it.isTextual }?.asText()?.takeIf(gitId::matches)?.let(::GitCommit) ?: throw IntegrationFailure("GITHUB_UNAVAILABLE")

    private fun safePath(path: RepositoryPath): Boolean =
        Regex("[A-Za-z0-9_./-]+").matches(path.value) && path.value.split('/').none { it in setOf("", ".", "..") }

    private fun allowed(path: RepositoryPath): Boolean = safePath(path) &&
        (path.value.endsWith(".kt") || Regex("(ufoere/maler|pensjon/maler|alder/maler|planlegge-pensjon-maler)/src/test/resources/brevoppskrift/[A-Za-z_][A-Za-z0-9_]*-sjekkliste\\.md").matches(path.value)) &&
        listOf("ufoere/maler/src/", "ufoere/ufoere-api-model/src/", "pensjon/maler/src/", "pensjon/pensjon-api-model/src/", "alder/maler/src/", "alder/alder-api-model/src/", "planlegge-pensjon-maler/src/").any(path.value::startsWith) &&
        !path.value.contains("/build/") && !path.value.contains("/generated/")

    private fun digest(bytes: ByteArray): Sha256Digest =
        Sha256Digest(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })

    override fun close() { client.close() }

    companion object {
        private val gitId = Regex("(?:[a-f0-9]{40}|[a-f0-9]{64})")
        private val bearerToken = Regex("[A-Za-z0-9._~+/-]+=*")

        private fun privateKey(pem: String): RSAPrivateKey = try {
            val info = PEMParser(StringReader(pem)).use { parser ->
                val parsed = parser.readObject()
                require(parser.readObject() == null)
                when (parsed) {
                    is PEMKeyPair -> parsed.privateKeyInfo
                    is PrivateKeyInfo -> parsed
                    else -> throw IllegalArgumentException()
                }
            }
            val key = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(info.encoded)) as RSAPrivateKey
            require(key.modulus.bitLength() >= 2048)
            key
        } catch (_: Exception) {
            throw IllegalArgumentException("GitHub private key must be an unencrypted RSA PEM key of at least 2048 bits")
        }
    }
}
