package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object IncludeAttachmentTestSelectors {

    object NullDataSelectors {
        val testSelector = object :
            TemplateModelSelector<IncludeAttachmentTest.NullData, IncludeAttachmentTest.StringWrapper?> {
            override val className: String = "no.nav.pensjon.brev.template.render.IncludeAttachmentTest.NullData"
            override val propertyName: String = "test"
            override val propertyType: String = IncludeAttachmentTest.StringWrapper::class.java.canonicalName + "?"
            override val selector = IncludeAttachmentTest.NullData::test
        }

        val TemplateGlobalScope<IncludeAttachmentTest.NullData>.test: Expression<IncludeAttachmentTest.StringWrapper?>
            get() = Expression.UnaryInvoke(
                Expression.FromScope.Argument(),
                UnaryOperation.Select(testSelector)
            )

        val Expression<IncludeAttachmentTest.NullData>.test: Expression<IncludeAttachmentTest.StringWrapper?>
            get() = Expression.UnaryInvoke(
                this,
                UnaryOperation.Select(testSelector)
            )
    }
}
