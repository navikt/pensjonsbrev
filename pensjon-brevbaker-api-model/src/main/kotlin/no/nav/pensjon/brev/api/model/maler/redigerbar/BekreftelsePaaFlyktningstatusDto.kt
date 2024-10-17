package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE

data class BekreftelsePaaFlyktningstatusDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: Pesysdata
) : RedigerbarBrevdata<EmptyBrevdata, BekreftelsePaaFlyktningstatusDto.Pesysdata> {
    data class Pesysdata(val pe: PE, val avsenderEnhet: String) : BrevbakerBrevdata
}