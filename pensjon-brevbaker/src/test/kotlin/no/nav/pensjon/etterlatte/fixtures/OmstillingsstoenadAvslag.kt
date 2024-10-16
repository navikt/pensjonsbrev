package no.nav.pensjon.etterlatte.fixtures

import io.mockk.mockk
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO

fun createOmstillingsstoenadAvslagDTO() =
    OmstillingstoenadAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false,
        erSluttbehandling = false,
    )

fun createOmstillingsstoenadAvslagRedigerbartUtfallDTO() = OmstillingstoenadAvslagRedigerbartUtfallDTO(avdoedNavn = "Ola Nordmann")
