package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brevbaker.api.model.LanguageCode

internal class DocumentLanguageSettings(private val language: LanguageCode) {
    val navnPrefix = localized("Navn:", "Namn:", "Name:")
    val annenMottakerPrefix = localized("Mottaker:", "Mottakar:", "Recipient:")
    val vedleggGjelderNavnPrefix = localized("Vedlegget gjelder:", "Vedlegget gjeld:", "Attachment regarding:")
    val gjelderNavnPrefix = localized("Saken gjelder:", "Saka gjeld:", "Case regarding:")
    val saksnummerPrefix = localized("Saksnummer:", "Saksnummer:", "Case number:")
    val foedselsnummerPrefix = localized("Fødselsnummer:", "Fødselsnummer:", "National identity number:")
    val sideSaksnummerPrefix = localized("Saksnummer: ", "Saksnummer: ", "Case number: ")
    val sidePrefix = localized("side", "side", "page")
    val sideInfix = localized("av", "av", "of")
    val closingGreeting = localized("Med vennlig hilsen", "Med vennleg helsing", "Sincerely")
    val closingAutomatiskInformasjonsbrev = localized(
        "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
        "Brevet er produsert automatisk og er derfor ikkje underskrive av saksbehandlar.",
        "This letter has been processed automatically and is therefore not signed by an assessor.",
    )
    val closingAutomatiskVedtaksbrev = localized(
        "Saken har blitt automatisk saksbehandlet. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
        "Saken har blitt automatisk saksbehandla. Vedtaksbrevet er derfor ikkje underskrive av saksbehandlar.",
        "Your case has been processed automatically. The decision letter has therefore not been signed by an assessor.",
    )
    val closingVedleggPrefix = localized("Vedlegg:", "Vedlegg:", "Attachments:")
    val tableNextPageContinuation = localized(
        "Tabellen fortsetter på neste side",
        "Tabellen fortsett på neste side",
        "Continued on next page",
    )
    val tableContinuedFromPreviousPage = localized(
        "Fortsettelse fra forrige side",
        "Fortsetjing frå førre side",
        "Continuation from previous page",
    )

    fun asMap(): Map<String, String> = mapOf(
        "navnprefix" to navnPrefix,
        "annenmottakerprefix" to annenMottakerPrefix,
        "vedlegggjeldernavnprefix" to vedleggGjelderNavnPrefix,
        "gjeldernavnprefix" to gjelderNavnPrefix,
        "saksnummerprefix" to saksnummerPrefix,
        "foedselsnummerprefix" to foedselsnummerPrefix,
        "sidesaksnummerprefix" to sideSaksnummerPrefix,
        "sideprefix" to sidePrefix,
        "sideinfix" to sideInfix,
        "closinggreeting" to closingGreeting,
        "closingautomatisktextinfobrev" to closingAutomatiskInformasjonsbrev,
        "closingautomatisktextvedtaksbrev" to closingAutomatiskVedtaksbrev,
        "closingvedleggprefix" to closingVedleggPrefix,
        "tablenextpagecontinuation" to tableNextPageContinuation,
        "tablecontinuedfrompreviouspage" to tableContinuedFromPreviousPage,
    )

    private fun localized(bokmal: String, nynorsk: String, english: String): String =
        when (language) {
            LanguageCode.BOKMAL -> bokmal
            LanguageCode.NYNORSK -> nynorsk
            LanguageCode.ENGLISH -> english
        }
}
