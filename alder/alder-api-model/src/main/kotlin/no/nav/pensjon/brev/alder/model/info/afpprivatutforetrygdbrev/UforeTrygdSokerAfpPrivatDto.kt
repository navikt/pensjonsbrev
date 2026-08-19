package no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class UforeTrygdSokerAfpPrivatDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata,


    ) : BrevdataMedSaksbehandlerValg<EmptyFagsystemdata>


