package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterfaceMedSelectors
import no.nav.brev.InterneDataklasser

interface IntValue {
    val value: Int
}

@InterfaceMedSelectors
interface Telefonnummer {
    val value: String
}

@InterneDataklasser
data class TelefonnummerImpl(override val value: String) : Telefonnummer

@InterneDataklasser
data class FoedselsnummerImpl(override val value: String) : Foedselsnummer

@InterfaceMedSelectors
interface Foedselsnummer {
    val value: String
}

data class Kroner(override val value: Int) : IntValue

data class Year(override val value: Int) : IntValue

data class Months(override val value: Int) : IntValue

data class Days(override val value: Int) : IntValue

data class Percent(override val value: Int) : IntValue
