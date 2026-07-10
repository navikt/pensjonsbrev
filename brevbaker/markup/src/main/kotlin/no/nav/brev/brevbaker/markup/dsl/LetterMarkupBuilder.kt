package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.Brevtype
import no.nav.brev.brevbaker.markup.Foedselsnummer
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.SaksbehandlerSignatur
import no.nav.brev.brevbaker.markup.Saksinformasjon
import no.nav.brev.brevbaker.markup.Saksnummer
import no.nav.brev.brevbaker.markup.Signatur
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate
import kotlin.jvm.JvmName

/**
 * Bygg en [LetterMarkup] via DSL. `variable` er ikke tilgjengelig her; bruk [letterMarkupWithVariables]
 * dersom du (som brevbaker/skribenten) trenger variabler.
 */
fun letterMarkup(build: LetterMarkupBuilder<ContentBuilder>.() -> Unit): LetterMarkup =
    LetterMarkupBuilder(IdGenerator(), ::ContentBuilder).apply(build).build()

/** Som [letterMarkup], men med `variable` tilgjengelig i tekstinnholdet. */
fun letterMarkupWithVariables(build: LetterMarkupBuilder<VariableContentBuilder>.() -> Unit): LetterMarkup =
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

/** Bygg en [PDFTittel] via DSL. */
fun pdfTittel(content: ContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(IdGenerator().content(::ContentBuilder, content))

/** Som [pdfTittel], men med `variable` tilgjengelig. */
fun pdfTittelWithVariables(content: VariableContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(IdGenerator().content(::VariableContentBuilder, content))

/** Bygg en [LetterMarkupWithDataUsage.Property] via DSL. */
fun dataUsageProperty(typeName: String, propertyName: String): LetterMarkupWithDataUsage.Property =
    LetterMarkupWithDataUsage.Property(typeName, propertyName)

/** Bygg en [LetterMarkupWithDataUsage] via DSL. */
fun letterMarkupWithDataUsage(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = letterMarkup(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)

/** Som [letterMarkupWithDataUsage], men med `variable` tilgjengelig. */
fun letterMarkupWithDataUsageWithVariables(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<VariableContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
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

    internal fun setTitle(content: IdGenerator.() -> List<Text>) {
        title1 = ids.content()
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

    internal fun build(): LetterMarkup = LetterMarkup(
        title1 = title1,
        saksinformasjon = requireNotNull(saksinformasjon) { "LetterMarkup must have saksinformasjon" },
        blocks = blocks,
        signatur = requireNotNull(signatur) { "LetterMarkup must have signatur" },
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

    fun title1(content: C.() -> Unit) {
        title1 = ids.content(contentFactory, content)
    }

    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(ids, contentFactory).apply(build).build()
    }

    internal fun build(): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)
}

/** Tittel som ren tekst (gjelder både [letterMarkup] og [letterMarkupWithVariables]). */
fun <C : AbstractContentBuilder> LetterMarkupBuilder<C>.title1(text: String) = setTitle { plainText(text) }

/** Tittel via builder uten `variable` ([letterMarkup]). */
@JvmName("title1WithPlainTextBuilder")
fun LetterMarkupBuilder<ContentBuilder>.title1(content: PlainTextBuilder.() -> Unit) =
    setTitle { plainText(content) }

/** Tittel som ren tekst med `variable` ([letterMarkupWithVariables]). */
fun LetterMarkupBuilder<VariableContentBuilder>.title1(content: PlainVariableTextBuilder.() -> Unit) =
    setTitle { plainVariableText(content) }

/** Vedleggstittel som ren tekst (gjelder både [attachment] og [attachmentWithVariables]). */
fun <C : AbstractContentBuilder> AttachmentBuilder<C>.title1(text: String) {
    title1 { this.text(text) }
}
