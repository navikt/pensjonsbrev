package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object IncludeAttachmentTestSelectors {

    object NullDataSelectors {
        val testSelector = object :
            TemplateModelSelector<IncludeAttachmentTest.NullData, String?> {
            override val className: String = "no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData"
            override val propertyName: String = "test"
            override val propertyType: String = "kotlin.String?"
            override val selector = IncludeAttachmentTest.NullData::test
        }

        val TemplateGlobalScope<IncludeAttachmentTest.NullData>.test: Expression<String?>
            get() = Expression.UnaryInvoke(
                Expression.FromScope.Argument(),
                UnaryOperation.Select(testSelector)
            )

        val Expression<IncludeAttachmentTest.NullData>.test: Expression<String?>
            get() = Expression.UnaryInvoke(
                this,
                UnaryOperation.Select(testSelector)
            )
    }
}
