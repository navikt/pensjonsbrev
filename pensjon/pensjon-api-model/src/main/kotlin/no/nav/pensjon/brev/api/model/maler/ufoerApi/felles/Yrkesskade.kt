package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import java.time.LocalDate

data class Yrkesskade(
    val skadetidspunkt: LocalDate,
    val yrkesskadegrad: Int,
    val inntektVedSkadetidspunkt: BrevbakerType.Kroner,
    val inntektVedSkadetidspunktBegrunnelse: VilkarsvurderingFullstendig.InntektVedSkadetidspunktBegrunnelse?,
)