package no.nav.pensjon.brev.maler.fraser.common

import java.time.LocalDate

interface LocalDateValue {
    val date: LocalDate
}

@JvmInline
value class KravVirkningFraOgMed(override val date: LocalDate): LocalDateValue

@JvmInline
value class Kroner(val value: Int) {
    override fun toString(): String {
        return value.toString()
    }
}

interface DoubleValue {
    val value: Double
}

@JvmInline
value class GrunnbeloepSats(override val value: Double): DoubleValue {
    override fun toString(): String {
        return value.toString()
    }
}