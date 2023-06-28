package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.PraktiskInformasjonEtteroppgjoerDto
import java.time.LocalDate

fun createPraktiskInformasjonEtteroppgjoerDto() =
    PraktiskInformasjonEtteroppgjoerDto(
        ufoeretrygdEtterOppgjoerPeriodeFom = LocalDate.of(2022,1,1 )
    )