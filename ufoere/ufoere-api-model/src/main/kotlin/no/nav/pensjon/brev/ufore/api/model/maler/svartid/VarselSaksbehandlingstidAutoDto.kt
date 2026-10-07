package no.nav.pensjon.brev.ufore.api.model.maler.svartid

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import java.time.LocalDate


data class VarselSaksbehandlingstidAutoDto(
    val dagensDatoMinus2Dager: LocalDate,
    val utvidetBehandlingstid: Boolean,
) : AutobrevData