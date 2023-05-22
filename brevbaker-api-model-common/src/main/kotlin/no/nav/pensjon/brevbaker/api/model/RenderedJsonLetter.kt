package no.nav.pensjon.brevbaker.api.model

@Suppress("unused")
data class RenderedJsonLetter(val title: String, val sakspart: Sakspart, val blocks: List<Block>, val signatur: Signatur) {

    data class Sakspart(val gjelderNavn: String, val gjelderFoedselsnummer: String, val saksnummer: String, val dokumentDato: String)
    data class Signatur(
        val hilsenTekst: String,
        val saksbehandlerRolleTekst: String,
        val saksbehandlerNavn: String,
        val attesterendeSaksbehandlerNavn: String?,
        val navAvsenderEnhet: String,
    )

    sealed class Block(open val id: Int, open val type: Type, open val editable: Boolean = true) {
        enum class Type {
            TITLE1, TITLE2, PARAGRAPH,
        }

        data class Title1(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE1, editable)
        data class Title2(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent.Text>) : Block(id, Type.TITLE2, editable)
        data class Paragraph(override val id: Int, override val editable: Boolean, val content: List<ParagraphContent>) : Block(id, Type.PARAGRAPH, editable)
    }

    sealed class ParagraphContent(open val id: Int, open val type: Type) {
        enum class Type {
            ITEM_LIST, LITERAL, VARIABLE,
        }

        data class ItemList(override val id: Int, val items: List<Item>) : ParagraphContent(id, Type.ITEM_LIST) {
            data class Item(val content: List<Text>)
        }

        sealed class Text(id: Int, type: Type, open val text: String) : ParagraphContent(id, type) {
            data class Literal(override val id: Int, override val text: String) : Text(id, Type.LITERAL, text)
            data class Variable(override val id: Int, override val text: String) : Text(id, Type.VARIABLE, text)
        }
    }
}
