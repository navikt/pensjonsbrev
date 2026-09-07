package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

/**
 * Datamodellen produsert av [TemplateDocumentationRendererV2]. Se designnotatet
 * "template-documentation-v2-design.md" for bakgrunn og motivasjon for modellen.
 *
 * Selve rendering-logikken (byggingen av denne modellen fra en [no.nav.pensjon.brev.template.LetterTemplate])
 * ligger i to andre filer i samme pakke:
 *  - `TemplateDocumentationRendererV2.kt` — traversering av innholdsstrukturen (titler, avsnitt,
 *    tabeller, lister osv).
 *  - `TemplateDocumentationRendererV2Expr.kt` — oversettelse av [no.nav.pensjon.brev.template.Expression]
 *    (predikater og tekstuttrykk) til [Expr].
 */
data class TemplateDocumentationV2(
    val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
    val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
    val attachments: List<Attachment>,
    val templateModelSpecification: TemplateModelSpecification,
) {
    data class Attachment(
        val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
        val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
        val include: Expr,
        val attachmentData: Expr,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStructureType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(ContentOrControlStructure.Conditional::class, name = "CONDITIONAL"),
        JsonSubTypes.Type(ContentOrControlStructure.Content::class, name = "CONTENT"),
        JsonSubTypes.Type(ContentOrControlStructure.ForEach::class, name = "FOR_EACH"),
    )
    @JsonPropertyOrder("controlStructureType")
    sealed class ContentOrControlStructure<E : Element> {
        data class Content<E : Element>(val content: E) : ContentOrControlStructure<E>()
        data class Conditional<E : Element>(
            val predicate: Expr,
            val showIf: List<ContentOrControlStructure<E>>,
            val elseIf: List<ElseIf<E>>,
            val showElse: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>() {
            data class ElseIf<E : Element>(val predicate: Expr, val showIf: List<ContentOrControlStructure<E>>)
        }

        data class ForEach<E : Element>(
            val items: Expr,
            val body: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "elementType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Element.OutlineContent.Title1::class, name = "TITLE1"),
        JsonSubTypes.Type(Element.OutlineContent.Title2::class, name = "TITLE2"),
        JsonSubTypes.Type(Element.OutlineContent.Title3::class, name = "TITLE3"),
        JsonSubTypes.Type(Element.OutlineContent.Paragraph::class, name = "PARAGRAPH"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Literal::class, name = "PARAGRAPH_TEXT_LITERAL"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Expression::class, name = "PARAGRAPH_TEXT_EXPRESSION"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList::class, name = "PARAGRAPH_ITEMLIST"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList.Item::class, name = "PARAGRAPH_ITEMLIST_ITEM"),
        JsonSubTypes.Type(Element.ParagraphContent.Table::class, name = "PARAGRAPH_TABLE"),
        JsonSubTypes.Type(Element.ParagraphContent.Table.Row::class, name = "PARAGRAPH_TABLE_ROW"),
    )
    @JsonPropertyOrder("elementType")
    sealed class Element {
        sealed class OutlineContent : Element() {
            data class Title1(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Title2(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Title3(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Paragraph(val paragraph: List<ContentOrControlStructure<ParagraphContent>>) : OutlineContent()
        }

        sealed class ParagraphContent : Element() {
            sealed class Text : ParagraphContent() {
                data class Literal(val text: String) : Text()
                data class Expression(val expression: Expr) : Text()
            }

            data class ItemList(val items: List<ContentOrControlStructure<Item>>) : ParagraphContent() {
                data class Item(val text: List<ContentOrControlStructure<Text>>) : Element()
            }

            data class Table(val header: Row, val rows: List<ContentOrControlStructure<Row>>) : ParagraphContent() {
                data class Row(val cells: List<Cell>) : Element()
                data class Cell(val text: List<ContentOrControlStructure<Text>>)
            }
        }
    }

    /**
     * Generalisert uttrykksmodell — dekker både predikater (showIf/ForEach.items) og
     * tekstuttrykk skrevet ut i brevet, siden begge er strukturelt samme problem
     * (se designnotat "template-documentation-v2-design.md"). Et "predikat" er ganske enkelt
     * en [Expr] man vet evaluerer til Boolean (typisk [Comparison], [AssociativeOp] med
     * AND/OR, [Not] eller enkelte [FunctionCall]).
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "exprType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Expr.Literal::class, name = "LITERAL"),
        JsonSubTypes.Type(Expr.FieldPath::class, name = "FIELD_PATH"),
        JsonSubTypes.Type(Expr.AssociativeOp::class, name = "ASSOCIATIVE_OP"),
        JsonSubTypes.Type(Expr.Comparison::class, name = "COMPARISON"),
        JsonSubTypes.Type(Expr.Not::class, name = "NOT"),
        JsonSubTypes.Type(Expr.FunctionCall::class, name = "FUNCTION_CALL"),
        JsonSubTypes.Type(Expr.Format::class, name = "FORMAT"),
        JsonSubTypes.Type(Expr.Conditional::class, name = "CONDITIONAL_EXPR"),
        JsonSubTypes.Type(Expr.NullCoalesce::class, name = "NULL_COALESCE"),
        JsonSubTypes.Type(Expr.EditableField::class, name = "EDITABLE_FIELD"),
    )
    @JsonPropertyOrder("exprType")
    sealed class Expr {
        data class Literal(val value: String, val kind: TemplateModelSpecification.FieldType.Scalar.Kind?) : Expr()

        data class FieldPath(val source: DataSource, val segments: List<String>, val leafType: String?) : Expr()

        data class AssociativeOp(val op: AssocOp, val operands: List<Expr>) : Expr()

        data class Comparison(val left: Expr, val op: CompareOp, val right: Expr) : Expr()

        data class Not(val term: Expr) : Expr()

        data class FunctionCall(val name: String, val args: List<Expr>) : Expr()

        /**
         * [exampleText] er et syntetisk, men 100% korrekt formattert eksempel (produsert ved å
         * kalle den faktiske [no.nav.pensjon.brev.template.LocalizedFormatter.apply] med en
         * fast eksempelverdi og riktig språk) — f.eks. "17. mars 2024" for et datofelt formattert
         * med `DateFormat`. Frontend bør vise dette som primær, kompakt tekst, og legge selve
         * uttrykksstrukturen (value + formatterName) bak en detaljvisning (popover). `null` når
         * formatteren er ukjent for oss eller eksempel-genereringen feiler — frontend faller da
         * tilbake til dagens fraseviisning ("formatert med <formatterName>").
         */
        data class Format(val value: Expr, val formatterName: String, val exampleText: String?) : Expr()

        /** Erstatter v1s `Invoke(IfElse, first, Tuple(ifTrue, ifElse))`-innpakking. */
        data class Conditional(val predicate: Expr, val ifTrue: Expr, val ifElse: Expr) : Expr()

        /** `?:` / `IfNull` — vist eksplisitt fremfor skjult slik v1 gjorde for `?: false`. */
        data class NullCoalesce(val value: Expr, val fallback: Expr) : Expr()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        data class EditableField(val kind: EditableKind, val value: Expr?, val fallback: Expr?) : Expr()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "dataSourceType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(DataSource.Scope::class, name = "SCOPE"),
        JsonSubTypes.Type(DataSource.ForEachVar::class, name = "FOR_EACH_VAR"),
        JsonSubTypes.Type(DataSource.Computed::class, name = "COMPUTED"),
    )
    @JsonPropertyOrder("dataSourceType")
    sealed class DataSource {
        data class Scope(val name: String) : DataSource()
        data class ForEachVar(val label: String, val depth: Int) : DataSource()

        /**
         * Feltaksess på et vilkårlig beregnet uttrykk, f.eks. `getOrNull(liste, 0).felt` eller
         * `(a ?: b).felt`. Uten denne varianten havnet slike kjeder i en bakvendt
         * `FunctionCall(".felt", [base])`-representasjon (se `renderUnaryInvoke`s
         * `UnaryOperation.Select`-håndtering) — [Computed] gjør at [FieldPath.segments] kan
         * bygges videre på toppen av hvilken som helst [Expr], ikke bare `Scope`/`ForEachVar`.
         */
        data class Computed(val expr: Expr) : DataSource()
    }

    enum class AssocOp { AND, OR, CONCAT, PLUS }
    enum class CompareOp { EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL }
    enum class EditableKind { FRITEKST, REDIGERBAR_DATA, BREVDATA_ELLER_FRITEKST }
}
