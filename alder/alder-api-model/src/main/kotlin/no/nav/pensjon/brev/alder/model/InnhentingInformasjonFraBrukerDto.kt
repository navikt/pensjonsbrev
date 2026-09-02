package no.nav.pensjon.brev.alder.model

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValgUtenFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")
data class InnhentingInformasjonFraBrukerDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
) : BrevdataMedSaksbehandlerValgUtenFagsystemdata
