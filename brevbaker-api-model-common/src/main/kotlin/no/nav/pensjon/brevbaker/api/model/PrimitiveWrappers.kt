package no.nav.pensjon.brevbaker.api.model

interface IntValue {
    val value: Int
}

data class Telefonnummer(val value: String)

data class Foedselsnummer(val value: String)

data class Kroner(override val value: Int) : IntValue

data class Year(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue
