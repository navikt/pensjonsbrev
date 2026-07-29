package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

@ConsistentCopyVisibility
data class LetterMarkup @MarkupInternalApi constructor(
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
data class Attachment @MarkupInternalApi constructor(
    val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
)

@ConsistentCopyVisibility
data class Saksinformasjon @MarkupInternalApi constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
    val saksnummer: Markup.Saksnummer,
    val dokumentDato: LocalDate,
)

@ConsistentCopyVisibility
data class Signatur @MarkupInternalApi constructor(
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
)

@ConsistentCopyVisibility
data class SaksbehandlerSignatur @MarkupInternalApi constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
)

@ConsistentCopyVisibility
data class PDFTittel @MarkupInternalApi constructor(
    val title1: List<Text>,
)
