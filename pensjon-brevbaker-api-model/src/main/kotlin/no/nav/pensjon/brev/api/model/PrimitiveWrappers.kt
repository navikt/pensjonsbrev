package no.nav.pensjon.brev.api.model


interface IntValue {
    val value: Int
}

@JvmInline
value class Telefonnummer(val value: String)

@JvmInline
value class Foedselsnummer(val value: String)

@JvmInline
value class Kroner(val value: Int)

@JvmInline
value class Year(override val value: Int) : IntValue

@JvmInline
value class Months(override val value: Int) : IntValue

@JvmInline
value class Days(override val value: Int) : IntValue

@JvmInline
value class Percent(override val value: Int) : IntValue
