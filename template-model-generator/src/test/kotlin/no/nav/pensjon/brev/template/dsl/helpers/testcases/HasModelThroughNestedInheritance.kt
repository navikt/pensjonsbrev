package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughNestedInheritanceSelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughNestedInheritanceSelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughNestedInheritanceSelectors.TheModelSelectors.navn_safe

/**
 * Verify that it is possible to generate selectors through nested inheritance of HasModel.
 * The test also verifies that some mixing of type parameters will work.
 *
 * If it does not compile then the test has failed.
 */
object HasModelThroughNestedInheritance {

    data class TheModel(val navn: String)
    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
    interface AThirdInterface<Unused2, Model3: Any>: AnotherInterface<Model3, String>

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    object MyClass : AThirdInterface<Int, TheModel> {
        fun someusage() {
            val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
            val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
            val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
            val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
        }
    }
}