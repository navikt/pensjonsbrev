package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.selectorTake2
import kotlin.also

class SBWrapper(val displayText: String, val scope: TemplateRootScope<*, *>) {
    fun bool(default: Boolean = false) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Bool(default) }
    fun int(default: Int? = null) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Integer(default) }
    fun <T : SaksbehandlerValgEnum> enum(default: T?) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Enum(default) }
}

class SaksbehandlervalgWrapper<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(val displayText: String, val scope: TemplateRootScope<*, LetterData>) {
    fun bool(default: Boolean = false): Expression<Boolean> {
        val selector: SaksbehandlervalgSelector<LetterData, SaksbehandlervalgVerdi.Bool> = selectorTake2<SaksbehandlervalgVerdi.Bool, LetterData>(displayText, SaksbehandlervalgVerdi.Bool(default))
        val selected: UnaryOperation.Select<LetterData, SaksbehandlervalgVerdi.Bool> = UnaryOperation.Select(selector)
        val unaryInvoke: Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi.Bool> = Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi.Bool>(
            scope.argument,
            selected
        )
        return unaryInvoke.bool().also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Bool(default) }
    }
    fun int(default: Int? = null): Expression<Int> {
        val selector: SaksbehandlervalgSelector<LetterData, SaksbehandlervalgVerdi.Integer> = selectorTake2<SaksbehandlervalgVerdi.Integer, LetterData>(displayText, SaksbehandlervalgVerdi.Integer(default))
        val selected: UnaryOperation.Select<LetterData, SaksbehandlervalgVerdi.Integer> = UnaryOperation.Select(selector)
        val unaryInvoke: Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi.Integer> = Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi.Integer>(
            scope.argument,
            selected
        )
        return unaryInvoke.int().also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Integer(default) }
    }
}
fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg3(displayText: String) = SaksbehandlervalgWrapper(displayText, this)

fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(displayText: String) = SBWrapper(displayText, this)


/*fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg2(displayText: String, default: Boolean = false): Expression<Boolean> {
    val selector: SaksbehandlervalgSelector<LetterData, SaksbehandlervalgVerdi> = this.argument.selectorTake2(displayText, SaksbehandlervalgVerdi.Bool(default))
    val selected: UnaryOperation.Select<LetterData, SaksbehandlervalgVerdi> = UnaryOperation.Select(selector)
    val unaryInvoke: Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi> = Expression.UnaryInvoke<LetterData, SaksbehandlervalgVerdi>(
        this.argument,
        selected
    )
    return unaryInvoke.bool().also { this.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Bool(default) }
}*/

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