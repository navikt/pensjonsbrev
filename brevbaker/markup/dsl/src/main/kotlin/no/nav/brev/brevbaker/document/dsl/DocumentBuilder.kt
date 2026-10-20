@file:Suppress("DEPRECATION")

package no.nav.brev.brevbaker.document.dsl

import no.nav.brev.brevbaker.document.Document
import no.nav.brev.brevbaker.document.DocumentModel
import no.nav.brev.brevbaker.document.DocumentMottaker
import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.DocumentSaksinformasjon
import no.nav.brev.brevbaker.document.DocumentSaksnummer
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import java.time.LocalDate

private const val MAKS_LENGDE_TITTEL = 300
private const val MAKS_LENGDE_NAVN = 747
private const val MAKS_LENGDE_PERSONIDENTIFIKATOR = 11
private const val MAKS_LENGDE_SAKSNUMMER = 20

private fun requireMaksLengde(felt: String, verdi: String?, maksLengde: Int) {
    require(verdi == null || verdi.length <= maksLengde) {
        "$felt kan ikke være lengre enn $maksLengde tegn, men var ${verdi?.length}"
    }
}

fun document(
    tittel: String,
    visTittel: Boolean = true,
    visLogo: Boolean = true,
    gjelderNavn: String? = null,
    gjelderPersonidentifikator: String? = null,
    annenMottakerNavn: String? = null,
    saksnummer: DocumentSaksnummer? = null,
    dokumentDato: LocalDate? = null,
    build: OutlineBuilder<ContentBuilder>.() -> Unit,
): Document {
    require(tittel.isNotBlank()) { "Dokumentet må ha en tittel" }
    requireMaksLengde("tittel", tittel, MAKS_LENGDE_TITTEL)
    requireMaksLengde("gjelderNavn", gjelderNavn, MAKS_LENGDE_NAVN)
    requireMaksLengde("gjelderPersonidentifikator", gjelderPersonidentifikator, MAKS_LENGDE_PERSONIDENTIFIKATOR)
    requireMaksLengde("annenMottakerNavn", annenMottakerNavn, MAKS_LENGDE_NAVN)
    val blocks = OutlineBuilder(::ContentBuilder).apply(build).build()
    require(blocks.isNotEmpty()) { "Dokumentet må ha innhold" }
    return DocumentModel.document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        gjelderNavn = gjelderNavn,
        gjelderPersonidentifikator = gjelderPersonidentifikator?.let { Markup.Personidentifikator(it) },
        annenMottakerNavn = annenMottakerNavn,
        saksnummer = saksnummer,
        dokumentDato = dokumentDato,
        blocks = blocks,
    )
}

fun documentSaksnummer(
    saksnummer: String,
    visFooter: Boolean = false,
): DocumentSaksnummer {
    requireMaksLengde("saksnummer", saksnummer, MAKS_LENGDE_SAKSNUMMER)
    return DocumentModel.documentSaksnummer(
        saksnummer = Markup.Saksnummer(saksnummer),
        visFooter = visFooter,
    )
}

@Deprecated("Bruk document med gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn og saksnummer")
fun document(
    tittel: String,
    visTittel: Boolean = true,
    visLogo: Boolean = true,
    saksinformasjon: DocumentSaksinformasjon?,
    dokumentDato: LocalDate? = null,
    build: OutlineBuilder<ContentBuilder>.() -> Unit,
): Document {
    require(tittel.isNotBlank()) { "Dokumentet må ha en tittel" }
    val blocks = OutlineBuilder(::ContentBuilder).apply(build).build()
    require(blocks.isNotEmpty()) { "Dokumentet må ha innhold" }
    return DocumentModel.document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        saksinformasjon = saksinformasjon,
        dokumentDato = dokumentDato,
        blocks = blocks,
    )
}

@Deprecated("Bruk documentSaksnummer og feltene direkte på document")
fun documentSaksinformasjon(
    saksnummer: String,
    visFooter: Boolean = false,
    mottaker: DocumentMottaker? = null,
): DocumentSaksinformasjon = DocumentModel.documentSaksinformasjon(
    saksnummer = Markup.Saksnummer(saksnummer),
    visFooter = visFooter,
    mottaker = mottaker,
)

@Deprecated("Bruk gjelderNavn, gjelderPersonidentifikator og annenMottakerNavn direkte på document")
fun documentMottaker(
    gjelderNavn: String,
    gjelderPersonidentifikator: String,
    annenMottakerNavn: String? = null,
): DocumentMottaker = DocumentModel.documentMottaker(
    gjelderNavn = gjelderNavn,
    gjelderPersonidentifikator = Markup.Personidentifikator(gjelderPersonidentifikator),
    annenMottakerNavn = annenMottakerNavn,
)

fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
    DocumentModel.documentPDFRequest(document = document, spraak = spraak)
