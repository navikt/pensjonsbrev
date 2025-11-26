package no.nav.pensjon.etterlatte.fixtures

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTO

fun createKlageSaksbehandlingstidDtoTestI() = KlageSaksbehandlingstidDTO(
    SakType.BARNEPENSJON,
    true,
    vilkaarligDato,
    vilkaarligDato
)