package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData
) : RedigerbarBrevdata<EmptyBrevdata, AvslagUttakFoerNormertPensjonsalderDto.PesysData> {
    data class PesysData(val avslagUttakData: AvslagUttakFoerNormertPensjonsalderAutoDto) : BrevbakerBrevdata
}