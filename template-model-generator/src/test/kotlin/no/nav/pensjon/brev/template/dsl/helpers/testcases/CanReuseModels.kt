package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.CanReuseModelsDto1Selectors.ChildModelSelectors.childName
import no.nav.pensjon.brev.template.dsl.helpers.testcases.CanReuseModelsDto2Selectors.ChildModelSelectors.uncle
import no.nav.pensjon.brev.template.dsl.helpers.testcases.CanReuseModelsDto2Selectors.child

data class CanReuseModelsDto1(val name: String) {
    data class ChildModel(val childName: String) {}
}
data class CanReuseModelsDto2(val child: ChildModel) {
    data class ChildModel(val uncle: CanReuseModelsDto1.ChildModel)
}

/**
 * Verify that models can be reused in multiple annotated targets without causing selectors to be generated multiple times.
 * The models of this test are declared in file root scope such that they aren't namespaced in CanReuseModelsSelectors-object.
 *
 * If it does not compile then the test has failed.
 */
object CanReuseModels {

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    object MyClass : HasModel<CanReuseModelsDto2> {
        val data: Expression<CanReuseModelsDto2> = Expression.Literal(CanReuseModelsDto2(CanReuseModelsDto2.ChildModel(CanReuseModelsDto1.ChildModel("Scrooge"))))
        val child: Expression<CanReuseModelsDto2.ChildModel> = data.child
        val uncleName: Expression<String> = child.uncle.childName
    }

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    object ReUse : HasModel<CanReuseModelsDto1.ChildModel> {
        val uncleName: Expression<String> = Expression.Literal(CanReuseModelsDto1.ChildModel("Scrooge")).childName
    }
}
