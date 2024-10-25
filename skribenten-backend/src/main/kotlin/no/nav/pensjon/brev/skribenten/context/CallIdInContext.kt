package no.nav.pensjon.brev.skribenten.context

import io.ktor.callid.*
import io.ktor.client.plugins.api.*
import io.ktor.client.request.*

object CallIdInContext : ContextValue<String> by ContextValueProvider(KtorCallIdContextElement, "CallId", KtorCallIdContextElement::callId)

val CallIdFromContext = createClientPlugin("CallIdFromContext") {
    onRequest { request, _ ->
        val callId = CallIdInContext.require()
        request.headers {
            append("Nav-Call-Id", callId)
            append("X-Request-ID", callId)
        }
    }
}