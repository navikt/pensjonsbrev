package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTO
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTO
import java.time.LocalDate

fun createAvvistKlageInnholdDTO() = AvvistKlageInnholdDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    klageDato = LocalDate.of(2024, 2, 29),
    datoForVedtaketKlagenGjelder = LocalDate.of(2023, 12, 12)
)

fun createAvvistKlageFerdigDTO() = AvvistKlageFerdigDTO(
    innhold = createManueltBrevDTO().innhold,
    data = AvvistKlageInnholdDTO(
        sakType = SakType.BARNEPENSJON,
        klageDato = LocalDate.of(2024, 2, 29),
        datoForVedtaketKlagenGjelder = LocalDate.of(2023, 12, 12),
        bosattUtland = true
    )
)