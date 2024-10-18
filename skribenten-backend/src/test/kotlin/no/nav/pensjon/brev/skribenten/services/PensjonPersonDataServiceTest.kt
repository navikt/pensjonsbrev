package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.ConfigFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.TokenResponse
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PensjonPersonDataServiceTest {

    private val mockAdService = mockk<AzureADService> {
        coEvery { getOnBehalfOfToken(any(), any()) } returns TokenResponse.OnBehalfOfToken("token", "refresh", "Bearer", "scope", 12345L)
    }

    private val principal = mockk<UserPrincipal> {
        every {
            navIdent()
        } returns NavIdent("veldig kul ansatt")
        every { navIdent } returns "veldig kul ansatt"
    }

    @Test
    fun `returnerer kontaktadresse`() {
        runBlocking {
            val expected = KontaktAdresseResponseDto("Eli, Jarl og Raffen", listOf("Portveien 2"), KontaktAdresseResponseDto.Adressetype.VEGADRESSE)
            val actual = withPrincipal(principal) { mockResponse(expected).hentKontaktadresse("1234") }

            assertThat(actual).isEqualTo(ServiceResult.Ok(expected))
        }
    }

    @Test
    fun `returerer null om person ikke har adresse`() {
        runBlocking {
            val actual = withPrincipal(principal) { mockResponse(null, HttpStatusCode.NotFound).hentKontaktadresse("1234") }

            assertThat(actual).isEqualTo(ServiceResult.Ok(null))
        }
    }

    private fun mockResponse(adresse: KontaktAdresseResponseDto?, status: HttpStatusCode = HttpStatusCode.OK) = PensjonPersonDataService(
        ConfigFactory.parseMap(mapOf("url" to "http://localhost", "scope" to "fri tilgang")),
        mockAdService,
        MockEngine {
            respond(
                content = adresse?.let { jacksonObjectMapper().writeValueAsString(it) } ?: "",
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        },
    )
}