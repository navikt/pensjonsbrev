package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadRedigerbartDTO
import java.time.LocalDate

fun createOmstillingsstoenadMotattSoekdnadDTO() =
    OmstillingsstoenadMottattSoeknadDTO(
        innhold = emptyList(),
        mottattDato = LocalDate.of(2024, 5, 1),
        borINorgeEllerIkkeAvtaleland = true,
    )

fun createOmstillingsstoenadMotattSoekdnadRedigerbartDTO() =
    OmstillingsstoenadMottattSoeknadRedigerbartDTO(
        innhold = emptyList(),
        mottattDato = LocalDate.of(2024, 5, 1),
        borINorgeEllerIkkeAvtaleland = true,
    )
