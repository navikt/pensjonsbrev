package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

class ShowElseScope<Lang : LanguageSupport, LetterData : Any, C : Element<Lang>, Scope : ControlStructureScope<Lang, LetterData, C, Scope>>(
    private val scopeFactory: () -> Scope,
) {
    val scope: Scope = scopeFactory()

    infix fun orShow(body: Scope.() -> Unit) {
        scope.apply(body)
    }

    fun orShowIf(
        predicate: Expression<Boolean>,
        body: Scope.() -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(scopeFactory).also { showElse ->
            scope.addControlStructure(ContentOrControlStructure.Conditional(predicate, scopeFactory().apply(body).elements, showElse.scope.elements))
        }

}
