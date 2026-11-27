package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.JsonNode
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import java.io.StringReader
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Clock
import java.time.Instant
import java.util.Date

internal class GitHubAppAuth(
    config: GitHubConfig,
    private val clock: Clock,
    private val requestToken: suspend (path: String, body: Map<String, Any>, assertion: String) -> JsonNode,
) {
    init { require(config.enabled) { "GitHub authentication is required" } }
    private val key = privateKey(requireNotNull(config.privateKey))
    private val appId = requireNotNull(config.appId).value
    private val installationId = requireNotNull(config.installationId).value

    suspend fun accessToken(): String {
        val now = Instant.now(clock)
        val assertion = JWT.create().withIssuer(appId).withIssuedAt(Date.from(now.minusSeconds(60)))
            .withExpiresAt(Date.from(now.plusSeconds(540))).sign(Algorithm.RSA256(null, key))
        return requestToken(
            "/app/installations/$installationId/access_tokens",
            mapOf("repositories" to listOf("pensjonsbrev"), "permissions" to mapOf("contents" to "write", "pull_requests" to "write")), assertion,
        ).path("token").takeIf { it.isTextual }?.asText()?.takeIf { it.length <= 8192 && bearerToken.matches(it) }
            ?: throw IntegrationFailure("GITHUB_AUTH_FAILED")
    }

    private companion object {
        val bearerToken = Regex("[A-Za-z0-9._~+/-]+=*")

        fun privateKey(pem: String): RSAPrivateKey = try {
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
