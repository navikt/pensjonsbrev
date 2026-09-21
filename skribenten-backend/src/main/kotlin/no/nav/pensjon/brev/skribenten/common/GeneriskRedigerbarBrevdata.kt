package no.nav.pensjon.brev.skribenten.common

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class GeneriskRedigerbarBrevdata(
    override val pesysData: FagsystemBrevdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdata<FagsystemBrevdata>