package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.Foedselsnummer
import no.nav.pensjon.brev.api.model.Telefonnummer

fun Telefonnummer.format() =
    "([0-9][0-9])".toRegex().replace(value, "$1 ").trim()

fun Foedselsnummer.format() =
    "([0-9]{6})([0-9]{5})".toRegex().replace(value, "$1 $2")