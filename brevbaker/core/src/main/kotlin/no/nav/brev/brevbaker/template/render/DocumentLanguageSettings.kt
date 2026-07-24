package no.nav.brev.brevbaker.template.render

import no.nav.pensjon.brev.template.Language

internal class DocumentLanguageSettings(private val language: Language) {
    val navnPrefix = localized("Navn:", "Namn:", "Name:")
    val gjelderNavnPrefix = localized("Saken gjelder:", "Saka gjeld:", "Case regarding:")
    val annenMottakerPrefix = localized("Mottaker:", "Mottakar:", "Recipient:")
    val saksnummerPrefix = localized("Saksnummer:", "Saksnummer:", "Case number:")
    val foedselsnummerPrefix = localized("Fødselsnummer:", "Fødselsnummer:", "National identity number:")
    val closingGreeting = localized("Med vennlig hilsen", "Med vennleg helsing", "Sincerely")
    val automatiskInformasjonsbrev = localized(
        "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
        "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
        "This letter has been processed automatically and is therefore not signed by an assessor.",
    )
    val automatiskVedtaksbrev = localized(
        "Saken har blitt automatisk saksbehandlet. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
        "Saken har blitt automatisk saksbehandla. Vedtaksbrevet er derfor ikkje underskriven av saksbehandlar.",
        "Your case has been processed automatically. The decision letter has therefore not been signed by an assessor.",
    )
    val altTextLogo = localized("Nav logo", "Nav logo", "Nav logo")

    private fun localized(bokmal: String, nynorsk: String, english: String): String =
        when (language) {
            Language.Bokmal -> bokmal
            Language.Nynorsk -> nynorsk
            Language.English -> english
        }
}
