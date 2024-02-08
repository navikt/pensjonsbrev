package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTO
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType

fun createAvvistKlageInnholdDTO() = AvvistKlageInnholdDTO(
    sakType = SakType.OMSTILLINGSSTOENAD
)

fun createAvvistKlageFerdigDTO() = AvvistKlageFerdigDTO(
    innhold = createManueltBrevDTO().innhold, data = AvvistKlageInnholdDTO(sakType = SakType.BARNEPENSJON)
)