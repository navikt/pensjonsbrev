package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadData
import java.time.LocalDate

fun createBarnepensjonInformasjonMottattSoeknadDTO() =
    BarnepensjonMottattSoeknadDTO(
        data = BarnepensjonMottattSoeknadData(
            mottattDato = LocalDate.of(2024, 1, 1),
            bosattUtland = true,
            erOver18aar = false,
            borINorgeEllerIkkeAvtaleland = true,
        )
    )
