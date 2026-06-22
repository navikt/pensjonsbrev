package no.nav.pensjon.brev.template.validation

import no.nav.pensjon.brev.template.OutlineElement

internal object EmptyValidator : BrevTemplateValidator {
    override fun validate(e: OutlineElement<*>) = Unit
    override fun subScope(): BrevTemplateValidator = this
}