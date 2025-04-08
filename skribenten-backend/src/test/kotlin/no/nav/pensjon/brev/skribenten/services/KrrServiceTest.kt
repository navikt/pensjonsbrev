package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import org.junit.jupiter.api.Test

class KrrServiceTest {

    private val jwtConfig = JwtConfig(
        "navn",
        "utsteder",
        "jwks url",
        "skribenten-client-id",
        "http://localhost:9991/token",
        "skribenten-secret",
        emptyList(),
        true
    )

    @Test
    fun `kan kontakte KRR`() {
        val config = com.typesafe.config.Config()
        val service = KrrService(
            config = TODO(),
            authService = AzureADService(jwtConfig)
        )

        service.getPreferredLocale("12345")
    }
}