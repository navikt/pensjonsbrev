package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

class ShowElseScope<Lang : LanguageSupport, LetterData : Any, Scope : TemplateManipulationScope<Lang, LetterData, Scope>>(
    private val scopeFactory: () -> Scope,
) {
    val scope: Scope = scopeFactory()

    infix fun orShow(body: Scope.() -> Unit) {
        scope.apply(body)
    }

    fun orShowIf(
        predicate: Expression<Boolean>,
        body: Scope.() -> Unit
    ): ShowElseScope<Lang, LetterData, Scope> =
        ShowElseScope(scopeFactory).also { showElse ->
            scope.children.add(Element.Conditional(predicate, scopeFactory().apply(body).children, showElse.scope.children))
        }

}
