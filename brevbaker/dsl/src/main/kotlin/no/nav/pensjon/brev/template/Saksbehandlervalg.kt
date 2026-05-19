package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL


sealed interface SaksbehandlervalgWrapper<T> {
    val displayText: String
    fun expr(): Expression<T>

    class Bool(override val displayText: String, val default: Boolean) : SaksbehandlervalgWrapper<Boolean> {
        override fun expr(): Expression<Boolean> = Expression.Literal(default)
    }
    class Integer(override val displayText: String, val default: Int?) : SaksbehandlervalgWrapper<Int?> {
        override fun expr(): Expression<Int?> = Expression.Literal(default)
    }
    class Enum<T : SaksbehandlerValgEnum>(override val displayText: String) : SaksbehandlervalgWrapper<T> {
        override fun expr(): Expression<T> = TODO()
    }
}

interface SaksbehandlerValgEnum {
    val displayText: String
}

class SBWrapper(val displayText: String) {
    fun bool(default: Boolean = false) = SaksbehandlervalgWrapper.Bool(displayText, default).expr()
    fun int(default: Int? = null) = SaksbehandlervalgWrapper.Integer(displayText, default)
    fun <T : SaksbehandlerValgEnum> enum() = SaksbehandlervalgWrapper.Enum<T>(displayText)
}

fun <LetterData: RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> RedigerbarTemplate<LetterData>.saksbehandlervalg(displayText: String) = SBWrapper(displayText)

