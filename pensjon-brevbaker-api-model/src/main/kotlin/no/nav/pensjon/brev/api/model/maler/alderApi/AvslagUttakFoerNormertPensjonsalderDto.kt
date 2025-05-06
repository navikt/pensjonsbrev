package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAutoDto
) : RedigerbarBrevdata<EmptyBrevdata, AvslagUttakFoerNormertPensjonsalderAutoDto>

data class AvslagUttakFoerNormertPensjonsalderAP2016Dto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
) : RedigerbarBrevdata<EmptyBrevdata, AvslagUttakFoerNormertPensjonsalderAP2016AutoDto>

data class AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
) : RedigerbarBrevdata<EmptyBrevdata, AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto>