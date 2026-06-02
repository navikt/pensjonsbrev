package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also

class SaksbehandlervalgWrapper<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(val id: String, val displayText: String, val scope: TemplateRootScope<*, LetterData>) {
    fun bool(default: Boolean = false): Expression<Boolean> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .bool()
        .also { scope.saksbehandlervalg[id] = SaksbehandlervalgVerdi.Bool(default, displayText) }

    fun int(default: Int? = null): Expression<Int?> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .int()
        .also { scope.saksbehandlervalg[id] = SaksbehandlervalgVerdi.Integer(default, displayText) }

    fun text(default: String?): Expression<String?> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .text()
        .also { scope.saksbehandlervalg[id] = SaksbehandlervalgVerdi.Text(default, displayText) }

    inline fun <reified T : SaksbehandlerValgEnum> enum(default: T? = null): Expression<T?> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .enum<T?>()
        .also { scope.saksbehandlervalg[id] = SaksbehandlervalgVerdi.Enum(default, displayText, T::class.java) }
}
fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(id: String, displayText: String) = SaksbehandlervalgWrapper(id, displayText, this)

fun <T : SaksbehandlervalgVerdi, D : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> selector(id: String) = SaksbehandlervalgSelector<D, T>(
    propertyName = id,
    propertyType = SaksbehandlervalgVerdi::class.qualifiedName!!,
    selector = { saksbehandlerValg.get(id) }
)

fun Expression<SaksbehandlervalgVerdi>.bool(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Boolean> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, Boolean> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "bool"
        override val propertyType = "kotlin.Boolean"
        override val selector: SaksbehandlervalgVerdi.() -> Boolean = { this.unwrap() as Boolean }
    }))

fun Expression<SaksbehandlervalgVerdi>.int(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Int?> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, Int?> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "int"
        override val propertyType = "Int"
        override val selector: SaksbehandlervalgVerdi.() -> Int? = { this.unwrap() as Int? }
    }))

fun Expression<SaksbehandlervalgVerdi>.text(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, String?> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, String?> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "string"
        override val propertyType = "kotlin.String"
        override val selector: SaksbehandlervalgVerdi.() -> String? = { this.unwrap() as String? }
    }))

inline fun <reified T : SaksbehandlerValgEnum?> Expression<SaksbehandlervalgVerdi>.enum(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, T?> =
    Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, T?> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = T::class.simpleName!!
        override val propertyType = T::class.qualifiedName!!
        override val selector: SaksbehandlervalgVerdi.() -> T? = { this.unwrap() as T? }
    }))