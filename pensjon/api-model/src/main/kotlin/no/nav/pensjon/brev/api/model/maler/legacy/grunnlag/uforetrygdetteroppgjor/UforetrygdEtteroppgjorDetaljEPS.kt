package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

data class UforetrygdEtteroppgjorDetaljEPS(
    val inntektliste: InntektListe?,
    val fratrekkListe: FratrekkListe?,
    val suminntekterbt: Kroner?,
    val sumfratrekkbt: Kroner?,
)
