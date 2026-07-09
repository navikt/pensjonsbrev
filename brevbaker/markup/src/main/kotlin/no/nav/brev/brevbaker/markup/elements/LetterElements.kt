package no.nav.brev.brevbaker.markup.elements

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

/** Bygg en frittstående [Saksinformasjon]. */
fun saksinformasjonElement(
    gjelderNavn: String = "",
    gjelderFoedselsnummer: String = "",
    saksnummer: String = "",
    dokumentDato: LocalDate = LocalDate.EPOCH,
    annenMottakerNavn: String? = null,
): Saksinformasjon = Saksinformasjon(
    gjelderNavn = gjelderNavn,
    gjelderFoedselsnummer = Foedselsnummer(gjelderFoedselsnummer),
    annenMottakerNavn = annenMottakerNavn,
    saksnummer = Saksnummer(saksnummer),
    dokumentDato = dokumentDato,
)

/** Bygg en frittstående [SaksbehandlerSignatur]. */
fun saksbehandlerSignaturElement(
    saksbehandlerNavn: String = "",
    attesterendeSaksbehandlerNavn: String? = null,
): SaksbehandlerSignatur = SaksbehandlerSignatur(saksbehandlerNavn, attesterendeSaksbehandlerNavn)

/** Bygg en frittstående [Signatur]. */
fun signaturElement(
    hilsenTekst: String = "",
    navAvsenderEnhet: String = "",
    saksbehandlerSignatur: SaksbehandlerSignatur? = null,
): Signatur = Signatur(hilsenTekst, saksbehandlerSignatur, navAvsenderEnhet)

/** Bygg en frittstående [PDFTittelV2]. */
fun pdfTittelElement(title1: List<Text> = emptyList()): PDFTittelV2 = PDFTittelV2(title1)

/** Bygg et frittstående [Attachment]. */
fun attachmentElement(
    inkluderSaksinformasjon: Boolean = false,
    title1: List<Text> = emptyList(),
    blocks: List<Block> = emptyList(),
): Attachment = Attachment(title1, blocks, inkluderSaksinformasjon)

/** Bygg en frittstående [LetterMarkupV2]. */
fun letterMarkupElement(
    saksinformasjon: Saksinformasjon = saksinformasjonElement(),
    signatur: Signatur = signaturElement(),
    title1: List<Text> = emptyList(),
    blocks: List<Block> = emptyList(),
    version: Int = LetterMarkupV2.VERSION,
): LetterMarkupV2 = LetterMarkupV2(title1, saksinformasjon, blocks, signatur, version)

/** Bygg en frittstående [LetterMarkupWithDataUsageV2.Property]. */
fun dataUsagePropertyElement(typeName: String, propertyName: String): LetterMarkupWithDataUsageV2.Property =
    LetterMarkupWithDataUsageV2.Property(typeName, propertyName)

/** Bygg en frittstående [LetterMarkupWithDataUsageV2]. */
fun letterMarkupWithDataUsageElement(
    markup: LetterMarkupV2 = letterMarkupElement(),
    brevtype: Brevtype = Brevtype.INFORMASJONSBREV,
    letterDataUsage: Set<LetterMarkupWithDataUsageV2.Property> = emptySet(),
): LetterMarkupWithDataUsageV2 = LetterMarkupWithDataUsageV2(markup, letterDataUsage, brevtype)
