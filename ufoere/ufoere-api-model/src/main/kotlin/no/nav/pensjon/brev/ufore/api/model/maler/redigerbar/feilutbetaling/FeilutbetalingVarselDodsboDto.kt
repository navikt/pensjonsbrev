package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class FeilutbetalingVarselDodsboDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: VarselFeilutbetalingPesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VarselFeilutbetalingPesysData>
