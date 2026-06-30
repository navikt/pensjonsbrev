package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.template.BrevbakerDSLInternal

@BrevbakerDSLInternal
class SaksbehandlervalgIDSLImpl(val verdier: Map<String, EttSaksbehandlervalgIDSLImpl<*>>) : SaksbehandlervalgIDSL {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T {
        val entry = verdier[key]
            ?: throw NoSuchElementException("Ukjent saksbehandlervalg '$key'. Tilgjengelige valg: ${verdier.keys.sorted()}")
        return entry.get() as T
    }
}

@BrevbakerDSLInternal
class EttSaksbehandlervalgIDSLImpl<T>(val key: String, val verdi: T?, val fraMalen: SaksbehandlervalgVerdi<T?>) {
    fun get() = verdi ?: fraMalen.defaultValue
}