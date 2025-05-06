@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

interface IntValue {
    val value: Int
}

class Telefonnummer(val value: String)

class Foedselsnummer(val value: String)

data class Kroner(override val value: Int) : IntValue

data class Year(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue

data class Broek(val teller: Int, val nevner: Int)
