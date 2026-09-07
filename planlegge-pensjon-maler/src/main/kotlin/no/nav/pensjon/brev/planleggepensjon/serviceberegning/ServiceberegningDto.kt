package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class ServiceberegningBrevDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: ServiceberegningDtoData,
) : BrevdataMedSaksbehandlerValg<ServiceberegningDtoData>

data class ServiceberegningDtoData(
    @DisplayText("Uttaksalder")
    val uttaksalder: Alder,
    @DisplayText("Uttaksdato")
    val uttaksdato: String,
    @DisplayText("Forventet fremtidig inntekt")
    val forventetFremtidigInntekt: Kroner,
    @DisplayText("AFP")
    val afp: TidsbegrensetOffentligAfp
) : FagsystemBrevdata
