package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")

data class BrukerTestBrevDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<EmptyFagsystemdata> {
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