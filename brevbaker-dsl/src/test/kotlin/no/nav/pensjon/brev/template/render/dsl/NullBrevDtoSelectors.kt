package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.brev.InternKonstruktoer

object NullBrevDtoSelectors {
    val test1Selector = object : TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.NullBrevDto, kotlin.String?> {
       override val className: String = "no.nav.pensjon.brev.template.render.dsl.NullBrevDto"
       override val propertyName: String = "test1"
       override val propertyType: String = "kotlin.String?"
       override val selector = no.nav.pensjon.brev.template.render.dsl.NullBrevDto::test1
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.NullBrevDto>.test1: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(test1Selector)
       )
    
    val Expression<no.nav.pensjon.brev.template.render.dsl.NullBrevDto>.test1: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(test1Selector)
       )
    
    val Expression<no.nav.pensjon.brev.template.render.dsl.NullBrevDto?>.test1_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(test1Selector)
       )
    
    val test2Selector = object : TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.NullBrevDto, kotlin.String?> {
       override val className: String = "no.nav.pensjon.brev.template.render.dsl.NullBrevDto"
       override val propertyName: String = "test2"
       override val propertyType: String = "kotlin.String?"
       override val selector = no.nav.pensjon.brev.template.render.dsl.NullBrevDto::test2
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.NullBrevDto>.test2: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(test2Selector)
       )
    
    val Expression<no.nav.pensjon.brev.template.render.dsl.NullBrevDto>.test2: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(test2Selector)
       )
    
    val Expression<no.nav.pensjon.brev.template.render.dsl.NullBrevDto?>.test2_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(test2Selector)
       )
    

}
