package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.Brevtype
import no.nav.brev.brevbaker.markup.Foedselsnummer
import no.nav.brev.brevbaker.markup.LetterMarkupV2
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsageV2
import no.nav.brev.brevbaker.markup.PDFTittelV2
import no.nav.brev.brevbaker.markup.Saksinformasjon
import no.nav.brev.brevbaker.markup.SaksbehandlerSignatur
import no.nav.brev.brevbaker.markup.Saksnummer
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

/**
 * Bygg en [LetterMarkupV2] via DSL. Dette er den eneste måten å konstruere markup-modellen på.
 */
fun letterMarkup(build: LetterMarkupBuilder.() -> Unit): LetterMarkupV2 =
    LetterMarkupBuilder(IdGenerator()).apply(build).build()

/**
 * Bygg en gjenbrukbar outline (liste av [Block]) uavhengig av brev. Kan brukes til ikke-brev-innhold.
 */
fun outline(build: OutlineBuilder.() -> Unit): List<Block> =
    OutlineBuilder(IdGenerator()).apply(build).build()

/**
 * Bygg et [Attachment] via DSL.
 */
fun attachment(inkluderSaksinformasjon: Boolean, build: AttachmentBuilder.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), inkluderSaksinformasjon).apply(build).build()

/**
 * Bygg en [PDFTittelV2] via DSL.
 */
fun pdfTittel(content: ContentBuilder.() -> Unit): PDFTittelV2 =
    PDFTittelV2(IdGenerator().content(content))

@MarkupDsl
class LetterMarkupBuilder internal constructor(private val ids: IdGenerator) {
    private var title1: List<Text> = emptyList()
    private var saksinformasjon: Saksinformasjon? = null
    private var blocks: List<Block> = emptyList()
    private var signatur: Signatur? = null

    fun title(content: ContentBuilder.() -> Unit) {
        title1 = ids.content(content)
    }

    fun saksinformasjon(
        gjelderNavn: String,
        gjelderFoedselsnummer: String,
        saksnummer: String,
        dokumentDato: LocalDate,
        annenMottakerNavn: String? = null,
    ) {
        saksinformasjon = Saksinformasjon(
            gjelderNavn = gjelderNavn,
            gjelderFoedselsnummer = Foedselsnummer(gjelderFoedselsnummer),
            annenMottakerNavn = annenMottakerNavn,
            saksnummer = Saksnummer(saksnummer),
            dokumentDato = dokumentDato,
        )
    }

    fun outline(build: OutlineBuilder.() -> Unit) {
        blocks = OutlineBuilder(ids).apply(build).build()
    }

    fun signatur(
        hilsenTekst: String,
        navAvsenderEnhet: String,
        saksbehandlerNavn: String? = null,
        attesterendeSaksbehandlerNavn: String? = null,
    ) {
        signatur = Signatur(
            hilsenTekst = hilsenTekst,
            saksbehandlerSignatur = saksbehandlerNavn?.let {
                SaksbehandlerSignatur(it, attesterendeSaksbehandlerNavn)
            },
            navAvsenderEnhet = navAvsenderEnhet,
        )
    }

    internal fun build(): LetterMarkupV2 = LetterMarkupV2(
        title1 = title1,
        saksinformasjon = requireNotNull(saksinformasjon) { "LetterMarkupV2 must have saksinformasjon" },
        blocks = blocks,
        signatur = requireNotNull(signatur) { "LetterMarkupV2 must have signatur" },
    )
}

@MarkupDsl
class AttachmentBuilder internal constructor(
    private val ids: IdGenerator,
    private val inkluderSaksinformasjon: Boolean,
) {
    private var title1: List<Text> = emptyList()
    private var blocks: List<Block> = emptyList()

    fun title(content: ContentBuilder.() -> Unit) {
        title1 = ids.content(content)
    }

    fun outline(build: OutlineBuilder.() -> Unit) {
        blocks = OutlineBuilder(ids).apply(build).build()
    }

    internal fun build(): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)
}

/**
 * Bygg en [LetterMarkupWithDataUsageV2.Property] via DSL.
 */
fun dataUsageProperty(typeName: String, propertyName: String): LetterMarkupWithDataUsageV2.Property =
    LetterMarkupWithDataUsageV2.Property(typeName, propertyName)

/**
 * Bygg en [LetterMarkupWithDataUsageV2] via DSL.
 */
fun letterMarkupWithDataUsage(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsageV2.Property> = emptySet(),
    build: LetterMarkupBuilder.() -> Unit,
): LetterMarkupWithDataUsageV2 = LetterMarkupWithDataUsageV2(
    markup = letterMarkup(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)
