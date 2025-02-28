package no.nav.pensjon.brev.model

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

data class LetterMetadataPensjon(
    override val displayTitle: String,
    override val isSensitiv: Boolean,
    override val distribusjonstype: LetterMetadata.Distribusjonstype,
    override val brevtype: LetterMetadata.Brevtype,
) : LetterMetadata