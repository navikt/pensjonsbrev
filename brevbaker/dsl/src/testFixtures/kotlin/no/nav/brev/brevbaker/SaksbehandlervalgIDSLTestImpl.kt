@file:OptIn(InternKonstruktoer::class)

package no.nav.brev.brevbaker

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSLImpl
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import kotlin.jvm.java

fun lagSaksbehandlervalg(vararg verdier: Pair<String, SaksbehandlervalgVerdi>) = lagSaksbehandlervalg(verdier.toMap())
fun lagSaksbehandlervalg(verdier: Map<String, SaksbehandlervalgVerdi> = emptyMap()) = SaksbehandlervalgIDSLImpl(verdier, emptyMap()) // TODO

fun <T> T.tilSaksbehandlervalgverdiEnum(displayText: String): SaksbehandlervalgVerdi.Enum<*> where T : SaksbehandlerValgEnum, T: Enum<T> =
    SaksbehandlervalgVerdi.Enum(
        defaultValue = this,
        displayText = displayText,
        clazz = this::class.java
    )

fun saksbehandlervalgVerdiBool(verdi: Boolean, displayText: String) = SaksbehandlervalgVerdi.Bool(
    defaultValue = verdi,
    displayText = displayText
)

fun saksbehandlervalgVerdiInteger(verdi: Int?, displayText: String) = SaksbehandlervalgVerdi.Integer(
    defaultValue = verdi,
    displayText = displayText
)

fun saksbehandlervalgVerdiText(verdi: String?, displayText: String) = SaksbehandlervalgVerdi.Text(
    defaultValue = verdi,
    displayText = displayText
)

inline fun <reified T> saksbehandlervalgVerdiEnum(verdi: T, displayText: String) where T : SaksbehandlerValgEnum, T : Enum<T> = SaksbehandlervalgVerdi.Enum(
    defaultValue = verdi,
    displayText = displayText,
    clazz = T::class.java
)