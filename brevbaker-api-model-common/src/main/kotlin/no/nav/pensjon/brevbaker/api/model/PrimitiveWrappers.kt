@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

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

@JvmInline
value class Months(override val value: Int) : IntValue

@JvmInline
value class Days(override val value: Int) : IntValue

@JvmInline
value class Percent(override val value: Int) : IntValue

class Broek(val teller: Int, val nevner: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Broek) return false
        return teller == other.teller && nevner == other.nevner
    }

    override fun hashCode() = Objects.hash(teller, nevner)

    override fun toString() = "Broek(teller=$teller, nevner=$nevner)"
}


// TODO: Alle disse under her skal slettes når pesys er oppdatert til ny modell
data class YearWrapper(override val value: Int) : IntWrapper

data class MonthsWrapper(override val value: Int) : IntWrapper

data class DaysWrapper(override val value: Int) : IntWrapper

data class PercentWrapper(override val value: Int) : IntWrapper

interface IntWrapper {
    val value: Int
}

