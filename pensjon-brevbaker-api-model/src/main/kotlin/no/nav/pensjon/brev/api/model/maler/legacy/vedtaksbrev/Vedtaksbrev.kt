package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.Grunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.Vedtaksdata


data class Vedtaksbrev(
    val vedtaksdata: Vedtaksdata?,
    val grunnlag: Grunnlag,
)
