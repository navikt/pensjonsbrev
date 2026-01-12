package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.ModelWithMultipleFieldsWithSameTypeSelectors.NestedModelWithRepetitionSelectors.SubModelSelectors.lastName
import no.nav.pensjon.brev.template.dsl.helpers.testcases.ModelWithMultipleFieldsWithSameTypeSelectors.NestedModelWithRepetitionSelectors.second

/**
 * Verify that it is possible to generate selectors for models with multiple fields of same type.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
object ModelWithMultipleFieldsWithSameType : HasModel<ModelWithMultipleFieldsWithSameType.NestedModelWithRepetition> {
    data class NestedModelWithRepetition(val name: String, val first: SubModel, val second: SubModel) {
        data class SubModel(val lastName: String)
    }
    fun doSomething() {
        SimpleTemplateScope<NestedModelWithRepetition>().second.lastName
    }
}
