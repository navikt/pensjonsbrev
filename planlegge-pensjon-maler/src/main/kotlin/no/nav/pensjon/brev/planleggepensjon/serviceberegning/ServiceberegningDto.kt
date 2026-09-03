package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class ServiceberegningBrevDto(
    override val saksbehandlerValg: ServiceberegningDto,
    override val pesysData: ServiceberegningDtoData,
) : RedigerbarBrevdata<ServiceberegningDto, ServiceberegningDtoData>

data class ServiceberegningDto(
    @DisplayText("Ingen ytelser")
    val alt1: Boolean = true,
    @DisplayText("Vedtak om alderspensjon")
    val alt2: Boolean = false,
    @DisplayText("Vedtak om uføretrygd")
    val alt3: Boolean = false,
    @DisplayText("AAP utbetales")
    val alt4: Boolean = false,
    @DisplayText("Mottar / søker om sykepenger")
    val alt5: Boolean = false,
) : SaksbehandlerValgBrevdata

data class ServiceberegningDtoData(
    val uttaksalder: Alder,
    val uttaksdato: String,
    val forventetFremtidigInntekt: Kroner,
    val afp: TidsbegrensetOffentligAfp
) : FagsystemBrevdata
