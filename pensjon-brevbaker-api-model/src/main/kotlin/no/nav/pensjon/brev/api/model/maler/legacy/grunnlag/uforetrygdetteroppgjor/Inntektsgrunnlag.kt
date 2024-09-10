package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor

import no.nav.pensjon.brevbaker.api.model.Kroner

data class Inntektsgrunnlag(
    val grunnikkereduksjon: String?,
    val belop: Kroner?,
)
