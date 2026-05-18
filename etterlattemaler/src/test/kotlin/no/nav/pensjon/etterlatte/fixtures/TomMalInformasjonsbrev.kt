package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelData

fun createTomMalInformasjonsbrev() =
    ManueltBrevMedTittelDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = ManueltBrevMedTittelData(tittel = "Dette er et informasjonsbrev")
    )