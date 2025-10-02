package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016Dto.SaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAutoDto
) : RedigerbarBrevdata<AvslagUttakFoerNormertPensjonsalderDto.SaksbehandlerValg, AvslagUttakFoerNormertPensjonsalderAutoDto> {
    data class SaksbehandlerValg(
        @DisplayText("Hvis bruker ikke har rett til å ta ut alderspensjon før 67 år")
        val visInfoOmUttakFoer67: Boolean
    ) : BrevbakerBrevdata
}

data class AvslagUttakFoerNormertPensjonsalderAP2016Dto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
) : RedigerbarBrevdata<SaksbehandlerValg, AvslagUttakFoerNormertPensjonsalderAP2016AutoDto> {
    data class SaksbehandlerValg(
        @DisplayText("Hvis bruker ikke har rett til å ta ut alderspensjon før 67 år")
        val visInfoOmUttakFoer67: Boolean
    ) : BrevbakerBrevdata
}

data class AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
) : RedigerbarBrevdata<EmptyBrevdata, AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto>