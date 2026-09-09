package no.nav.pensjon.brev.skribenten.common

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap

data class GeneriskRedigerbarBrevdata(
    override val pesysData: FagsystemBrevdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdata<FagsystemBrevdata> {
    constructor(pesysData: Api.GeneriskBrevdata, saksbehandlerValg: SaksbehandlervalgMap) : this(
        pesysData,
        GeneriskSaksbehandlervalg(saksbehandlerValg)
    )

    private class GeneriskSaksbehandlervalg(saksbehandlervalgMap: SaksbehandlervalgMap) : SaksbehandlervalgIDSL,
        LinkedHashMap<String, Any?>() {
        init {
            putAll(saksbehandlervalgMap)
        }
    }
}