package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata

data class VarselFeilutbetalingPesysData(
    val feilutbetaltBrutto: Int,
) : FagsystemBrevdata