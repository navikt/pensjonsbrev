package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.Grunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.Vedtaksdata


data class Vedtaksbrev(
    val vedtaksdata: Vedtaksdata?,
    val grunnlag: Grunnlag,
)
