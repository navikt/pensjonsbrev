package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.dsl.helpers.testcases.NestedModelsSelectors.FirstSelectors.second
import no.nav.pensjon.brev.template.dsl.helpers.testcases.NestedModelsSelectors.FourthSelectors.value
import no.nav.pensjon.brev.template.dsl.helpers.testcases.NestedModelsSelectors.SecondSelectors.third
import no.nav.pensjon.brev.template.dsl.helpers.testcases.NestedModelsSelectors.ThirdSelectors.fourth

/**
 * Verify that selectors are generated for nested models.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object NestedModels : HasModel<NestedModels.First> {
    data class First(val second: Second)
    data class Second(val third: Third)
    data class Third(val fourth: Fourth)
    data class Fourth(val value: Int)

    val expr: Expression<First> = Expression.Literal(First(Second(Third(Fourth(99)))))
    val value: Expression<Int> = expr.second.third.fourth.value
}