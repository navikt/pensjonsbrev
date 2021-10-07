package no.nav.pensjon.brev.api.model

data class Telefonnummer(override val value: String): JsonInlineValue<String>
data class Foedselsnummer(override val value: String): JsonInlineValue<String>
