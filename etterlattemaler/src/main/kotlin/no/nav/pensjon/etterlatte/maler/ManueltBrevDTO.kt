package no.nav.pensjon.etterlatte.maler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue

data class ManueltBrevMedTittelDTO(
    override val innhold: List<Element> = emptyList(),
    val tittel: String? = null,
) : FerdigstillingBrevDTO

data class ManueltBrevDTO(
    override val innhold: List<Element> = emptyList(),
) : BrevDTO

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
)

enum class ElementType(
    @JsonValue val value: String,
) {
    HEADING_TWO("heading-two"),
    HEADING_THREE("heading-three"),
    PARAGRAPH("paragraph"),
    BULLETED_LIST("bulleted-list"),
    LIST_ITEM("list-item"),
}
