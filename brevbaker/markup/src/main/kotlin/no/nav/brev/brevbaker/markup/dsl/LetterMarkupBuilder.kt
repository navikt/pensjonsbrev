package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.Brevtype
import no.nav.brev.brevbaker.markup.Foedselsnummer
import no.nav.brev.brevbaker.markup.LetterMarkupV2
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsageV2
import no.nav.brev.brevbaker.markup.PDFTittelV2
import no.nav.brev.brevbaker.markup.SaksbehandlerSignatur
import no.nav.brev.brevbaker.markup.Saksinformasjon
import no.nav.brev.brevbaker.markup.Saksnummer
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

/**
 * Bygg en [LetterMarkupV2] via DSL. `variable` er ikke tilgjengelig her; bruk [letterMarkupWithVariables]
 * dersom du (som brevbaker/skribenten) trenger variabler.
 */
fun letterMarkup(build: LetterMarkupBuilder<ContentBuilder>.() -> Unit): LetterMarkupV2 =
    LetterMarkupBuilder(IdGenerator(), ::ContentBuilder).apply(build).build()

/** Som [letterMarkup], men med `variable` tilgjengelig i tekstinnholdet. */
fun letterMarkupWithVariables(build: LetterMarkupBuilder<VariableContentBuilder>.() -> Unit): LetterMarkupV2 =
    LetterMarkupBuilder(IdGenerator(), ::VariableContentBuilder).apply(build).build()

/** Bygg en gjenbrukbar outline (liste av [Block]). */
fun outline(build: OutlineBuilder<ContentBuilder>.() -> Unit): List<Block> =
    OutlineBuilder(IdGenerator(), ::ContentBuilder).apply(build).build()

/** Som [outline], men med `variable` tilgjengelig. */
fun outlineWithVariables(build: OutlineBuilder<VariableContentBuilder>.() -> Unit): List<Block> =
    OutlineBuilder(IdGenerator(), ::VariableContentBuilder).apply(build).build()

/** Bygg et [Attachment] via DSL. */
fun attachment(inkluderSaksinformasjon: Boolean, build: AttachmentBuilder<ContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), ::ContentBuilder, inkluderSaksinformasjon).apply(build).build()

/** Som [attachment], men med `variable` tilgjengelig. */
fun attachmentWithVariables(inkluderSaksinformasjon: Boolean, build: AttachmentBuilder<VariableContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), ::VariableContentBuilder, inkluderSaksinformasjon).apply(build).build()

/** Bygg en [PDFTittelV2] via DSL. */
fun pdfTittel(content: ContentBuilder.() -> Unit): PDFTittelV2 =
    PDFTittelV2(IdGenerator().content(::ContentBuilder, content))

/** Som [pdfTittel], men med `variable` tilgjengelig. */
fun pdfTittelWithVariables(content: VariableContentBuilder.() -> Unit): PDFTittelV2 =
    PDFTittelV2(IdGenerator().content(::VariableContentBuilder, content))

/** Bygg en [LetterMarkupWithDataUsageV2.Property] via DSL. */
fun dataUsageProperty(typeName: String, propertyName: String): LetterMarkupWithDataUsageV2.Property =
    LetterMarkupWithDataUsageV2.Property(typeName, propertyName)

/** Bygg en [LetterMarkupWithDataUsageV2] via DSL. */
fun letterMarkupWithDataUsage(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsageV2.Property> = emptySet(),
    build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsageV2 = LetterMarkupWithDataUsageV2(
    markup = letterMarkup(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)

/** Som [letterMarkupWithDataUsage], men med `variable` tilgjengelig. */
fun letterMarkupWithDataUsageWithVariables(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsageV2.Property> = emptySet(),
    build: LetterMarkupBuilder<VariableContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsageV2 = LetterMarkupWithDataUsageV2(
    markup = letterMarkupWithVariables(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)

@MarkupDsl
class LetterMarkupBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private var title1: List<Text> = emptyList()
    private var saksinformasjon: Saksinformasjon? = null
    private var blocks: List<Block> = emptyList()
    private var signatur: Signatur? = null

    fun title(content: C.() -> Unit) {
        title1 = ids.content(contentFactory, content)
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

    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(ids, contentFactory).apply(build).build()
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
class AttachmentBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
    private val inkluderSaksinformasjon: Boolean,
) {
    private var title1: List<Text> = emptyList()
    private var blocks: List<Block> = emptyList()

    fun title(content: C.() -> Unit) {
        title1 = ids.content(contentFactory, content)
    }

    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(ids, contentFactory).apply(build).build()
    }

    internal fun build(): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)
}
