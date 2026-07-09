package no.nav.pensjon.brev.template.expression

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl

class SelectorUsage {
    val propertyUsage: Set<LetterMarkupWithDataUsage.Property> field: MutableSet<LetterMarkupWithDataUsage.Property> = mutableSetOf()

    operator fun plusAssign(selector: TemplateModelSelector<*, *>) {
        propertyUsage += LetterMarkupWithDataUsageImpl.PropertyImpl(typeName = selector.className, propertyName = selector.propertyName)
    }
}