package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertySelectors.TheModelSelectors
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertySelectors.TheModelSelectors.navn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.SelectorsForAnnotatedPropertySelectors.TheModelSelectors.navn_safe

/**
 * Verify that it is possible to generate selectors for annotated properties.
 *
 * If it does not compile then the test has failed.
 */
object SelectorsForAnnotatedProperty {
    data class TheModel(val navn: String)
    interface SimpleAttachment<T : Any> : HasModel<T>

    @TemplateModelHelpers
    @OptIn(InternKonstruktoer::class)
    val anAttachment: SimpleAttachment<TheModel> = object : SimpleAttachment<TheModel> {
        fun someusage() {
            val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().navn
            val expressionExtensionProperty: Expression<String> = Expression.Literal(TheModel("jadda")).navn
            val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).navn_safe
            val actualSelector: TemplateModelSelector<TheModel, String> = TheModelSelectors.navnSelector
        }
    }
}