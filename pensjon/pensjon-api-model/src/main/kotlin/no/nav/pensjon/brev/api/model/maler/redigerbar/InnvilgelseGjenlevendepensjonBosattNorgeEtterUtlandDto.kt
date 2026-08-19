package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

@Suppress("unused")
data class InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto.PesysData> {
    data class PesysData(
        val kravMottattDato: LocalDate,
    ) : FagsystemBrevdata
}

