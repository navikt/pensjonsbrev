@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import java.util.Objects


object BrevWrappers {

    interface IntValue {
        val value: Int
    }

    @JvmInline
    value class Telefonnummer(val value: String) {
        override fun toString() = value
    }

    @JvmInline
    value class Pid(val value: String) {
        override fun toString() = value
    }

    @JvmInline
    value class Foedselsnummer(val value: String) {
        override fun toString() = value
    }

    @JvmInline
    value class Kroner(override val value: Int) : IntValue {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class Year(override val value: Int) : IntValue {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class Months(override val value: Int) : IntValue {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class Days(override val value: Int) : IntValue {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class Percent(override val value: Int) : IntValue {
        override fun toString() = value.toString()
    }

    class Broek(val teller: Int, val nevner: Int) {
        override fun equals(other: Any?): Boolean {
            if (other !is Broek) return false
            return teller == other.teller && nevner == other.nevner
        }

        override fun hashCode() = Objects.hash(teller, nevner)

        override fun toString() = "Broek(teller=$teller, nevner=$nevner)"
    }
}