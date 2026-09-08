package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

data class UforeAvslagEnkelDto(
    val kravMottattDato: LocalDate,
    val vurdering: String
) : FagsystemBrevdata
