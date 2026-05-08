package no.nav.pensjon.etterlatte.fixtures
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO

fun createOmstillingsstoenadAvslagDTO() =
    OmstillingstoenadAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = OmstillingstoenadAvslagData(bosattUtland = false),
    )

fun createOmstillingsstoenadAvslagRedigerbartUtfallDTO() =
    OmstillingstoenadAvslagRedigerbartUtfallDTO(
        avdoedNavn = "Ola Nordmann",
        erSluttbehandling = true,
        tidligereFamiliepleier = false,
    )
