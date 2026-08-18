package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

@Suppress("unused")
data class AvslagUfoerepensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdataMedSaksbehandlerValg<AvslagUfoerepensjonDto.PesysData> {
    data class PesysData(
        val kravMottattDato: LocalDate,
    ) : FagsystemBrevdata
}