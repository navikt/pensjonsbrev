package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import java.time.LocalDate

fun createBekreftelsePaaUfoeretrygdDto() = BekreftelsePaaUfoeretrygdDto(
    foedselsdato = LocalDate.of(1950, 1, 1),
    navn = "Per Pensjon",
)