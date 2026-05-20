package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.expr


internal sealed interface SaksbehandlervalgWrapper<T> {
    val displayText: String
    fun expr(scope: TemplateRootScope<*, *>): Expression<T>
    fun doExpr(scope: TemplateRootScope<*, *>) = expr(scope).also { scope.saksbehandlervalg[displayText] = it }

    class Bool(override val displayText: String, val default: Boolean) : SaksbehandlervalgWrapper<Boolean> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<Boolean> = false.expr() // Expression.FromScope.Saksbehandlervalg()
    }
    class Integer(override val displayText: String, val default: Int?) : SaksbehandlervalgWrapper<Int?> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<Int?> = Expression.FromScope.Saksbehandlervalg()
    }
    class Enum<T : SaksbehandlerValgEnum>(override val displayText: String, default: T?) : SaksbehandlervalgWrapper<T> {
        override fun expr(scope: TemplateRootScope<*, *>): Expression<T> = Expression.FromScope.Saksbehandlervalg()
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