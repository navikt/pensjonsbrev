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

class Foedselsnummer internal constructor(
    val value: String
) {
    companion object {
        fun from(string: String): Foedselsnummer = Foedselsnummer(string)
    }
}

data class Kroner(override val value: Int) : IntValue

data class Year(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue

data class Broek(val teller: Int, val nevner: Int)
