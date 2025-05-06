package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughSecondaryInheritanceSelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughSecondaryInheritanceSelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.HasModelThroughSecondaryInheritanceSelectors.TheModelSelectors.navn_safe

interface AnotherInterface<Model2: Any> : HasModel<Model2>

/**
 * Verify that it is possible to generate selectors through secondary inheritance of HasModel.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object HasModelThroughSecondaryInheritance : AnotherInterface<HasModelThroughSecondaryInheritance.TheModel> {
    data class TheModel(val navn: String)

    fun someusage() {
        val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
        val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
        val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
        val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
    }
}