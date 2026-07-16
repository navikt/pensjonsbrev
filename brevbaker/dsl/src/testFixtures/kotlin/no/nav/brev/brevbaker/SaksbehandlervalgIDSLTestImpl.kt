package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

class SaksbehandlervalgIDSLTestImpl : SaksbehandlervalgIDSL, LinkedHashMap<String, Any?>()

fun lagSaksbehandlervalg(vararg verdier: Pair<String, *>) = lagSaksbehandlervalg(verdier.toMap())
fun lagSaksbehandlervalg(verdier: Map<String, *> = emptyMap<String, Any>()) = SaksbehandlervalgIDSLTestImpl().also { it.putAll(verdier) }