package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InformasjonOmGjenlevenderettigheterDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData
) : RedigerbarBrevdata<EmptyBrevdata, InformasjonOmGjenlevenderettigheterDto.PesysData> {

    data class PesysData(
        val sakstype: Sakstype,
        val gjenlevendesAlder: Int,
        val avdoedNavn: String?,
    ) : BrevbakerBrevdata
}