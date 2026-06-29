package no.nav.pensjon.brev.template.expression

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl

class SelectorUsage {
    private val _propertyUsage: MutableSet<LetterMarkupWithDataUsage.Property> = mutableSetOf()
    val propertyUsage: Set<LetterMarkupWithDataUsage.Property> get() = _propertyUsage

    operator fun plusAssign(selector: TemplateModelSelector<*, *>) {
        _propertyUsage += LetterMarkupWithDataUsageImpl.PropertyImpl(typeName = selector.className, propertyName = selector.propertyName)
    }
}