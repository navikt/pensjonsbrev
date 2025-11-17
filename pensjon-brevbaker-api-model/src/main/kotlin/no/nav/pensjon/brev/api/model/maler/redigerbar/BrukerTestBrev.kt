package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")

data class BrukerTestBrevDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<BrukerTestBrevDto.SaksbehandlerValg, EmptyFagsystemdata> {
    data class SaksbehandlerValg(
        @DisplayText("Utsikten fra kontoret")
        val utsiktenFraKontoret: UtsiktenFraKontoret?,
        @DisplayText("Den beste kaken er")
        val denBesteKaken: DenBesteKaken?,
        @DisplayText("Kaffemaskinens tilgjengelighet")
        val kaffemaskinensTilgjengelighet: Boolean,
        @DisplayText("Kontorplanten TorLill")
        val kontorplantenTorlill: Boolean,
    ) : SaksbehandlerValgBrevdata

    enum class UtsiktenFraKontoret {
        @DisplayText("Trær og natur")
        MOT_TRAER_OG_NATUR,
        @DisplayText("Parkeringsplass")
        MOT_PARKERINGSPLASSEN,
    }

    enum class DenBesteKaken {
        @DisplayText("Gulrotkake")
        GULROTKAKE,
        @DisplayText("Rullekake")
        RULLEKAKE,
        @DisplayText("Ostekake")
        OSTEKAKE,
        @DisplayText("Kaken er en løgn")
        KAKENERLOEGN,
    }
}