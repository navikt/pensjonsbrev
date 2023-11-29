package no.nav.pensjon.brev.skribenten

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*

fun HeadersBuilder.callId(call: ApplicationCall) {
    call.callId?.also {
        append("Nav-Call-Id", it)
        append("X-Request-ID", it)
    }
}