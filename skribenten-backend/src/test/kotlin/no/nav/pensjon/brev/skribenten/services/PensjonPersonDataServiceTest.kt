package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.ConfigFactory
import io.ktor.callid.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.auth.TokenResponse
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PensjonPersonDataServiceTest {

    private val authService = object : AuthService {
        override suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String) =
            TokenResponse.OnBehalfOfToken("token", "refresh", "Bearer", "scope", 12345L)
    }

    private val principal = MockPrincipal(NavIdent("veldig kul ansatt"), "Veldig kult navn")

    @Test
    fun `returnerer kontaktadresse`() {
        runBlocking {
            withCallId("veldig-kul-id") {
                val expected = KontaktAdresseResponseDto("Eli, Jarl og Raffen", listOf("Portveien 2"), KontaktAdresseResponseDto.Adressetype.VEGADRESSE)
                val actual = withPrincipal(principal) { mockResponse(expected).hentKontaktadresse("1234") }

                assertThat(actual).isEqualTo(ServiceResult.Ok(expected))
            }
        }
    }

    @Test
    fun `returerer null om person ikke har adresse`() {
        runBlocking {
            withCallId("veldig-kul-id") {
                val actual = withPrincipal(principal) { mockResponse(null, HttpStatusCode.NotFound).hentKontaktadresse("1234") }
                assertThat(actual).isEqualTo(ServiceResult.Ok(null))
            }
        }
    }

    private fun mockResponse(adresse: KontaktAdresseResponseDto?, status: HttpStatusCode = HttpStatusCode.OK) = PensjonPersonDataService(
        ConfigFactory.parseMap(mapOf("url" to "http://localhost", "scope" to "fri tilgang")),
        authService,
        MockEngine {
            respond(
                content = adresse?.let { jacksonObjectMapper().writeValueAsString(it) } ?: "",
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        },
    )
}