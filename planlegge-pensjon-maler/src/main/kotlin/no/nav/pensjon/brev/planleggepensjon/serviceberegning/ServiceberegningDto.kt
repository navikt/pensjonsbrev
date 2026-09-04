package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class ServiceberegningBrevDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: ServiceberegningDtoData,
) : RedigerbarBrevdata<SaksbehandlervalgIDSL, ServiceberegningDtoData>

data class ServiceberegningDtoData(
    val uttaksalder: Alder,
    val uttaksdato: String,
    val forventetFremtidigInntekt: Kroner,
    val afp: TidsbegrensetOffentligAfp
) : FagsystemBrevdata
