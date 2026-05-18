package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerData

fun createBarnepensjonInnhentingAvOpplysningerDTO() =
    BarnepensjonInnhentingAvOpplysningerDTO(
        data = BarnepensjonInnhentingAvOpplysningerData(
            erOver18aar = false,
            borIUtlandet = true,
        )
    )
