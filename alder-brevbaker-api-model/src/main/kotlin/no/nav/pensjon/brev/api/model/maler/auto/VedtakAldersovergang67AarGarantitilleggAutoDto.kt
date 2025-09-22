package no.nav.pensjon.brev.api.model.maler.auto

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class VedtakAldersovergang67AarGarantitilleggAutoDto(
    val virkFom: LocalDate,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val alderspensjonVedVirk: AlderspensjonVedVirk
) : BrevbakerBrevdata

data class BeregnetPensjonPerManedVedVirk(
    val garantitillegg: Kroner,
)

data class AlderspensjonVedVirk(
    val totalPensjon: Kroner,
    val uttaksgrad: Int,
)
