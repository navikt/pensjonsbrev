package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor

import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdEtteroppgjorDetaljBruker(
    val fratrekkliste: FratrekkListe?,
    val inntektliste: InntektListe?,
    val sumfratrekkut: Kroner?,
    val sumfratrekkbt: Kroner?,
    val suminntekterbt: Kroner?,
    val suminntekterut: Kroner?,
)
