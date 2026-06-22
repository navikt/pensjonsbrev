package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi

class SaksbehandlervalgIDSLTestImpl(override val verdier: Map<String, SaksbehandlervalgVerdi> = emptyMap()) : SaksbehandlervalgIDSL {
    constructor(vararg verdier: Pair<String, SaksbehandlervalgVerdi>) : this(verdier.toMap())

    override fun <T : SaksbehandlervalgVerdi> get(key: String): T = verdier[key] as T
}

fun <T> T.tilSaksbehandlervalgverdiEnum(displayText: String): SaksbehandlervalgVerdi.Enum<*> where T : SaksbehandlerValgEnum, T: Enum<T> =
    SaksbehandlervalgVerdi.Enum(
        enum = this,
        displayText = displayText,
        clazz = this::class.java
    )

fun saksbehandlervalgVerdiBool(verdi: Boolean, displayText: String) = SaksbehandlervalgVerdi.Bool(
    bool = verdi,
    displayText = displayText
)

fun saksbehandlervalgVerdiInteger(verdi: Int?, displayText: String) = SaksbehandlervalgVerdi.Integer(
    int = verdi,
    displayText = displayText
)

fun saksbehandlervalgVerdiText(verdi: String?, displayText: String) = SaksbehandlervalgVerdi.Text(
    text = verdi,
    displayText = displayText
)