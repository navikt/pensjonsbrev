package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTO
import java.time.LocalDate

fun createKlageSaksbehandlingstidDtoTestI() =
    KlageSaksbehandlingstidDTO(
        SakType.BARNEPENSJON,
        true,
        LocalDate.now(),
        LocalDate.now(),
    )
