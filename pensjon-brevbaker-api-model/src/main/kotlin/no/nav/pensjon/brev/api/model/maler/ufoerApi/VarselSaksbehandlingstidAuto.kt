package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

@Suppress("unused")
data class VarselSaksbehandlingstidAutoDto(
    val dagensDatoMinus2Dager: LocalDate,
    val utvidetBehandlingstid: Boolean,
) : BrevbakerBrevdata
