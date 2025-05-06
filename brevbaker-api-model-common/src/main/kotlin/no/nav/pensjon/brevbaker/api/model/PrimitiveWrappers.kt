@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser

interface IntValue {
    val value: Int
}

class Telefonnummer internal constructor(val value: String) {
    companion object {
        fun from(string: String): Telefonnummer = Telefonnummer(string)
    }
}

@InterneDataklasser
data class FoedselsnummerImpl(override val value: String) : Foedselsnummer

interface Foedselsnummer {
    val value: String
}

data class Kroner(override val value: Int) : IntValue

data class Year(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue

data class Broek(val teller: Int, val nevner: Int)
