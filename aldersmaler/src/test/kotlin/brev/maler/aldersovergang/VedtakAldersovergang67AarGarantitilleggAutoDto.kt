package brev.maler.aldersovergang

import no.nav.pensjon.brev.model.alder.aldersovergang.AlderspensjonVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.BeregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakAldersovergang67AarGarantitilleggAutoDto(): VedtakAldersovergang67AarGarantitilleggAutoDto =
    VedtakAldersovergang67AarGarantitilleggAutoDto(
        virkFom = LocalDate.of(2024, 7, 1),
        beregnetPensjonPerManedVedVirk = BeregnetPensjonPerManedVedVirk(
            garantitillegg = Kroner(123)
        ),
        alderspensjonVedVirk = AlderspensjonVedVirk(
            totalPensjon = Kroner(5678),
            uttaksgrad = 100
        )
    )