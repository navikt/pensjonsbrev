package no.nav.pensjon.etterlatte.maler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue


data class Slate(
    @JsonValue val elements: List<Element> = emptyList(),
)

data class Element(
    val type: ElementType,
    val children: List<InnerElement> = emptyList(),
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class InnerElement(
    val type: ElementType? = null,
    val text: String? = null,
    val children: List<InnerElement>? = null,
    // TODO: Sjekk på at denne ikke finnes ved ferdigstilling av PDF
    val placeholder: Boolean? = null,
)

enum class ElementType(@JsonValue val value: String) {
    HEADING_TWO("heading-two"),
    HEADING_THREE("heading-three"),
    PARAGRAPH("paragraph"),
    BULLETED_LIST("bulleted-list"),
    LIST_ITEM("list-item"),
}