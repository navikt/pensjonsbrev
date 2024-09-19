package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor

import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdEtteroppgjorDetaljEPS(
    val inntektliste: InntektListe?,
    val fratrekkListe: FratrekkListe?,
    val suminntekterbt: Kroner?,
)
