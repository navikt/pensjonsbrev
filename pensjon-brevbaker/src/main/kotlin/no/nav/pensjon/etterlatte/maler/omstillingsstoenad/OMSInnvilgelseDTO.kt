package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Avkortingsinfo
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo

data class OMSInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo,
    val avdoed: Avdoed
)