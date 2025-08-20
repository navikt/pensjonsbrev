package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")

data class BrukerTestBrevDto(
    override val pesysData: EmptyBrevdata,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<BrukerTestBrevDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        @DisplayText("Utsikten fra kontoret")
        val utsiktenFraKontoret: UtsiktenFraKontoret,
        @DisplayText("Den beste kaken er")
        val denBesteKaken: DenBesteKaken,
        @DisplayText("Kaffemaskinens tilgjengelighet")
        val kaffemaskinensTilgjengelighet: Boolean,
        @DisplayText("Kontorplanten TorLill")
        val kontorplantenTorlill: Boolean,
    ) : BrevbakerBrevdata

    enum class UtsiktenFraKontoret {
        @DisplayText("Mot trær og natur")
        MOT_TRAER_OG_NATUR,
        @DisplayText("Mot parkeringsplassen")
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