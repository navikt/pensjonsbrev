package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO

fun createBarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO() =
    BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann",
        borIutland =  false,
    )
