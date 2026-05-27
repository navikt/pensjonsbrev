package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also

class SaksbehandlervalgWrapper<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(val displayText: String, val scope: TemplateRootScope<*, LetterData>) {
    fun bool(default: Boolean = false): Expression<Boolean> = generisk { SaksbehandlervalgVerdi.Bool(default) }
        .bool()
        .also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Bool(default) }

    fun int(default: Int? = null): Expression<Int> = generisk { SaksbehandlervalgVerdi.Integer(default) }
        .int()
        .also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Integer(default) }

    fun <T: SaksbehandlervalgVerdi> generisk(factory: () -> T): Expression<T> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selectorTake2(displayText, factory())))
}
fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg3(displayText: String) = SaksbehandlervalgWrapper(displayText, this)

fun <T : SaksbehandlervalgVerdi, D : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> selectorTake2(displayText: String, type: T) = SaksbehandlervalgSelector<D, T>(
    propertyName = displayText,
    propertyType = SaksbehandlervalgVerdi::class.qualifiedName!!,
    selector = { saksbehandlerValg.get(displayText) as T }
)

fun Expression<SaksbehandlervalgVerdi>.bool(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Boolean> {
    val selector1: TemplateModelSelector<SaksbehandlervalgVerdi, Boolean> = object : TemplateModelSelector<SaksbehandlervalgVerdi, Boolean> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "bool"
        override val propertyType = "kotlian.Boolean"
        override val selector: SaksbehandlervalgVerdi.() -> Boolean = { this.unwrap() as Boolean }
    }
    val operation: UnaryOperation.Select<SaksbehandlervalgVerdi, Boolean> = UnaryOperation.Select(selector1)
    return Expression.UnaryInvoke(this, operation)
}

fun Expression<SaksbehandlervalgVerdi>.int(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Int> {
    val selector1: TemplateModelSelector<SaksbehandlervalgVerdi, Int> = object : TemplateModelSelector<SaksbehandlervalgVerdi, Int> {
        override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
        override val propertyName = "int"
        override val propertyType = "Int"
        override val selector: SaksbehandlervalgVerdi.() -> Int = { this.unwrap() as Int }
    }
    val operation: UnaryOperation.Select<SaksbehandlervalgVerdi, Int> = UnaryOperation.Select(selector1)
    return Expression.UnaryInvoke(this, operation)
}


// .let {
//    Expression.UnaryInvoke(
//        null,
//        it
//    )
//}


//fun <D : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> Expression<D>.alleSaksbehandlervalgSelector() {
//    return UnaryOperation.Select(
//        SaksbehandlervalgSelector<SaksbehandlervalgIDSL>(
//            className = "no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL",
//            propertyName = displayText,
//            propertyType = T::class.qualifiedName!!,
//            selector = { SaksbehandlervalgIDSL::get }
//        )
//    )
//}


//val saksbehandlervalgSelector = SimpleSelector<no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDto, no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL>(
//    className = "no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDto",
//    propertyName = "saksbehandlerValg",
//    propertyType = "no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL",
//    selector = no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDto::saksbehandlerValg
//)



//val innerSelector = Expression.UnaryInvoke(
//    this,
//    UnaryOperation.Select(
//        SaksbehandlervalgSelector<SaksbehandlervalgVerdi.Bool>(
//            className = "no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL",
//            propertyName = displayText,
//            propertyType = "kotlin.Boolean",
//            selector = { SaksbehandlervalgIDSL::get }
//        )
//    )