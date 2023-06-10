package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.PraktiskInformasjonEtterOppgjoerDto
import java.time.LocalDate

fun createPraktiskInformasjonEtterOppgjoerDto() =
    PraktiskInformasjonEtterOppgjoerDto(
        ufoeretrygdEtterOppgjoerPeriodeFom = LocalDate.of(2022,1,1 )
    )