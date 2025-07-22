package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class AdhocAlderspensjonSkjermingstilleggDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<EmptyBrevdata, EmptyBrevdata>