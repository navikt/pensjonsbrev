package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class FeilutbetalingSpesifikkVarselDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: VarselFeilutbetalingPesysData

) : RedigerbarBrevdata<EmptySaksbehandlerValg, VarselFeilutbetalingPesysData>

data class VarselFeilutbetalingPesysData(
    val feilutbetaltBrutto: Int
) :FagsystemBrevdata