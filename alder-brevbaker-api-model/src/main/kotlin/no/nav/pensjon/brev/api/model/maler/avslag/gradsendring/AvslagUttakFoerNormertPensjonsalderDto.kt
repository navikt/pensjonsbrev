package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016Dto.SaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAutoDto
) : RedigerbarBrevdata<AvslagUttakFoerNormertPensjonsalderDto.SaksbehandlerValg, AvslagUttakFoerNormertPensjonsalderAutoDto> {
    data class SaksbehandlerValg(
        @DisplayText("Hvis bruker ikke har rett til å ta ut alderspensjon før 67 år")
        val visInfoOmUttakFoer67: Boolean?
    ) : SaksbehandlerValgBrevdata
}

data class AvslagUttakFoerNormertPensjonsalderAP2016Dto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
) : RedigerbarBrevdata<SaksbehandlerValg, AvslagUttakFoerNormertPensjonsalderAP2016AutoDto> {
    data class SaksbehandlerValg(
        @DisplayText("Hvis bruker ikke har rett til å ta ut alderspensjon før 67 år")
        val visInfoOmUttakFoer67: Boolean?
    ) : SaksbehandlerValgBrevdata
}

data class AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
) : RedigerbarBrevdata<EmptySaksbehandlerValg, AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto>