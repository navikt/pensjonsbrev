package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto

fun createInformasjonOmGjenlevenderettigheterDto() = InformasjonOmGjenlevenderettigheterDto(
    sakstype = Sakstype.ALDER,
    gjenlevendesAlder = 65,
)