package no.nav.brev.brevbaker.document.dsl

import no.nav.brev.brevbaker.document.Document
import no.nav.brev.brevbaker.document.DocumentModel
import no.nav.brev.brevbaker.document.DocumentMottaker
import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.DocumentSaksinformasjon
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import java.time.LocalDate

fun document(
    tittel: String,
    visTittel: Boolean = true,
    visLogo: Boolean = true,
    saksinformasjon: DocumentSaksinformasjon? = null,
    dokumentDato: LocalDate? = null,
    build: OutlineBuilder<ContentBuilder>.() -> Unit,
): Document {
    require(tittel.isNotBlank()) { "Dokumentet må ha en tittel" }
    return DocumentModel.document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        saksinformasjon = saksinformasjon,
        dokumentDato = dokumentDato,
        blocks = OutlineBuilder(::ContentBuilder).apply(build).build(),
    )
}

fun documentSaksinformasjon(
    saksnummer: String,
    visFooter: Boolean = false,
    mottaker: DocumentMottaker? = null,
): DocumentSaksinformasjon = DocumentModel.documentSaksinformasjon(
    saksnummer = saksnummer,
    visFooter = visFooter,
    mottaker = mottaker,
)

fun documentMottaker(
    gjelderNavn: String,
    gjelderPersonidentifikator: String,
    annenMottakerNavn: String? = null,
): DocumentMottaker = DocumentModel.documentMottaker(
    gjelderNavn = gjelderNavn,
    gjelderPersonidentifikator = gjelderPersonidentifikator,
    annenMottakerNavn = annenMottakerNavn,
)

fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
    DocumentModel.documentPDFRequest(document = document, spraak = spraak)
