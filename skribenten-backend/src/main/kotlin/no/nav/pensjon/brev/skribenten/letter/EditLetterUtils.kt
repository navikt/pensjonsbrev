package no.nav.pensjon.brev.skribenten.letter

fun Edit.Letter.variablesValueMap(): Map<Int, String> =
    variables.mapNotNull { it.id?.let { id -> id to it.text } }.toMap()

