package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTO
import java.time.LocalDate

fun createOmstillingsstoenadMotattSoekdnadDTO() =
    OmstillingsstoenadMottattSoeknadDTO(
        mottattDato = LocalDate.of(2024, 5, 1),
        borINorgeEllerIkkeAvtaleland = true,
    )
