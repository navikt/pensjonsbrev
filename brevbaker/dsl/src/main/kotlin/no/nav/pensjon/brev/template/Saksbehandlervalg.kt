package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also

class SaksbehandlervalgWrapper<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(val displayText: String, val scope: TemplateRootScope<*, LetterData>) {
    private fun <T : SaksbehandlervalgVerdi> TemplateRootScope<*, LetterData>.saksbehandlervalg(verdi: T) { saksbehandlervalg[displayText] = verdi }

    fun bool(default: Boolean = false): Expression<Boolean> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(displayText)))
        .bool()
        .also { scope.saksbehandlervalg(SaksbehandlervalgVerdi.Bool(default)) }

    fun int(default: Int? = null): Expression<Int> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(displayText)))
        .int()
        .also { scope.saksbehandlervalg(SaksbehandlervalgVerdi.Integer(default)) }
}
fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(displayText: String) = SaksbehandlervalgWrapper(displayText, this)

private fun <T : SaksbehandlervalgVerdi, D : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> selector(displayText: String) = SaksbehandlervalgSelector<D, T>(
    propertyName = displayText,
    propertyType = SaksbehandlervalgVerdi::class.qualifiedName!!,
    selector = { saksbehandlerValg.get(displayText) }
)

fun Expression<SaksbehandlervalgVerdi>.bool(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Boolean> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, Boolean> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "bool"
        override val propertyType = "kotlin.Boolean"
        override val selector: SaksbehandlervalgVerdi.() -> Boolean = { this.unwrap() as Boolean }
    }))

fun Expression<SaksbehandlervalgVerdi>.int(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Int> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, Int> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "int"
        override val propertyType = "Int"
        override val selector: SaksbehandlervalgVerdi.() -> Int = { this.unwrap() as Int }
    }))