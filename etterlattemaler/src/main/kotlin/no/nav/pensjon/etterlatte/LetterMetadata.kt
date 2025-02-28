package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

data class LetterMetadataEtterlatte(
    override val displayTitle: String,
    override val isSensitiv: Boolean,
    override val distribusjonstype: LetterMetadata.Distribusjonstype,
    override val brevtype: LetterMetadata.Brevtype,
) : LetterMetadata