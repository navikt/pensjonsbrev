package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTO
import java.time.LocalDate

fun createBarnepensjonInformasjonMottattSoeknadDTO() =
    BarnepensjonMottattSoeknadDTO(
        mottattDato = LocalDate.of(2024, 1, 1),
        bosattUtland = true,
        erOver18aar = false,
        borINorgeEllerIkkeAvtaleland = true,
    )
