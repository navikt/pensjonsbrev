package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallData
import java.time.LocalDate

fun createBarnepensjonAvslagRedigerbartUtfallDTO(
    erSluttbehandling: Boolean = true,
): BarnepensjonAvslagRedigerbartUtfallDTO =
    BarnepensjonAvslagRedigerbartUtfallDTO(
        data = BarnepensjonAvslagRedigerbartUtfallData(
            avdoedNavn = "Ola Nordmann",
            avdoedDoedsdato = LocalDate.of(2020, 1, 1),
            erSluttbehandling = erSluttbehandling,
        ),
    )

fun createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato(
    erSluttbehandling: Boolean,
): BarnepensjonAvslagRedigerbartUtfallDTO =
    BarnepensjonAvslagRedigerbartUtfallDTO(
        data =
            BarnepensjonAvslagRedigerbartUtfallData(
                avdoedNavn = "Ola Nordmann",
                avdoedDoedsdato = null,
                erSluttbehandling = erSluttbehandling,
            ),
    )