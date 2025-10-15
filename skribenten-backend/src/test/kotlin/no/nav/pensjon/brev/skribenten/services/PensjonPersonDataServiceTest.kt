package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.ConfigFactory
import io.ktor.client.engine.mock.*
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PensjonPersonDataServiceTest {

    @Test
    fun `returnerer kontaktadresse`() {
        val expected = KontaktAdresseResponseDto("Eli, Jarl og Raffen", listOf("Portveien 2"), KontaktAdresseResponseDto.Adressetype.VEGADRESSE)
        httpClientTest(expected) {
            val actual = pensjonPersonDataService(it).hentKontaktadresse("1234")
            assertThat(actual).isEqualTo(ServiceResult.Ok(expected))
        }
    }

    @Test
    fun `returerer null om person ikke har adresse`() = httpClientTest(null) { engine ->
        val actual = pensjonPersonDataService(engine).hentKontaktadresse("1234")
        assertThat(actual).isEqualTo(ServiceResult.Ok(null))
    }

    private fun pensjonPersonDataService(engine: MockEngine) = PensjonPersonDataService(
        ConfigFactory.parseMap(mapOf("url" to "http://localhost", "scope" to "fri tilgang")),
        FakeAuthService,
        engine,
    )

}