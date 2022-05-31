package no.nav.pensjon.brev.api.model

data class Telefonnummer(val value: String)
data class Foedselsnummer(val value: String)

interface IntValue{
    val value: Int
}

@JvmInline
value class Kroner(val value: Int){
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class Year(override val value: Int): IntValue{
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class Months(override val value: Int): IntValue{
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class Days(override val value: Int): IntValue{
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class Percent(override val value: Int): IntValue{
    override fun toString(): String {
        return value.toString()
    }
}
