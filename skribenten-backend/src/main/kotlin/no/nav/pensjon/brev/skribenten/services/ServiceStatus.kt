package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.statement.*
import io.ktor.http.*


interface ServiceStatus {
    suspend fun ping(): PingResult

    data class PingResult(val name: String, val status: HttpStatusCode, val body: String)
}

suspend fun ping(name: String, block: suspend () -> HttpResponse): ServiceStatus.PingResult =
    block().let { response ->
        ServiceStatus.PingResult(name, response.status, response.bodyAsText())
    }
