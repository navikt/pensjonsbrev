package no.nav.pensjon.brev.skribenten.letter

// Sjekker om brevet er klar til sending. Foreløpig er det kun at alle fritekst felter er redigert som sjekkes,
// om det legges til flere valideringer så må feilmeldingen i BrevredigeringsService.validerErKlarTilSending endres.
fun Edit.Letter.klarTilSending(): Boolean =
    literals.all { it.tags?.contains("FRITEKST") != true || it.editedText != null }

fun Edit.Letter.variablesValueMap(): Map<Int, String> =
    variables.mapNotNull { it.id?.let { id -> id to it.text } }.toMap()

