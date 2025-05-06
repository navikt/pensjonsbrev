package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.testcases.AscendantDeclaresModelSelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.AscendantDeclaresModelSelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.AscendantDeclaresModelSelectors.TheModelSelectors.navn_safe

/**
 * Verify that it is possible to generate selectors through nested inheritance of HasModel when the model is declared by super.
 * The test also verifies that some mixing of type parameters will work.
 *
 * If it does not compile then the test has failed.
 */
object AscendantDeclaresModel {
    data class TheModel(val navn: String)
    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
    interface AThirdInterface<Unused2>: AnotherInterface<TheModel, Unused2>

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    object MyClass : AThirdInterface<Int> {
        fun someusage() {
            val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
            val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
            val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
            val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
        }
    }
}
