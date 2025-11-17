package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

interface AllTemplates {
    fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>>
    fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>>
}