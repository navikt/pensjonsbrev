package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also


internal sealed interface SaksbehandlervalgWrapper<T> {
    val displayText: String
    val default: T?
    fun expr(scope: TemplateRootScope<*, *>): Expression<T>
    fun doExpr(scope: TemplateRootScope<*, *>) = expr(scope).also { scope.saksbehandlervalg[displayText] = default }

    class Bool(override val displayText: String, override val default: Boolean) : SaksbehandlervalgWrapper<Boolean> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<Boolean> = Expression.FromScope.Saksbehandlervalg(displayText, default)
    }
    class Integer(override val displayText: String, override val default: Int?) : SaksbehandlervalgWrapper<Int?> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<Int?> = Expression.FromScope.Saksbehandlervalg(displayText, default)
    }
    class Enum<T : SaksbehandlerValgEnum>(override val displayText: String, override val default: T?) : SaksbehandlervalgWrapper<T?> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<T?> = Expression.FromScope.Saksbehandlervalg(displayText, default)
    }
}

interface SaksbehandlerValgEnum {
    val displayText: String
}

class SBWrapper(val displayText: String, val scope: TemplateRootScope<*, *>) {
    fun bool(default: Boolean = false) = SaksbehandlervalgWrapper.Bool(displayText, default).doExpr(scope)
    fun int(default: Int? = null) = SaksbehandlervalgWrapper.Integer(displayText, default).doExpr(scope)
    fun <T : SaksbehandlerValgEnum> enum(default: T?) = SaksbehandlervalgWrapper.Enum<T>(displayText, default).doExpr(scope)
}

fun <LetterData: RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(displayText: String) = SBWrapper(displayText, this)