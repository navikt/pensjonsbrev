package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.services.KrrService.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class KrrServiceTest {
    private val config = OboClientConfig(url = "http://krr.test", scope = "test-scope")

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