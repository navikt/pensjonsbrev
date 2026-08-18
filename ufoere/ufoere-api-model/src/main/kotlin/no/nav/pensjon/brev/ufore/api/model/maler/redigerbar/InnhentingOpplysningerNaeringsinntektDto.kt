package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class InnhentingOpplysningerNaeringsinntektDto(
    override val pesysData: EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<EmptyFagsystemdata>
