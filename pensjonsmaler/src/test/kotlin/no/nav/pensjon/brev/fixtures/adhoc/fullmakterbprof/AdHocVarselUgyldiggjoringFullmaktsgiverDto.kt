package no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof

import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmaktsgiverBprofAutoDto

fun createFullmaktsgiverBprofAutoDto() =
    FullmaktsgiverBprofAutoDto(
        navnFullmektig = "Ola 'Fullmektig' Nordmann",
    )