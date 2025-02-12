package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

interface ServiceStatus {
    val name: String

    suspend fun ping(): Boolean?
}

data class StatusResponse(val overall: Boolean, val services: Map<String, Boolean?>, val errors: Map<String, String>)

fun Routing.setupServiceStatus(vararg services: ServiceStatus) {
    get("/status") {
        call.respond(services.checkStatuses())
    }
}

private suspend fun Array<out ServiceStatus>.checkStatuses(): StatusResponse =
    associate {
        it.name to
            try {
                Result.success(it.ping())
            } catch (e: Exception) {
                Result.failure(e)
            }
    }.let { statuses ->
        val results =
            statuses.mapValues {
                if (it.value.isSuccess) {
                    it.value.getOrNull()
                } else {
                    false
                }
            }
        StatusResponse(
            overall = results.values.all { it == true },
            services = results,
            errors = statuses.toList().mapNotNull { r -> r.second.exceptionOrNull()?.message?.let { r.first to it } }.toMap(),
        )
    }
