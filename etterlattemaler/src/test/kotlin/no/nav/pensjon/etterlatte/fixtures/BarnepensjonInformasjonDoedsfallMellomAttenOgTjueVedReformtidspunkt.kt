package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktData

fun createBarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO() =
    BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO(
        data = BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktData(
            avdoedNavn = "Ola Nordmann",
            borIutland = false,
        )
    )
