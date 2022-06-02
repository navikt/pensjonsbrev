package no.nav.pensjon.brev.api.model

data class Telefonnummer(val value: String)
data class Foedselsnummer(val value: String)

interface IntValue{
    val value: Int
}

@JvmInline
value class Kroner(val value: Int)

@JvmInline
value class Year(override val value: Int): IntValue

@JvmInline
value class Months(override val value: Int): IntValue

@JvmInline
value class Days(override val value: Int): IntValue

@JvmInline
value class Percent(override val value: Int): IntValue
