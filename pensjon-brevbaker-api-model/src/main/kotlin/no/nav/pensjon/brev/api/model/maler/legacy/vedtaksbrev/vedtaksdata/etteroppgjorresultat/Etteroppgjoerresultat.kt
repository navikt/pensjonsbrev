package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.etteroppgjorresultat

import no.nav.pensjon.brevbaker.api.model.Kroner

data class Etteroppgjoerresultat(
    val avviksbelop: Kroner?,
    val avviksbeloptfb: Kroner?,
    val avviksbeloptsb: Kroner?,
    val avviksbeloput: Kroner?,
    val etteroppgjorresultattype: String?,
    val inntektut: Kroner?,
    val tidligerebeloptfb: Kroner?,
    val tidligerebeloptsb: Kroner?,
    val tidligerebeloput: Kroner?,
    val totalbeloptfb: Kroner?,
    val totalbeloptsb: Kroner?,
    val totalbeloput: Kroner?,
)
