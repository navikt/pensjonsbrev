package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDto.Saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class InnhentingOpplysningerSamboerDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<Saksbehandlervalg, EmptyFagsystemdata> {

    data class Saksbehandlervalg(
        @DisplayText("Ukjent samboer")
        val ukjentSamboer: Boolean = false,
    ) : SaksbehandlerValgBrevdata
}
