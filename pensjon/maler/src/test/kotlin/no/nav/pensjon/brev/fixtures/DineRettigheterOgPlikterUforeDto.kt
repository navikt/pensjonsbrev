package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto

fun createDineRettigheterOgPlikterUforeDto() =
    DineRettigheterOgPlikterUforeDto(
        utland = true,
    )
