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
    val forventetFremtidigInntekt: Kroner?,
    @DisplayText("AFP i offentlig sektor")
    val afp: TidsbegrensetOffentligAfp,
) : SaksbehandlerValgBrevdata
