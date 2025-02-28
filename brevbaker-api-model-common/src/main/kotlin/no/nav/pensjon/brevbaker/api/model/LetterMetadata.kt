package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser

interface LetterMetadata {
    val displayTitle: String
    val isSensitiv: Boolean
    val distribusjonstype: Distribusjonstype
    val brevtype: Brevtype
    enum class Distribusjonstype { VEDTAK, VIKTIG, ANNET, }
    enum class Brevtype{ VEDTAKSBREV, INFORMASJONSBREV }
}

@InterneDataklasser
data class LetterMetadataImpl(
    override val displayTitle: String,
    override val isSensitiv: Boolean,
    override val distribusjonstype: LetterMetadata.Distribusjonstype,
    override val brevtype: LetterMetadata.Brevtype,
) : LetterMetadata