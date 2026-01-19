package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDto.Saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysData
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class VarselFeilutbetalingUforeDto(
    override val pesysData: VarselFeilutbetalingPesysData,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<Saksbehandlervalg, VarselFeilutbetalingPesysData> {

    data class Saksbehandlervalg(
        @DisplayText("Vurdert rentetillegg")
        val rentetillegg: Boolean,
    ) : SaksbehandlerValgBrevdata
}
