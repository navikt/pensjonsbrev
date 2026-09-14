package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.common.InMemoryCache
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SamhandlerServiceTest {
    private val config = OboClientConfig(url = "http://samhandler.test", scope = "test-scope")

    private val samhandler = SamhandlerServiceHttp.SamhandlerEnkel(
        navn = "Advokat Handler AS",
        samhandlerType = "ADVO",
        offentligId = "987654321",
        idType = "ORGNR",
    )

    @Test
    fun `navn og type deler ett cachet kall mot samhandler-proxy`() {
        httpClientTest(samhandler) { engine ->
            val service = SamhandlerServiceHttp(
                config = config,
                authService = FakeAuthService,
                cache = InMemoryCache(),
                engine = engine,
            )

            assertEquals("Advokat Handler AS", service.hentSamhandlerNavn("80000123456"))
            assertEquals("ADVO", service.hentSamhandlerType("80000123456"))

            assertEquals(1, engine.requestHistory.count { it.url.encodedPath.contains("hentSamhandlerEnkel") })
        }
    }
}
