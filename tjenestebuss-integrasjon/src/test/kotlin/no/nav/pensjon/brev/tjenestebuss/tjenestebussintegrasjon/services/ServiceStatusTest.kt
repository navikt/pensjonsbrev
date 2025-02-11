package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.createTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class ServiceStatusTest {
    private fun serviceStatusApp(
        vararg services: ServiceStatus,
        block: suspend (client: HttpClient) -> Unit,
    ): Unit =
        createTestApplication { client ->
            application {
                routing {
                    setupServiceStatus(*services)
                }
            }
            block(client)
        }

    @Test
    fun `status responds`() =
        serviceStatusApp { client ->
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(StatusResponse(true, emptyMap(), emptyMap()), response.body<StatusResponse>())
        }

    private object AvailableService : ServiceStatus {
        override val name = "AvailService"

        override suspend fun ping(): Boolean = true
    }

    private object UnavailableService : ServiceStatus {
        override val name = "UnavailService"

        override suspend fun ping(): Boolean = false
    }

    private class ServiceFailure : Exception("can't talk right now")

    private object FailingService : ServiceStatus {
        override val name = "FailingService"

        override suspend fun ping(): Boolean = throw ServiceFailure()
    }

    @Test
    fun `status for one available service`() =
        serviceStatusApp(AvailableService) { client ->
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(StatusResponse(true, mapOf(AvailableService.name to true), emptyMap()), response.body<StatusResponse>())
        }

    @Test
    fun `status for one unavailable service`() =
        serviceStatusApp(UnavailableService) { client ->
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(StatusResponse(false, mapOf(UnavailableService.name to false), emptyMap()), response.body<StatusResponse>())
        }

    @Test
    fun `status for one available and one unavailable service`() =
        serviceStatusApp(AvailableService, UnavailableService) { client ->
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(StatusResponse(false, mapOf(UnavailableService.name to false, AvailableService.name to true), emptyMap()), response.body<StatusResponse>())
        }

    @Test
    fun `status for one failing and one available service`() =
        serviceStatusApp(AvailableService, FailingService) { client ->
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                StatusResponse(false, mapOf(FailingService.name to false, AvailableService.name to true), mapOf(FailingService.name to "can't talk right now")),
                response.body<StatusResponse>(),
            )
        }
}
