package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.Foedselsnummer
import no.nav.brev.brevbaker.markup.LetterMarkup
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
 * DSL som bygger opp innholdet til ett brevbaker brev.
 *
 * [saksinformasjon] og [signatur] er obligatoriske og angis som argumenter (bygg dem med
 * [saksinformasjon]- og [signatur]-fabrikkfunksjonene).
 *
 * ```
 * val brev = letterMarkup(
 *     saksinformasjon = saksinformasjon(gjelderNavn = "Ola", gjelderFoedselsnummer = "12345678901",
 *         saksnummer = "9876543", dokumentDato = LocalDate.now()),
 *     signatur = signatur(hilsenTekst = "Med vennlig hilsen", navAvsenderEnhet = "Nav"),
 * ) {
 *     title1("Vedtak")
 *     outline { paragraph("Du får innvilget søknaden.") }
 * }
 * ```
 */
fun letterMarkup(
    saksinformasjon: Saksinformasjon,
    signatur: Signatur,
    build: LetterMarkupBuilder<ContentBuilder>.() -> Unit,
): LetterMarkup =
    LetterMarkupBuilder(::ContentBuilder, saksinformasjon, signatur).apply(build).build()

/**
 * Bygg [Saksinformasjon] (saksinformasjon og mottaker) for et brev.
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
): Saksinformasjon = Saksinformasjon(
    gjelderNavn = gjelderNavn,
    gjelderFoedselsnummer = Foedselsnummer(gjelderFoedselsnummer),
    annenMottakerNavn = annenMottakerNavn,
    saksnummer = Saksnummer(saksnummer),
    dokumentDato = dokumentDato,
)

/**
 * Bygg [Signatur] (hilsen og avsender) for et brev. Angi
 * [saksbehandlerNavn]/[attesterendeSaksbehandlerNavn] når brevet er signert av saksbehandler(e).
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
): Signatur = Signatur(
    hilsenTekst = hilsenTekst,
    saksbehandlerSignatur = saksbehandlerNavn?.let {
        SaksbehandlerSignatur(it, attesterendeSaksbehandlerNavn)
    },
    navAvsenderEnhet = navAvsenderEnhet,
)

/**
 * Bygg et [Attachment] (brevvedlegg) via DSL. [inkluderSaksinformasjon] styrer om saksinformasjonen
 * gjentas i vedlegget.
 *
 * ```
 * val vedlegg = attachment(inkluderSaksinformasjon = true) {
 *     title1("Vedlegg 1")
 *     outline { paragraph("Vedleggsinnhold.") }
 * }
 * ```
 */
fun attachment(inkluderSaksinformasjon: Boolean = false, build: AttachmentBuilder<ContentBuilder>.() -> Unit): Attachment =
    AttachmentBuilder(::ContentBuilder, inkluderSaksinformasjon).apply(build).build()

/**
 * Bygg en frittstående [PDFTittel] via DSL.
 *
 * ```
 * val tittel = pdfTittel { text("Vedtak om uføretrygd") }
 * ```
 */
fun pdfTittel(content: ContentBuilder.() -> Unit): PDFTittel =
    PDFTittel(ContentBuilder().apply(content).build())

@MarkupDsl
class LetterMarkupBuilder<C : AbstractContentBuilder> internal constructor(
    private val contentFactory: ContentFactory<C>,
    private val saksinformasjon: Saksinformasjon,
    private val signatur: Signatur,
) {
    private var title1: List<Text> = emptyList()
    private var blocks: List<Block> = emptyList()

    internal fun setTitle(content: () -> List<Text>) {
        title1 = content()
    }

    /**
     * Bygg innholdet i brevet.
     *
     * ```
     * outline {
     *     title2("Innledning")
     *     paragraph("Første avsnitt.")
     * }
     * ```
     */
    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(contentFactory).apply(build).build()
    }

    internal fun build(): LetterMarkup = LetterMarkup(
        title1 = title1,
        saksinformasjon = saksinformasjon,
        blocks = blocks,
        signatur = signatur,
    )
}

@MarkupDsl
class AttachmentBuilder<C : AbstractContentBuilder> internal constructor(
    private val contentFactory: ContentFactory<C>,
    private val inkluderSaksinformasjon: Boolean,
) {
    private var title1: List<Text> = emptyList()
    private var blocks: List<Block> = emptyList()

    /**
     * Sett vedleggets tittel via DSL-blokk.
     *
     * ```
     * title1 { text("Vedlegg ") }
     * ```
     */
    fun title1(content: C.() -> Unit) {
        title1 = contentFactory.content(content)
    }

    /**
     * Bygg vedleggets brødtekst. Syntax for innhold er likt som i brevet.
     *
     * ```
     * outline { paragraph("Du får....") }
     * ```
     */
    fun outline(build: OutlineBuilder<C>.() -> Unit) {
        blocks = OutlineBuilder(contentFactory).apply(build).build()
    }

    internal fun build(): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)
}

/**
 * Sett brevets hoved-tittel som en string.
 *
 * ```
 * title1("Vedtak om uføretrygd")
 * ```
 */
fun LetterMarkupBuilder<ContentBuilder>.title1(text: String) = setTitle { plainText(text) }

/**
 * Setter brevets hoved-tittel som plaintext via DSL-blokk.
 *
 * ```
 * title1 { text("Vedtak om "); text("uføretrygd") }
 * ```
 */
@JvmName("title1WithPlainTextBuilder")
fun LetterMarkupBuilder<ContentBuilder>.title1(content: PlainTextBuilder.() -> Unit) =
    setTitle { plainText(content) }

/**
 * Sett vedleggets tittel som ren tekst.
 *
 * ```
 * title1("Vedlegg 1")
 * ```
 */
fun AttachmentBuilder<ContentBuilder>.title1(text: String) {
    title1 { this.text(text) }
}
