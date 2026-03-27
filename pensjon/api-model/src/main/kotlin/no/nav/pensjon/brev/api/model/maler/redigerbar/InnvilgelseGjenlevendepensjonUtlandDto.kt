package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class InnvilgelseGjenlevendepensjonUtlandDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseGjenlevendepensjonUtlandDto.PesysData> {
    data class PesysData(
        val kravMottattDato: LocalDate,
    ) : FagsystemBrevdata
}

