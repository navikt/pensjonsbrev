package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")

data class BrukerTestBrevDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<EmptyFagsystemdata> {
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

    enum class UtsiktenFraKontoret(override val displayText: String) : SaksbehandlerValgEnum {
        MOT_TRAER_OG_NATUR("Trær og natur"),
        MOT_PARKERINGSPLASSEN("Parkeringsplass"),
    }

    enum class DenBesteKaken(override val displayText: String) : SaksbehandlerValgEnum {
        GULROTKAKE("Gulrotkake"),
        RULLEKAKE("Rullekake"),
        OSTEKAKE("Ostekake"),
        KAKENERLOEGN("Kaken er en løgn"),
    }
}