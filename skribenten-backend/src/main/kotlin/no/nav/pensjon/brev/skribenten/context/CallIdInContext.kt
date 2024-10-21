package no.nav.pensjon.brev.skribenten.context

import io.ktor.callid.*

object CallIdInContext : ContextValue<String> by ContextValueProvider(KtorCallIdContextElement, "CallId", KtorCallIdContextElement::callId)