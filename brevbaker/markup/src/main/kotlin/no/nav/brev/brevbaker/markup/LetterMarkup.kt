package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

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

data class Attachment @MarkupInternalApi constructor(
    val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
)

data class Saksinformasjon @MarkupInternalApi constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
    val saksnummer: Markup.Saksnummer,
    val dokumentDato: LocalDate,
)

data class Signatur @MarkupInternalApi constructor(
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
)

data class SaksbehandlerSignatur @MarkupInternalApi constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
)

data class PDFTittel @MarkupInternalApi constructor(
    val title1: List<Text>,
)
