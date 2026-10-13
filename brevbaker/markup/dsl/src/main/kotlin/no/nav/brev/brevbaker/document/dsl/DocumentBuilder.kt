package no.nav.brev.brevbaker.document.dsl

import no.nav.brev.brevbaker.document.Document
import no.nav.brev.brevbaker.document.DocumentModel
import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.DocumentSaksnummer
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.ContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import java.time.LocalDate

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
): DocumentSaksnummer = DocumentModel.documentSaksnummer(
    saksnummer = Markup.Saksnummer(saksnummer),
    visFooter = visFooter,
)

fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
    DocumentModel.documentPDFRequest(document = document, spraak = spraak)
