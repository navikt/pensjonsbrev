package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

@ConsistentCopyVisibility
@Serializable
data class LetterMarkupV2 internal constructor(
    val title1: List<Text>,
    val saksinformasjon: Saksinformasjon,
    val blocks: List<Block>,
    val signatur: Signatur,
    val version: Int = VERSION,
) {
    companion object {
        const val VERSION = 2
    }
}

@ConsistentCopyVisibility
@Serializable
data class Attachment internal constructor(
    override val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
) : AttachmentTitleV2

@ConsistentCopyVisibility
@Serializable
data class Saksinformasjon internal constructor(
    val gjelderNavn: String,
    val gjelderFoedselsnummer: Foedselsnummer,
    val annenMottakerNavn: String?,
    val saksnummer: Saksnummer,
    @Serializable(with = LocalDateSerializer::class)
    val dokumentDato: LocalDate,
)

@ConsistentCopyVisibility
@Serializable
data class Signatur internal constructor(
    val hilsenTekst: String,
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
)

@ConsistentCopyVisibility
@Serializable
data class SaksbehandlerSignatur internal constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
)

interface AttachmentTitleV2 {
    val title1: List<Text>
}

@ConsistentCopyVisibility
@Serializable
data class PDFTittelV2 internal constructor(
    override val title1: List<Text>,
) : AttachmentTitleV2

@ConsistentCopyVisibility
@Serializable
data class LetterMarkupWithDataUsageV2 internal constructor(
    val markup: LetterMarkupV2,
    val letterDataUsage: Set<Property>,
    val brevtype: Brevtype,
) {
    @ConsistentCopyVisibility
    @Serializable
    data class Property internal constructor(
        val typeName: String,
        val propertyName: String,
    )
}
