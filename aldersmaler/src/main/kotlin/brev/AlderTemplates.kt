package no.nav.pensjon.brev

import brev.auto.InfoFyller67AarSaerskiltSats
import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.AutoMal
import no.nav.brev.brevbaker.RedigerbarMal
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

object AlderTemplates : AllTemplates {
    override fun hentAutobrevmaler() = setOf(
        AutoMal(InfoFyller67AarSaerskiltSats)
    )

    override fun hentRedigerbareMaler(): Set<RedigerbarMal<RedigerbarBrevdata<*, *>>> = setOf(

    )
}