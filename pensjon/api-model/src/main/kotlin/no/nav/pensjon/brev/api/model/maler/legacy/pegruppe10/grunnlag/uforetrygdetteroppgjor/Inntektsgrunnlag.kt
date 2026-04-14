package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.uforetrygdetteroppgjor

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class Inntektsgrunnlag(
    val grunnikkereduksjon: String?,
    val belop: Kroner?,
    val inntekttype: String?,
    val registerkilde: String?,
)
