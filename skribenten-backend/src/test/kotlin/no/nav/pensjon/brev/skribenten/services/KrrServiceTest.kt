package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.ConfigFactory
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponse
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponseEnkeltperson
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoResponse
import no.nav.pensjon.brevbaker.api.model.Pid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class KrrServiceTest {
    private val config = ConfigFactory.parseMap(mapOf("scope" to "test-scope", "url" to "http://krr.test"))

    @Test
    fun `kan hente foretrukket spraak fra KRR`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf(Pid("12345") to KontaktinfoKRRResponseEnkeltperson(KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn)),
            feil = mapOf()
        )

        httpClientTest(respons) { engine ->
            val service = KrrService(
                config = config,
                authService = FakeAuthService,
                engine = engine,
            )

            val preferredLocale = service.getPreferredLocale(Pid("12345"))
            assertNull(preferredLocale.failure)
            assertEquals(SpraakKode.NN, preferredLocale.spraakKode)
        }
    }

    @Test
    fun `not found fra KRR gir not found ogsaa hos oss`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf(),
            feil = mapOf(Pid("12345") to KrrService.Feiltype.person_ikke_funnet)
        )

        httpClientTest(respons) { engine ->
            val service = KrrService(
                config = config,
                authService = FakeAuthService,
                engine = engine,
            )

            val preferredLocale = service.getPreferredLocale(Pid("12345"))
            assertNull(preferredLocale.spraakKode)
            assertEquals(KontaktinfoResponse.FailureType.NOT_FOUND, preferredLocale.failure)
        }
    }

    @Test
    fun `feil fra KRR gir feil ogsaa hos oss`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf(),
            feil = mapOf(Pid("12345") to KrrService.Feiltype.skjermet)
        )

        httpClientTest(respons) { engine ->
            val service = KrrService(
                config = config,
                authService = FakeAuthService,
                engine = engine
            )

            val preferredLocale = service.getPreferredLocale(Pid("12345"))
            assertNull(preferredLocale.spraakKode)
            assertEquals(KontaktinfoResponse.FailureType.ERROR, preferredLocale.failure)
        }
    }
}