package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE

data class PEDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, PEDto.PesysData> {
    data class PesysData(val pe: PE) : BrevbakerBrevdata
}