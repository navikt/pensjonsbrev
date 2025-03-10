package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.brev.InternKonstruktoer

object ForEachViewTestSelectors {

    object ListArgumentSelectors {
        val listeSelector = object : TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument, kotlin.collections.List<kotlin.String>> {
           override val className: String = "no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument"
           override val propertyName: String = "liste"
           override val propertyType: String = "kotlin.collections.List<kotlin.String>"
           override val selector = no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument::liste
        }
        
        val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument>.liste: Expression<kotlin.collections.List<kotlin.String>>
           get() = Expression.UnaryInvoke(
               Expression.FromScope.Argument(),
               UnaryOperation.Select(listeSelector)
           )
        
        val Expression<no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument>.liste: Expression<kotlin.collections.List<kotlin.String>>
           get() = Expression.UnaryInvoke(
               this,
               UnaryOperation.Select(listeSelector)
           )
        
        val Expression<no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument?>.liste_safe: Expression<kotlin.collections.List<kotlin.String>?>
           get() = Expression.UnaryInvoke(
               this,
               UnaryOperation.SafeCall(listeSelector)
           )
        

    }
}
