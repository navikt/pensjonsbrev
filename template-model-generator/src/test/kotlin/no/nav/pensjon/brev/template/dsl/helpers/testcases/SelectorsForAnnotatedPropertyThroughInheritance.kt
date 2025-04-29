package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertyThroughInheritanceSelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertyThroughInheritanceSelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertyThroughInheritanceSelectors.TheModelSelectors.navn_safe

/**
 * Verify that it is possible to generate selectors for annotated properties with nested inheritance.
 *
 * If it does not compile then the test has failed.
 */
object SelectorsForAnnotatedPropertyThroughInheritance {
    data class TheModel(val navn: String)
    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
    interface AThirdInterface<Unused2>: AnotherInterface<TheModel, Unused2>

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    val anAttachment: AThirdInterface<String> = object : AThirdInterface<String> {
        fun someusage() {
            val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
            val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
            val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
            val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
        }
    }
}
