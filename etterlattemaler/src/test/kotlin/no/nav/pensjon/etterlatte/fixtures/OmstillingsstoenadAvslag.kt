package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallData
import java.time.LocalDate

fun createOmstillingsstoenadAvslagDTO() =
    OmstillingstoenadAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = OmstillingstoenadAvslagData(bosattUtland = false),
    )

fun createOmstillingsstoenadAvslagRedigerbartUtfallDTO() =
    OmstillingstoenadAvslagRedigerbartUtfallDTO(
        data = OmstillingstoenadAvslagRedigerbartUtfallData(
            avdoedNavn = "Ola Nordmann",
            erSluttbehandling = true,
            tidligereFamiliepleier = false,
            avdoedDoedsdato = LocalDate.of(2024, 1, 1),
        )
    )
