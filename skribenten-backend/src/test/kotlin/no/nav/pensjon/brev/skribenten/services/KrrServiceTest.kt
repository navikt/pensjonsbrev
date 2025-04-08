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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KrrServiceTest {

    @Test
    fun `kan kontakte KRR`() {
        val respons = KontaktinfoKRRResponse(
            personer = mapOf("12345" to KontaktinfoKRRResponseEnkeltperson(KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn)),
            feil = mapOf()
        )
        val service = spyk(
            KrrService(
                config = mockk(),
                authService = mockk(),
                client = mockk()
            )
        ).also {
            coEvery { it.doPost(any(), any()) } returns mockk<HttpResponse>().also {
                every { it.status } returns HttpStatusCode.OK
                coEvery { it.body<KontaktinfoKRRResponse>() } returns respons
            }
        }

        runBlocking {
            val preferredLocale = service.getPreferredLocale("12345")
            assertEquals(SpraakKode.NN, preferredLocale.spraakKode)
        }
    }
}