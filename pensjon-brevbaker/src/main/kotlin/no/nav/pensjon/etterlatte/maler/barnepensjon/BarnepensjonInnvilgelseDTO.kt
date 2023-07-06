package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Avkortingsinfo
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo

data class BarnepensjonInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo? = null,
    val avdoed: Avdoed,
)
