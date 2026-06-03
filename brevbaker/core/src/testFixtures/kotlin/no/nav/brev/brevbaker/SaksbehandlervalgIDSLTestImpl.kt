package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi

class SaksbehandlervalgIDSLTestImpl(override val verdier: Map<String, SaksbehandlervalgVerdi> = emptyMap()) : SaksbehandlervalgIDSL {
    constructor(vararg verdier: Pair<String, SaksbehandlervalgVerdi>) : this(verdier.toMap())

    override fun <T : SaksbehandlervalgVerdi> get(key: String): T = verdier[key] as T
}

fun <T : Enum<T>> T.tilSaksbehandlervalgverdiEnum(displayText: String): SaksbehandlervalgVerdi.Enum<*> =
    SaksbehandlervalgVerdi.Enum(
        enum = this as SaksbehandlerValgEnum?,
        displayText = displayText,
        clazz = this::class.java
    )
