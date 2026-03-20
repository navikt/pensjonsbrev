package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class AvslagGjenlevendepensjonUtlandDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: EmptyFagsystemdata
) : RedigerbarBrevdata<EmptySaksbehandlerValg, EmptyFagsystemdata>

/*  data class PesysData(
    val mottattDato: LocalDateTime,  //TODO  ikke støtte for PEbrevgruppe2
) : FagsystemBrevdata
*/