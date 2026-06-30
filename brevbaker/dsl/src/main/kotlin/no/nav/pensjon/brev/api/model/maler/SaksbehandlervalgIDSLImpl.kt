package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.template.BrevbakerDSLInternal

@BrevbakerDSLInternal
class SaksbehandlervalgIDSLImpl(val verdier: Map<String, EttSaksbehandlervalgIDSLImpl<*>>) : SaksbehandlervalgIDSL {
    fun <T> get(key: String): T = (verdier[key])?.get() as T
}

@BrevbakerDSLInternal
class EttSaksbehandlervalgIDSLImpl<T>(val key: String, val verdi: T?, val fraMalen: SaksbehandlervalgVerdi<T?>) {
    fun get() = verdi ?: fraMalen.defaultValue
}