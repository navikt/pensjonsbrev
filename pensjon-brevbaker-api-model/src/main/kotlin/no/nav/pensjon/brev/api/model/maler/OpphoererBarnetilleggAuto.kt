package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")


data class OpphoererBarnetilleggAutoInnledningDto(
    val oensketVirkningsDato: LocalDate,
// Vedtaksdata.Kravhode.onsketVirkningsDato
    val fdatoPaaBarnetilleggOpphoert: Number,
// UT.FodselsdatoBarn
    val totalNettoMaanedligUfoertrygdUtbetalt: Kroner,
// Vedtaksdata.BeregningsData.BeregningUfore.TotalNetto
)