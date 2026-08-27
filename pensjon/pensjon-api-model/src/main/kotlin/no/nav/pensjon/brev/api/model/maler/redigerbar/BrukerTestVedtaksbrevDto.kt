package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")

data class BrukerTestVedtaksbrevDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValg<EmptyFagsystemdata> {
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
