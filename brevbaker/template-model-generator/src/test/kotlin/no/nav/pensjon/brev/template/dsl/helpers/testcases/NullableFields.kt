package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.NullableFieldsSelectors.TheModelSelectors.name

/**
 * Verify that selectors are generated for nullable fields of models.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object NullableFields : HasModel<NullableFields.TheModel> {
    data class TheModel(val name: String?)

    fun someUsage() {
        val name: Expression<String?> = SimpleTemplateScope<TheModel>().name
    }
}