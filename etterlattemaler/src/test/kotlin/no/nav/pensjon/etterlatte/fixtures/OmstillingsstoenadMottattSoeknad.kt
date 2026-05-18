package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadData
import java.time.LocalDate

fun createOmstillingsstoenadMotattSoekdnadDTO() =
    OmstillingsstoenadMottattSoeknadDTO(
        data = OmstillingsstoenadMottattSoeknadData(
            mottattDato = LocalDate.of(2024, 5, 1),
            borINorgeEllerIkkeAvtaleland = true,
        )
    )
