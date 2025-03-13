package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAutoDto
) : RedigerbarBrevdata<EmptyBrevdata, AvslagUttakFoerNormertPensjonsalderAutoDto>