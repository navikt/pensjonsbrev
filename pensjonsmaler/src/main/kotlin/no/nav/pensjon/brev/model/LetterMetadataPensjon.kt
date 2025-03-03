package no.nav.pensjon.brev.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

data class LetterMetadataPensjon(
    override val displayTitle: String,
    override val isSensitiv: Boolean,
    override val distribusjonstype: LetterMetadata.Distribusjonstype,
    override val brevtype: LetterMetadata.Brevtype,
) : LetterMetadata {
    override fun equals(other: Any?): Boolean {
        if (other !is LetterMetadata) return false
        return displayTitle == other.displayTitle
                && isSensitiv == other.isSensitiv
                && distribusjonstype == other.distribusjonstype
                && brevtype == other.brevtype
    }

    override fun hashCode() =
        displayTitle.hashCode() + isSensitiv.hashCode() + distribusjonstype.hashCode() + brevtype.hashCode()
}