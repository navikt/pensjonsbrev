package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object IncludeAttachmentTestSelectors {

    object NullDataSelectors {
        val testSelector = SimpleSelector(IncludeAttachmentTest.NullData::test)

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
