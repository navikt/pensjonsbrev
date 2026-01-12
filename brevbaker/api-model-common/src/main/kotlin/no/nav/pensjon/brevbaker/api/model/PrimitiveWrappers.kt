@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

interface IntValue {
    val value: Int
}

@JvmInline
value class Telefonnummer(val value: String)

@JvmInline
value class Foedselsnummer(val value: String)

@JvmInline
value class Kroner(override val value: Int) : IntValue

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