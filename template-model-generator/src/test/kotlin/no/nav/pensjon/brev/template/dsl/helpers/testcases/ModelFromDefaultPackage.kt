package no.nav.pensjon.brev.template.dsl.helpers.testcases

import ModelFromDefaultPackage_TheModel
import ModelFromDefaultPackage_TheModelSelectors
import ModelFromDefaultPackage_TheModelSelectors.name
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

/**
 * Verify that selectors are generated when model resides in default package.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object ModelFromDefaultPackage : HasModel<ModelFromDefaultPackage_TheModel> {
    fun someusage() {
        SimpleTemplateScope<ModelFromDefaultPackage_TheModel>().name
        Expression.Literal(ModelFromDefaultPackage_TheModel("jadda")).name
        ModelFromDefaultPackage_TheModelSelectors.nameSelector
    }
}