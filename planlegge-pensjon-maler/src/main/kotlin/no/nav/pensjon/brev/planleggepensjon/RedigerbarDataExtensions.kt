package no.nav.pensjon.brev.planleggepensjon

import no.nav.pensjon.brev.template.DslExtensionForRedigerbareBrev
import no.nav.pensjon.brev.template.RedigerbarData
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

/**
 * Snarvei for å pakke en [StringExpression] (typisk et saksbehandlervalg) inn i [RedigerbarData], slik at
 * verdien ikke vises som en grå boks i Skribenten. Lar oss skrive `saksbehandlerValg.uttaksdato.redigerbar()`
 * i stedet for `redigerbarData(saksbehandlerValg.uttaksdato)`.
 */
context(ext: DslExtensionForRedigerbareBrev, scope: TemplateGlobalScope<*>)
fun StringExpression.redigerbar(): RedigerbarData =
    with(ext) { with(scope) { redigerbarData(this@redigerbar) } }
