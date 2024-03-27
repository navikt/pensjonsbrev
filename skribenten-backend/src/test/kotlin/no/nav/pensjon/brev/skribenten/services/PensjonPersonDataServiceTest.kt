package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.typesafe.config.ConfigFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.TokenResponse
import org.junit.Test

class PensjonPersonDataServiceTest {

    private val mockAdService = mockk<AzureADService> {
        coEvery { getOnBehalfOfToken(any(), any()) } returns TokenResponse.OnBehalfOfToken("token", "refresh", "Bearer", "scope", 12345L)
    }
    private val mockCall = mockk<ApplicationCall> {
        every { callId } returns "utrolig kul callId"
    }

    @Test
    fun `returnerer kontaktadresse`() = runBlocking {
        val expected = KontaktAdresseResponseDto("Eli, Jarl og Raffen", listOf("Portveien 2"), KontaktAdresseResponseDto.Adressetype.VEGADRESSE)
        val actual = mockResponse(expected).hentKontaktadresse(mockCall, "1234")

        assertThat(actual, equalTo(ServiceResult.Ok(expected)))
    }

    @Test
    fun `returerer null om person ikke har adresse`() = runBlocking {
        val actual = mockResponse(null, HttpStatusCode.NotFound).hentKontaktadresse(mockCall, "1234")

        assertThat(actual, equalTo(ServiceResult.Ok(null)))
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