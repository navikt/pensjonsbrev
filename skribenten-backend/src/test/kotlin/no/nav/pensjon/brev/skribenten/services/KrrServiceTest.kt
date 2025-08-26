package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponse
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponseEnkeltperson
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class KrrServiceTest {

    @Test
    fun `kan hente foretrukket spraak fra KRR`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf("12345" to KontaktinfoKRRResponseEnkeltperson(KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn)),
            feil = mapOf()
        )

        val service = KrrService(
            config = ConfigFactory.parseMap(mapOf()),
            authService = FakeAuthService,
            client = settOppHttpClient(respons)
        )

        runBlocking {
            val preferredLocale = service.getPreferredLocale("12345")
            assertNull(preferredLocale.failure)
            assertEquals(SpraakKode.NN, preferredLocale.spraakKode)
        }
    }

    @Test
    fun `not found fra KRR gir not found ogsaa hos oss`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf(),
            feil = mapOf("12345" to KrrService.Feiltype.person_ikke_funnet)
        )

        val service = KrrService(
            config = ConfigFactory.parseMap(mapOf()),
            authService = FakeAuthService,
            client = settOppHttpClient(respons)
        )

        runBlocking {
            val preferredLocale = service.getPreferredLocale("12345")
            assertNull(preferredLocale.spraakKode)
            assertEquals(KontaktinfoResponse.FailureType.NOT_FOUND, preferredLocale.failure)
        }
    }

    @Test
    fun `feil fra KRR gir feil ogsaa hos oss`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf(),
            feil = mapOf("12345" to KrrService.Feiltype.skjermet)
        )

        val service = KrrService(
            config = ConfigFactory.parseMap(mapOf()),
            authService = FakeAuthService,
            client = settOppHttpClient(respons)
        )

        runBlocking {
            val preferredLocale = service.getPreferredLocale("12345")
            assertNull(preferredLocale.spraakKode)
            assertEquals(KontaktinfoResponse.FailureType.ERROR, preferredLocale.failure)
        }
    }
}