package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.DataClassAsModelSelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.DataClassAsModelSelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.DataClassAsModelSelectors.TheModelSelectors.navn_safe

/**
 * Verify that it is possible to generate selectors through direct inheritance of HasModel.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object DataClassAsModel : HasModel<DataClassAsModel.TheModel> {
    data class TheModel(val navn: String)

    fun someusage() {
        val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
        val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
        val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
        val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
    }
}