package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

data class AvslagUfoeretrygdDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, AvslagUfoeretrygdDto.PesysData> {
    data class PesysData(val pe: PE, val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto) : BrevbakerBrevdata
}