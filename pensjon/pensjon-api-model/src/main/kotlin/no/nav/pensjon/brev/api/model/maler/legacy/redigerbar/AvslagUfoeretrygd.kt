package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10

data class AvslagUfoeretrygdDto(
    val pe: PEgruppe10
) : FagsystemBrevdata