package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.GitHubConfig
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.junit.jupiter.api.Test
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.Base64

class GitHubAppAuthTest {
    @Test
    fun `PKCS1 and PKCS8 keys sign time bounded assertions for restricted installation tokens`(): Unit = runBlocking {
        val pair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        val pkcs8 = pair.private.encoded
        val pkcs1 = PrivateKeyInfo.getInstance(pkcs8).parsePrivateKey().toASN1Primitive().encoded
        val now = Instant.parse("2026-01-02T03:04:05Z")
        for (pem in listOf(pem("PRIVATE KEY", pkcs8), pem("RSA PRIVATE KEY", pkcs1))) {
            var calls = 0
            val authentication = GitHubAppAuth(
                GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), pem), Clock.fixed(now, ZoneOffset.UTC),
            ) { path, body, assertion ->
                calls++
                assertThat(path).isEqualTo("/app/installations/2/access_tokens")
                val jwt = JWT.decode(assertion)
                Algorithm.RSA256(pair.public as RSAPublicKey, null).verify(jwt)
                assertThat(jwt.algorithm).isEqualTo("RS256")
                assertThat(jwt.issuer).isEqualTo("1")
                assertThat(jwt.issuedAt.toInstant()).isEqualTo(now.minusSeconds(60))
                assertThat(jwt.expiresAt.toInstant()).isEqualTo(now.plusSeconds(540))
                assertThat(body).isEqualTo(mapOf(
                    "repositories" to listOf("pensjonsbrev"),
                    "permissions" to mapOf("contents" to "write", "pull_requests" to "write"),
                ))
                draftObjectMapper.readTree("""{"token":"synthetic-token"}""")
            }
            assertThat(authentication.accessToken()).isEqualTo("synthetic-token")
            assertThat(calls).isEqualTo(1)
        }
    }

    @Test
    fun `invalid installation tokens are sanitized without retrying the request`(): Unit = runBlocking {
        for (body in listOf("""{"token":""}""", """{"token":42}""", """{"token":"unsafe\nheader"}""",
            draftObjectMapper.writeValueAsString(mapOf("token" to "x".repeat(8193))))) {
            var calls = 0
            val authentication = GitHubAppAuth(GitHubPrTest.appConfig, Clock.systemUTC()) { _, _, _ ->
                calls++
                draftObjectMapper.readTree(body)
            }
            assertThat(runCatching { authentication.accessToken() }.exceptionOrNull())
                .isInstanceOf(IntegrationFailure::class.java).hasMessage("GITHUB_AUTH_FAILED")
            assertThat(calls).isEqualTo(1)
        }
    }

    private fun pem(type: String, bytes: ByteArray) = "-----BEGIN $type-----\n${Base64.getEncoder().encodeToString(bytes)}\n-----END $type-----"
}
