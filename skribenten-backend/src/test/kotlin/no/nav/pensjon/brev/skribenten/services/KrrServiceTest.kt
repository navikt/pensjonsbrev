package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponse
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoKRRResponseEnkeltperson
import no.nav.pensjon.brev.skribenten.services.KrrService.KontaktinfoResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class KrrServiceTest {
    private val service = spyk(
        KrrService(
            config = mockk(),
            authService = mockk(),
            client = mockk()
        )
    )

    @Test
    fun `kan hente foretrukket spraak fra KRR`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf("12345" to KontaktinfoKRRResponseEnkeltperson(KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn)),
            feil = mapOf()
        )

        coEvery { service.doPost(any(), any()) } returns mockk<HttpResponse>().also {
            every { it.status } returns HttpStatusCode.OK
            coEvery { it.body<KontaktinfoKRRResponse>() } returns respons
        }

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

        coEvery { service.doPost(any(), any()) } returns mockk<HttpResponse>().also {
            every { it.status } returns HttpStatusCode.OK
            coEvery { it.body<KontaktinfoKRRResponse>() } returns respons
        }

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

        coEvery { service.doPost(any(), any()) } returns mockk<HttpResponse>().also {
            every { it.status } returns HttpStatusCode.OK
            coEvery { it.body<KontaktinfoKRRResponse>() } returns respons
        }

        runBlocking {
            val preferredLocale = service.getPreferredLocale("12345")
            assertNull(preferredLocale.spraakKode)
            assertEquals(KontaktinfoResponse.FailureType.ERROR, preferredLocale.failure)
        }
    }
}