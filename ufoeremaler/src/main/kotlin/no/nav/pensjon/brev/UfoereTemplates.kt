package no.nav.pensjon.brev

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.AutoMal
import no.nav.brev.brevbaker.RedigerbarMal
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

object UfoereTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutoMal<*>> = setOf(

    )

    override fun hentRedigerbareMaler(): Set<RedigerbarMal<RedigerbarBrevdata<*, *>>> = setOf(

    )
}