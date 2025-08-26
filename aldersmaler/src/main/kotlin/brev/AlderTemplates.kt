package no.nav.pensjon.brev

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object AlderTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(

    )

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(

    )
}