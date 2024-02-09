package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO

fun createOmstillingsstoenadAvslagDTO() =
    OmstillingstoenadAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        avdoedNavn = "Ola Nordmann",
        bosattUtland = false
    )