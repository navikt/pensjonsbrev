package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateContainerScope

interface Phrase<Data: Any> {
    val elements: List<Element<BaseLanguages>>
}

// Though code analysis states otherwise, receiver parameter is actually used to bind the parametric type Data.
fun <Data : Any> Phrase<Data>.phrase(
    init: TemplateContainerScope<BaseLanguages, Data>.() -> Unit
): List<Element<BaseLanguages>> =
    TemplateContainerScope<BaseLanguages, Data>().apply(init).children