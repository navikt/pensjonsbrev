package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.InterfaceAsModelSelectors.TheModelSelectors.fornavn
import no.nav.pensjon.brev.template.dsl.helpers.testcases.InterfaceAsModelSelectors.TheModelSelectors.fornavn_safe

/**
 * Verify that it is possible to generate selectors when the model is an interface.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object InterfaceAsModel : HasModel<InterfaceAsModel.TheModel> {

    interface TheModel {
        val fornavn: String
    }

    fun someusage() {
        val scopeExtensionProperty: Expression<String> = SimpleTemplateScope<TheModel>().fornavn
        val expressionExtensionProperty: Expression<String> = Expression.Literal(object : TheModel {
            override val fornavn: String = "Jadda"
        }).fornavn
        val nullableExpressionExtensionProperty: Expression<String?> = Expression.Literal<TheModel?>(null).fornavn_safe
        val actualSelector: TemplateModelSelector<TheModel, String> = InterfaceAsModelSelectors.TheModelSelectors.fornavnSelector
    }
}