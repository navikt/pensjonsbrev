package no.nav.pensjon.brev.alder.model.avslag

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class AvslagUttakFoerNormertPensjonsalderDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAutoDto,
) : BrevdataMedSaksbehandlerValg<AvslagUttakFoerNormertPensjonsalderAutoDto>

data class AvslagUttakFoerNormertPensjonsalderAP2016Dto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: AvslagUttakFoerNormertPensjonsalderAP2016AutoDto,
) : BrevdataMedSaksbehandlerValg<AvslagUttakFoerNormertPensjonsalderAP2016AutoDto>

data class AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto,
) : BrevdataMedSaksbehandlerValg<AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto>