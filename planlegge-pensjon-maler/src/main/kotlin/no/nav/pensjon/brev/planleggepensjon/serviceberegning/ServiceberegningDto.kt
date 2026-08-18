package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class ServiceberegningBrevDto(
    override val saksbehandlerValg: ServiceberegningDto,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
) : RedigerbarBrevdata<ServiceberegningDto, EmptyFagsystemdata>

data class ServiceberegningDto(
    @DisplayText("Uttaksalder")
    val uttaksalder: Alder,
    @DisplayText("Uttaksdato")
    val uttaksdato: String,
    @DisplayText("Forventet fremtidig inntekt")
    val forventetFremtidigInntekt: Kroner,
    @DisplayText("AFP")
    val afp: TidsbegrensetOffentligAfp,
    @DisplayText("Ingen ytelser")
    val alt1: Boolean = false,
    @DisplayText("Vedtak om alderspensjon")
    val alt2: Boolean = false,
    @DisplayText("Vedtak om uføretrygd")
    val alt3: Boolean = false,
    @DisplayText("AAP utbetales")
    val alt4: Boolean = false,
    @DisplayText("Mottar / søker om sykepenger")
    val alt5: Boolean = false,
) : SaksbehandlerValgBrevdata
