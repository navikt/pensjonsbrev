package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import no.nav.pensjon.brev.template.SaksbehandlervalgDeklarasjon

@BrevbakerDSLInternal
class SaksbehandlervalgIDSLImpl(val verdier: Map<String, SaksbehandlervalgVerdi>, val fraMalen: SaksbehandlervalgDeklarasjon) : SaksbehandlervalgIDSL {
    fun <T : SaksbehandlervalgVerdi> get(key: String) = (verdier[key] ?: fraMalen[key]) as T
}