@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

interface IntValue {
    val value: Int
}

class Telefonnummer(val value: String) {
    override fun equals(other: Any?): Boolean {
        if (other !is Telefonnummer) return false
        return value == other.value
    }

    override fun hashCode() = value.hashCode()

    override fun toString(): String = "Telefonnummer(value='$value')"
}

class Foedselsnummer(val value: String) {
    override fun equals(other: Any?): Boolean {
        if (other !is Foedselsnummer) return false
        return value == other.value
    }

    override fun hashCode() = value.hashCode()
}

data class Kroner(override val value: Int) : IntValue

@JvmInline
value class Year(override val value: Int) : IntValue

data class YearWrapper(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue

data class Broek(val teller: Int, val nevner: Int)
