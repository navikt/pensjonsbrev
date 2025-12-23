package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata

data class FeilutbetalingVarselDodsboDto(
    override val saksbehandlerValg: FeilutbetalingDodsboSaksbehandlervalg,
    override val pesysData: VarselFeilutbetalingPesysData

) : RedigerbarBrevdata<FeilutbetalingDodsboSaksbehandlervalg, VarselFeilutbetalingPesysData>

data class FeilutbetalingDodsboSaksbehandlervalg(
    val kjentBobestyrer: Boolean,
    val ukjentBobestyrer: Boolean,
) : SaksbehandlerValgBrevdata