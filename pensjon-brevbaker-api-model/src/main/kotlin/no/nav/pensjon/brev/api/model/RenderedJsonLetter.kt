package no.nav.pensjon.brev.api.model


typealias TreeLocation = List<String>

@Suppress("unused")
data class RenderedJsonLetter(val title: String, val sakspart: Sakspart, val blocks: List<Block>, val signatur: Signatur) {

    data class Content(val type: Type, val id: Int, val location: TreeLocation, val text: String) {
        enum class Type {
            LITERAL, VARIABLE
        }
    }

    data class Block(val id: Int, val type: Type, val location: TreeLocation, val content: List<Content>, val editable: Boolean = true) {
        enum class Type {
            TITLE1, PARAGRAPH, TEXT
        }
    }

    data class Sakspart(val gjelderNavn: String, val gjelderFoedselsnummer: String, val saksnummer: String, val dokumentDato: String)
    data class Signatur(
        val hilsenTekst: String,
        val saksbehandlerRolleTekst: String,
        val saksbehandlerNavn: String,
        val attesterendeSaksbehandlerNavn: String?,
        val navAvsenderEnhet: String,
    )
}
