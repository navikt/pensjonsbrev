package no.nav.pensjon.etterlatte.fixtures

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTO
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidData

fun createKlageSaksbehandlingstidDtoTestI() = KlageSaksbehandlingstidDTO(
    data = KlageSaksbehandlingstidData(
        sakType = SakType.BARNEPENSJON,
        borIUtlandet = true,
        datoMottatKlage = vilkaarligDato,
        datoForVedtak = vilkaarligDato,
    )
)