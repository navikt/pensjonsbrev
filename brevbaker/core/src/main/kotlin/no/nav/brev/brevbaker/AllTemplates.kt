package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

interface AllTemplates {
    fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>>
    fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out FagsystemBrevdata>>
    fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>>
}