package no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof

import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDto

fun createFullmektigBprofAutoDto() =
    FullmektigBprofAutoDto(
        navnFullmaktsgiver = "Ola 'Fullmaktsgiver' Nordmann",
    )