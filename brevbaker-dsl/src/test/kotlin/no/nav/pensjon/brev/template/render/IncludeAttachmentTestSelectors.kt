package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.brev.InternKonstruktoer

object IncludeAttachmentTestSelectors {

    object NullDataSelectors {
        val testSelector = object : TemplateModelSelector<no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData, kotlin.String?> {
           override val className: String = "no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData"
           override val propertyName: String = "test"
           override val propertyType: String = "kotlin.String?"
           override val selector = no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData::test
        }
        
        val TemplateGlobalScope<no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData>.test: Expression<kotlin.String?>
           get() = Expression.UnaryInvoke(
               Expression.FromScope.Argument(),
               UnaryOperation.Select(testSelector)
           )
        
        val Expression<no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData>.test: Expression<kotlin.String?>
           get() = Expression.UnaryInvoke(
               this,
               UnaryOperation.Select(testSelector)
           )
        
        val Expression<no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData?>.test_safe: Expression<kotlin.String?>
           get() = Expression.UnaryInvoke(
               this,
               UnaryOperation.SafeCall(testSelector)
           )
        

    }
}
