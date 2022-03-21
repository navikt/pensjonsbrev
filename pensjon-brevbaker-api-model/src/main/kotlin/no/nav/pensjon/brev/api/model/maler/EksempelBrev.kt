package no.nav.pensjon.brev.api.model.maler

import java.time.LocalDate

data class EksempelBrevDto(val pensjonInnvilget: Boolean, val datoInnvilget: LocalDate) {
    // No-arg constructor for integration tests
    constructor() : this(true, LocalDate.now())
}