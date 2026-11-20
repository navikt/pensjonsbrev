package no.nav.pensjon.brev.brevoppskrift

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging

fun Application.configureLogging() {
    install(CallId) {
        header(HttpHeaders.XRequestId)
        generate()
        verify { it.isNotEmpty() }
    }
    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        filter(::observeCall)
    }
}
