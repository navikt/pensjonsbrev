package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagIkkeYrkesskadeDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonAvslagIkkeYrkesskadeDTO() =
    BarnepensjonAvslagIkkeYrkesskadeDTO(
        dinForelder = "din mor",
        doedsdato = LocalDate.of(2018, Month.JULY, 10),
        yrkesskadeEllerYrkessykdom = "yrkesskade",
    )
