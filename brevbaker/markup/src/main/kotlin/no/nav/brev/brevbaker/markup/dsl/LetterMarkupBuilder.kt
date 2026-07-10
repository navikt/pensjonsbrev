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
 * Bygg en [LetterMarkup] med grunnleggende DSL.
 *
 * Denne varianten støtter vanlig tekst, men ikke `variable(...)`.
 * Bruk [letterMarkupExtended] når du trenger utvidet tekststøtte.
 *
 * ```
 * val brev = letterMarkup {
 *     title1("Vedtak")
 *     saksinformasjon(gjelderNavn = "Ola", gjelderFoedselsnummer = "12345678901",
 *         saksnummer = "9876543", dokumentDato = LocalDate.now())
 *     outline { paragraph("Du får innvilget søknaden.") }
 *     signatur(hilsenTekst = "Med vennlig hilsen", navAvsenderEnhet = "Nav")
 * }
 * ```
 */
fun letterMarkup(build: LetterMarkupBuilder<ContentBuilder>.() -> Unit): LetterMarkup =
    LetterMarkupBuilder(IdGenerator(), ::ContentBuilder).apply(build).build()

/**
 * Bygg en [LetterMarkup] med utvidet DSL.
 *
 * Denne varianten støtter `variable(...)` i tekstinnhold, og metadata som tags på variabler.
 *
 * ```
 * val brev = letterMarkupExtended {
 *     outline { paragraph { text("Du får "); variable("uføretrygd") } }
 *     // ... saksinformasjon, signatur
 * }
 * ```
 */
fun letterMarkupExtended(build: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit): LetterMarkup =
    LetterMarkupBuilder(IdGenerator(), ::ExtendedContentBuilder).apply(build).build()

/**
 * Bygg en gjenbrukbar outline (liste av [Block]) frittstående fra et brev.
 *
 * ```
 * val blocks = outline {
 *     title2("Innledning")
 *     paragraph("Dette er et avsnitt.")
 * }
 * ```
 */
fun outline(build: OutlineBuilder<ContentBuilder>.() -> Unit): List<Block> =
    OutlineBuilder(IdGenerator(), ::ContentBuilder).apply(build).build()

/**
 * Som [outline], men med utvidet tekststøtte (`variable(...)`).
 *
 * ```
 * val blocks = outlineExtended { paragraph { text("Beløp: "); variable("1000 kr") } }
 * ```
 */
fun outlineExtended(build: OutlineBuilder<ExtendedContentBuilder>.() -> Unit): List<Block> =
    OutlineBuilder(IdGenerator(), ::ExtendedContentBuilder).apply(build).build()

/**
 * Bygg et [Attachment] (brevvedlegg) via DSL. [inkluderSaksinformasjon] styrer om saksinformasjonen
 * gjentas i vedlegget.
 *
 * Parametere:
 * - `inkluderSaksinformasjon`: `true` for å vise saksinformasjon i vedlegget, `false` for å skjule den
 * - `build`: innholdet i vedlegget (tittel + outline)
 *
 * ```
 * val vedlegg = attachment(inkluderSaksinformasjon = true) {
 *     title1("Vedlegg 1")
 *     outline { paragraph("Vedleggsinnhold.") }
 * }
 * ```
 */
fun attachment(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), ::ContentBuilder, inkluderSaksinformasjon).apply(build).build()

/**
 * Som [attachment], men med utvidet tekststøtte (`variable(...)`).
 *
 * ```
 * val vedlegg = attachmentExtended(inkluderSaksinformasjon = false) {
 *     outline { paragraph { text("Sats: "); variable("2G") } }
 * }
 * ```
 */
fun attachmentExtended(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ExtendedContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(IdGenerator(), ::ExtendedContentBuilder, inkluderSaksinformasjon).apply(build).build()

/**
 * Bygg en frittstående [PDFTittel] via DSL.
 *
 * ```
 * val tittel = pdfTittel { text("Vedtak om uføretrygd") }
 * ```
 */
fun pdfTittel(content: ContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(IdGenerator().content(::ContentBuilder, content))

/**
 * Som [pdfTittel], men med utvidet tekststøtte (`variable(...)`).
 *
 * ```
 * val tittel = pdfTittelExtended { text("Vedtak for "); variable("navn") }
 * ```
 */
fun pdfTittelExtended(content: ExtendedContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(IdGenerator().content(::ExtendedContentBuilder, content))

/**
 * Lag en [LetterMarkupWithDataUsage.Property] – ett datafelt (type + property) brevet leser fra.
 *
 * Parametere:
 * - `typeName`: navnet på datatypen (for eksempel `UngUfoerDto`)
 * - `propertyName`: navnet på feltet i typen
 *
 * ```
 * val prop = dataUsageProperty(typeName = "UngUfoerDto", propertyName = "totaltUfoerePerMnd")
 * ```
 */
fun dataUsageProperty(typeName: String, propertyName: String): LetterMarkupWithDataUsage.Property =
    LetterMarkupWithDataUsage.Property(typeName, propertyName)

/**
 * Bygg et [LetterMarkupWithDataUsage] – et [LetterMarkup] beriket med [brevtype] og hvilke datafelter
 * brevet bruker ([letterDataUsage]).
 *
 * Parametere:
 * - `brevtype`: hvilken brevtype markuppen tilhører
 * - `letterDataUsage`: sett av datafelter brevet bruker (kan stå tomt)
 * - `build`: selve brevinnholdet
 *
 * ```
 * val brev = letterMarkupWithDataUsage(
 *     brevtype = Brevtype.VEDTAKSBREV,
 *     letterDataUsage = setOf(dataUsageProperty("UngUfoerDto", "belop")),
 * ) {
 *     saksinformasjon(/* ... */); outline { paragraph("...") }; signatur(/* ... */)
 * }
 * ```
 */
fun letterMarkupWithDataUsage(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = letterMarkup(build),
    letterDataUsage = letterDataUsage,
    brevtype = brevtype,
)

/**
 * Som [letterMarkupWithDataUsage], men med utvidet tekststøtte (`variable(...)`).
 *
 * Parametere:
 * - `brevtype`: hvilken brevtype markuppen tilhører
 * - `letterDataUsage`: sett av datafelter brevet bruker (kan stå tomt)
 * - `build`: selve brevinnholdet
 *
 * ```
 * val brev = letterMarkupWithDataUsageExtended(brevtype = Brevtype.INFORMASJONSBREV) {
 *     outline { paragraph { text("Beløp: "); variable("1000 kr") } }
 * }
 * ```
 */
fun letterMarkupWithDataUsageExtended(
    brevtype: Brevtype,
    letterDataUsage: Set<LetterMarkupWithDataUsage.Property> = emptySet(),
    build: LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit,
): LetterMarkupWithDataUsage = LetterMarkupWithDataUsage(
    markup = letterMarkupExtended(build),
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

    /**
     * Sett saksinformasjon og mottaker for brevet.
     *
     * Parametere:
     * - `gjelderNavn`: navn på personen brevet gjelder
     * - `gjelderFoedselsnummer`: fødselsnummeret til personen brevet gjelder
     * - `saksnummer`: saksnummer for brevet
     * - `dokumentDato`: dokumentdato som vises i brevet
     * - `annenMottakerNavn`: valgfritt navn på annen mottaker (f.eks verge/fullmektig)
     *
     * ```
     * saksinformasjon(gjelderNavn = "Ola Nordmann", gjelderFoedselsnummer = "12345678901",
     *     saksnummer = "9876543", dokumentDato = LocalDate.now())
     * ```
     */
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

    /**
     * Bygg innholdet i brevet.
     *
     * Parametere:
     * - `build`: blokker som `title2`, `paragraph`, `itemList`, `table`, `formText` og `formChoice`
     *
     * ```
     * outline {
     *     title2("Innledning")
     *     paragraph("Første avsnitt.")
     *     ...
     * }
     * ```
     */
    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(ids, contentFactory).apply(build).build()
    }

    /**
     * Sett hilsen og avsender. Angi [saksbehandlerNavn]/[attesterendeSaksbehandlerNavn] når brevet er
     * signert av saksbehandler(e).
     *
     * Parametere:
     * - `hilsenTekst`: teksten i hilsenen
     * - `navAvsenderEnhet`: avsenderenhet som vises i signaturen
     * - `saksbehandlerNavn`: valgfritt navn på saksbehandler
     * - `attesterendeSaksbehandlerNavn`: valgfritt navn på attesterende saksbehandler
     *
     * ```
     * signatur(hilsenTekst = "Med vennlig hilsen", navAvsenderEnhet = "Nav Familie- og pensjonsytelser",
     *     saksbehandlerNavn = "Kari Saksbehandler")
     * ```
     */
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

    /**
     * Sett vedleggets tittel via DSL-blokk.
     * Se også shorthand-varianten `title1("...")`.
     *
     * Parametere:
     * - `content`: tittelinnhold (tekst, og i utvidet DSL også `variable(...)`)
     *
     * ```
     * title1 { text("Vedlegg "); variable("nr") }
     * ```
     */
    fun title1(content: C.() -> Unit) {
        title1 = ids.content(contentFactory, content)
    }

    /**
     * Bygg vedleggets brødtekst.
     *
     * Syntax for innhold er likt som i brevet.
     *
     * Parametere:
     * - blokker som `title2/3/4`, `paragraph`, `itemList/numberedList`, `table`, `formText` og `formChoice`
     *
     * ```
     * outline { paragraph("Du får....") }
     * ```
     */
    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(ids, contentFactory).apply(build).build()
    }

    internal fun build(): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)
}

/**
 * Sett brevets hoved-tittel som en string.
 * Har også en DSL-variant `title1 { text("...") }`.
 *
 * ```
 * title1("Vedtak om uføretrygd")
 * ```
 */
fun <C : AbstractContentBuilder> LetterMarkupBuilder<C>.title1(text: String) = setTitle { plainText(text) }

/**
 * Setter brevets hoved-tittel som plaintext
 * Se også kort-varianten `title1("...")`.
 *
 * ```
 * title1 { text("Vedtak om "); text("uføretrygd") }
 * ```
 */
@JvmName("title1WithPlainTextBuilder")
fun LetterMarkupBuilder<ContentBuilder>.title1(content: PlainTextBuilder.() -> Unit) =
    setTitle { plainText(content) }

/**
 *  Setter brevets hoved-tittel via dsl. Støtter også variable:
 * Se også shorthand-varianten `title1("...")`.
 *
 * ```
 * title1 { text("Vedtak for "); variable("navn") }
 * ```
 */
fun LetterMarkupBuilder<ExtendedContentBuilder>.title1(content: PlainExtendedTextBuilder.() -> Unit) =
    setTitle { plainExtendedText(content) }

/**
 * Sett vedleggets tittel som ren tekst.
 * Se også DSL-varianten `title1 { text("...") }`.
 *
 * ```
 * title1("Vedlegg 1")
 * ```
 */
fun <C : AbstractContentBuilder> AttachmentBuilder<C>.title1(text: String) {
    title1 { this.text(text) }
}
