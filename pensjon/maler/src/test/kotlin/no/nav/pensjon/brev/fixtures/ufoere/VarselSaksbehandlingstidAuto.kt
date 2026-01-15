package no.nav.pensjon.brev.fixtures.ufoere

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto

fun createVarselSaksbehandlingstidAutoDto() =
    VarselSaksbehandlingstidAutoDto(
        dagensDatoMinus2Dager = vilkaarligDato,
        utvidetBehandlingstid = false,
    )