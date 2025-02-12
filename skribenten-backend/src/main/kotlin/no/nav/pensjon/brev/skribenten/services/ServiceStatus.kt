package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.TjenestebussStatus

interface ServiceStatus {
    val name: String

    suspend fun ping(): ServiceResult<Boolean>
}

data class StatusResponse(
    val overall: Boolean,
    val services: Map<String, Boolean>,
    val errors: Map<String, String>,
    val tjenestebuss: TjenestebussStatus?,
)

fun Route.setupServiceStatus(vararg services: ServiceStatus) {
    get("/status") {
        call.respond(services.checkStatuses())
    }
}

private suspend fun Array<out ServiceStatus>.checkStatuses(): StatusResponse {
    val pingResponses =
        this.associate {
            it.name to
                try {
                    it.ping()
                } catch (e: ClientRequestException) {
                    ServiceResult.Error(e.response.bodyAsText(), e.response.status)
                } catch (e: Exception) {
                    ServiceResult.Error(e.message ?: "Unknown error", HttpStatusCode.InternalServerError)
                }
        }

    val results =
        pingResponses.mapValues {
            when (val r = it.value) {
                is ServiceResult.Ok -> r.result
                is ServiceResult.Error -> false
            }
        }
    val errors =
        pingResponses.toList().mapNotNull {
            when (val e = it.second) {
                is ServiceResult.Ok -> null
                is ServiceResult.Error -> it.first to "${e.statusCode}: ${e.error}"
            }
        }.toMap()

    return StatusResponse(
        overall = results.values.all { it },
        services = results,
        errors = errors,
        tjenestebuss = this.filterIsInstance<TjenestebussIntegrasjonService>().firstOrNull()?.status()?.resultOrNull(),
    )
}
