package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

data class UforeAvslagDto(
    val kravMottattDato: LocalDate,
) : FagsystemBrevdata
