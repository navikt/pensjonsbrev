package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Sakspart
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Signatur


fun letter(vararg blocks: Block) =
    LetterMarkup(
        title = "En tittel",
        sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
        blocks = blocks.toList(),
        signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn")
    )

fun editedLetter(vararg blocks: Edit.Block, deleted: Set<Int> = emptySet()): Edit.Letter =
    Edit.Letter(
        "Et koselig brev",
        Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
        blocks.toList().toList(),
        Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn"),
        deleted
    )