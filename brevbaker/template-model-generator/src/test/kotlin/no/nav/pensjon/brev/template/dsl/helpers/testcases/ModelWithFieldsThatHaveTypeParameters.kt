package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.ModelWithFieldsThatHaveTypeParametersSelectors.ModelWithTypeParametersSelectors.NestedModelSelectors.SecondModelSelectors.lastName
import no.nav.pensjon.brev.template.dsl.helpers.testcases.ModelWithFieldsThatHaveTypeParametersSelectors.ModelWithTypeParametersSelectors.NestedModelSelectors.second
import no.nav.pensjon.brev.template.dsl.helpers.testcases.ModelWithFieldsThatHaveTypeParametersSelectors.ModelWithTypeParametersSelectors.otherList

/**
 * Verify that selectors are generated for type parameters of model fields.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object ModelWithFieldsThatHaveTypeParameters : HasModel<ModelWithFieldsThatHaveTypeParameters.ModelWithTypeParameters> {
    data class ModelWithTypeParameters(val name: String, val aList: List<String>, val otherList: List<NestedModel>) {
        data class NestedModel(val name: String, val second: SecondModel) {
            data class SecondModel(val lastName: String)
        }
    }

    fun doSomething() {
        val list: Expression<List<ModelWithTypeParameters.NestedModel>> = SimpleTemplateScope<ModelWithTypeParameters>().otherList
        val listItemLastName: Expression<String> =
            Expression.Literal(
                ModelWithTypeParameters.NestedModel(
                    "firstname",
                    ModelWithTypeParameters.NestedModel.SecondModel("lastname")
                )
            ).second.lastName
    }
}