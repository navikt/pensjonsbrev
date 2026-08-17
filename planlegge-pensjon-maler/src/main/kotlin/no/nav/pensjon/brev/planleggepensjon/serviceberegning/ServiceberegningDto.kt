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
    val uttaksalder: Alder,
    val uttaksdato: String,
    val forventetFremtidigInntekt: Kroner,
    val afp: TidsbegrensetOffentligAfp,
    @DisplayText("Alternativ 1")
    val alt1: Boolean,
    @DisplayText("Alternativ 2")
    val alt2: Boolean,
) : SaksbehandlerValgBrevdata
