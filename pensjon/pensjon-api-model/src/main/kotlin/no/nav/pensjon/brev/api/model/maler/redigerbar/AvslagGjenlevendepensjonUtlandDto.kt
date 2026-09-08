package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

@Suppress("unused")
data class AvslagGjenlevendepensjonUtlandDto(
    val kravMottattDato: LocalDate,
) : FagsystemBrevdata
