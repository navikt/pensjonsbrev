@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import java.util.Objects


object BrevbakerType {

    interface IntValue {
        val value: Int
    }

    @JvmInline
    value class Telefonnummer(val value: String) {
        override fun toString() = value

        init {
            require(value.length <= 32) {
                "Telefonnummer må være mindre enn eller lik 32 tegn, var ${value.length}"
            }
        }
    }

    @JvmInline
    value class Pid(val value: String) {
        override fun toString() = value

        init {
            require(value.length <= 32) {
                "Pid må være mindre enn eller lik 32 tegn, var ${value.length}"
            }
        }
    }

    @JvmInline
    value class Foedselsnummer(val value: String) {
        override fun toString() = value

        init {
            require(value.length <= 32) {
                "Foedselsnummer må være mindre enn eller lik 32 tegn, var ${value.length}"
            }
        }
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

    @JvmInline
    value class VedleggId(val id: String) {
        init {
            require(id.isNotBlank()) { "VedleggId kan ikke være blank" }
            require(id.length <= 128) { "VedleggId må være mindre enn eller lik 128 tegn, var ${id.length}" }
        }
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