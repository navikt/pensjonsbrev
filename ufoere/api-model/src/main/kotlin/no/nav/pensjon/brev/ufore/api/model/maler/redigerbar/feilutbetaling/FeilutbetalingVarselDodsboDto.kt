package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class FeilutbetalingVarselDodsboDto(
    override val saksbehandlerValg: FeilutbetalingDodsboSaksbehandlervalg,
    override val pesysData: VarselFeilutbetalingPesysData

) : RedigerbarBrevdata<FeilutbetalingDodsboSaksbehandlervalg, VarselFeilutbetalingPesysData>

data class FeilutbetalingDodsboSaksbehandlervalg(
    @DisplayText("Kjent bobestyrer")
    val kjentBobestyrer: Boolean = true,
) : SaksbehandlerValgBrevdata