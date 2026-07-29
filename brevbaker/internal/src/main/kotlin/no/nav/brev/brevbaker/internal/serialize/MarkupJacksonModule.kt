package no.nav.brev.brevbaker.internal.serialize

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text

/**
 * Jackson-oppsett for markup-modellen.
 *
 * `brevbaker:markup` er bevisst uten avhengigheter og inneholder derfor ingen Jackson-annotasjoner.
 * All serialiseringskunnskap ligger her, som mixins.
 *
 * Polymorfien bruker `type`-egenskapen som allerede finnes i modellen ([Block.type]/[Text.type]),
 * derfor [JsonTypeInfo.As.EXISTING_PROPERTY]: Jackson leser diskriminatoren fra JSON-en, men skriver
 * den ikke selv – den kommer fra modellen. Wire-formatet er dermed identisk med det kotlinx
 * produserte.
 */
object MarkupJacksonModule : SimpleModule("MarkupJacksonModule") {
    @Suppress("unused")
    private fun readResolve(): Any = MarkupJacksonModule

    init {
        setMixInAnnotation(Block::class.java, BlockMixin::class.java)
        setMixInAnnotation(Text::class.java, TextMixin::class.java)
        setMixInAnnotation(Text.NewLine::class.java, NewLineMixin::class.java)
    }

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = false,
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = Block.Title2::class, name = "TITLE2"),
        JsonSubTypes.Type(value = Block.Title3::class, name = "TITLE3"),
        JsonSubTypes.Type(value = Block.Title4::class, name = "TITLE4"),
        JsonSubTypes.Type(value = Block.Paragraph::class, name = "PARAGRAPH"),
        JsonSubTypes.Type(value = Block.ItemList::class, name = "ITEM_LIST"),
        JsonSubTypes.Type(value = Block.NumberedList::class, name = "NUMBERED_LIST"),
        JsonSubTypes.Type(value = Block.Table::class, name = "TABLE"),
        JsonSubTypes.Type(value = Block.FormText::class, name = "FORM_TEXT"),
        JsonSubTypes.Type(value = Block.FormChoice::class, name = "FORM_CHOICE"),
    )
    @JsonPropertyOrder("type", "id")
    private interface BlockMixin

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = false,
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = Text.Literal::class, name = "LITERAL"),
        JsonSubTypes.Type(value = Text.Variable::class, name = "VARIABLE"),
        JsonSubTypes.Type(value = Text.NewLine::class, name = "NEW_LINE"),
    )
    @JsonPropertyOrder("type", "id")
    private interface TextMixin

    /**
     * [Text.NewLine] arver `text` og `fontType` som utledede gettere med faste verdier (`""` og
     * `PLAIN`). De bærer ingen informasjon, så de holdes utenfor wire-formatet.
     */
    private interface NewLineMixin {
        @get:JsonIgnore
        val text: String

        @get:JsonIgnore
        val fontType: Text.FontType
    }
}
