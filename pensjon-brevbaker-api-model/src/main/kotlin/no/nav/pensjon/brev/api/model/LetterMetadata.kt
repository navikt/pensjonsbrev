package no.nav.pensjon.brev.api.model

data class LetterMetadata(
    val displayTitle: String,
    val isSensitiv: Boolean,
    val distribusjonstype: Distribusjonstype,
) {
    enum class Distribusjonstype { VEDTAK, VIKTIG, ANNET, }
}
