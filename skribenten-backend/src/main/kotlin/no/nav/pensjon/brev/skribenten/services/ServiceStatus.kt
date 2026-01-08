package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

interface ServiceStatus {
    suspend fun ping(): PingResult

    data class PingResult(val name: String, val status: HttpStatusCode, val body: String)
}

suspend fun ping(name: String, block: suspend () -> HttpResponse): ServiceStatus.PingResult =
    block().let { response ->
        ServiceStatus.PingResult(name, response.status, response.bodyAsText())
    }

data class StatusResponse(
    val overall: Boolean,
    val services: Map<String, Boolean>,
    val errors: Map<String, String>,
)

fun Route.setupServiceStatus(vararg services: ServiceStatus) {
    get("/status") {
        call.respond(services.checkStatuses())
    }
}


private suspend fun Array<out ServiceStatus>.checkStatuses(): StatusResponse {
    val pingResponses = withContext(Dispatchers.IO) {
        this@checkStatuses.map { async { it.ping() } }
    }.awaitAll()

    val results = pingResponses.associate { it.name to it.status.isSuccess() }
    val errors = pingResponses.filter { !it.status.isSuccess() }
        .associate { it.name to "${it.status.value}: ${it.body}" }

    return StatusResponse(
        overall = results.values.all { it },
        services = results,
        errors = errors,
    )
}
