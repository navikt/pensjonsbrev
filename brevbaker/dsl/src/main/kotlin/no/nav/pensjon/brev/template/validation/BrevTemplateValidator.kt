package no.nav.pensjon.brev.template.validation

import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.RedigerbarTemplate

internal fun BrevTemplate<*, *>.validator(): BrevTemplateValidator =
    when (this) {
        is AutobrevTemplate<*> -> EmptyValidator
        is RedigerbarTemplate<*> -> RedigerbarTemplateValidator()
    }

internal interface BrevTemplateValidator {
    fun validate(e: OutlineElement<*>)
    fun subScope(): BrevTemplateValidator
}