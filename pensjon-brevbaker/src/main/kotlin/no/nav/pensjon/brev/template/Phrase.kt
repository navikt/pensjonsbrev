package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateContainerScope

interface Phrase<out Lang : LanguageSupport, Data: Any> {
    val elements: List<Element<Lang>>
}

// Though code analysis states otherwise, receiver parameter is actually used to bind the parametric type Data.
fun <Lang : LanguageSupport, Data : Any> Phrase<Lang, Data>.phrase(
    init: TemplateContainerScope<Lang, Data>.() -> Unit
): List<Element<Lang>> =
    TemplateContainerScope<Lang, Data>().apply(init).children