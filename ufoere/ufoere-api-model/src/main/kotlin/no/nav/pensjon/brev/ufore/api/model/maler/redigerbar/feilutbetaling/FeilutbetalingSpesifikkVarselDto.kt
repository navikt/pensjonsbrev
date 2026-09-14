package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class FeilutbetalingSpesifikkVarselDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: VarselFeilutbetalingPesysData,
) : RedigerbarBrevdata<VarselFeilutbetalingPesysData>

data class VarselFeilutbetalingPesysData(
    val feilutbetaltBrutto: Int,
) : FagsystemBrevdata
