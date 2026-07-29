package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

data class RedigerbareVedleggTitler(val vedlegg: List<Vedlegg>) {
    data class Vedlegg(val id: VedleggId, val tittel: String)
}
