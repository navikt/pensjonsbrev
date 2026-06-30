package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.dsl.helpers.testcases.selectors.selectorsThroughAdditionalModels.additionalModel.*
import no.nav.pensjon.brev.template.dsl.helpers.testcases.selectors.selectorsThroughAdditionalModels.theModel.*

/**
 * Verify that selectors also are generated for models declared through the `additionalModels` annotation argument.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers([SelectorsThroughAdditionalModels.AdditionalModel::class])
@OptIn(InternKonstruktoer::class)
object SelectorsThroughAdditionalModels : HasModel<List<SelectorsThroughAdditionalModels.TheModel>> {
    data class AdditionalModel(val age: Int)
    data class TheModel(val navn: String)

    val x: Expression<Int> = Expression.Literal(AdditionalModel(35)).age
    val y: Expression<String> = Expression.Literal(TheModel("Jadda")).navn
}