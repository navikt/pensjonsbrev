package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsThroughAdditionalModelsInterfaceSelectors.AdditionalModelSelectors.age

/**
 * Verify that selectors also are generated for interface-models declared through the `additionalModels` annotation argument.
 *
 * If it does not compile then the test has failed.
 */
object SelectorsThroughAdditionalModelsInterface {
    data class AdditionalModel(val age: Int)

    @TemplateModelHelpers([AdditionalModel::class])
    interface MyInterface

    @OptIn(InternKonstruktoer::class)
    val x: Expression<Int> = Expression.Literal(AdditionalModel(35)).age
}