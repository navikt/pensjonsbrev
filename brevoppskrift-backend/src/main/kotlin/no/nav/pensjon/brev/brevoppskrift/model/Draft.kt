package no.nav.pensjon.brev.brevoppskrift.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.JsonNode
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import java.math.BigDecimal
import java.time.Instant

const val DRAFT_FORMAT_VERSION = 1

enum class DraftLanguage { NB, NN, EN }
enum class TargetModule { UFOERE, PENSJON, ALDER, PLANLEGGE_PENSJON }
enum class DocumentType { AUTOBREV, REDIGERBAR, VEDLEGG }
enum class FontType { PLAIN, BOLD, ITALIC }
enum class FieldKind { STRING, NUMBER, DOUBLE, BOOLEAN, DATE, YEAR, MONTH, PERCENT, ENUM, OBJECT, LIST }
enum class Formatter { DEFAULT, CURRENCY, MONTH_YEAR }
enum class Comparison { EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL }

typealias Translations = Map<DraftLanguage, String>

data class Draft(
    val formatVersion: Int,
    val metadata: DraftMetadata,
    val title: Translations,
    val fields: List<DataField> = emptyList(),
    val choices: List<CaseworkerChoice> = emptyList(),
    val outline: List<Block> = emptyList(),
    val source: SourceReference? = null,
    val scenarios: List<TestScenario> = emptyList(),
    val comments: List<DraftComment> = emptyList(),
    val baseline: DraftBaseline? = null,
    val attachments: List<AttachmentCopy> = emptyList(),
)

data class DraftMetadata(
    val name: String,
    val type: DocumentType,
    val targetModule: TargetModule,
    val languages: Set<DraftLanguage>,
    val displayTitle: String? = null,
    val brevkode: String? = null,
    val kategori: String? = null,
    val brevkontekst: String? = null,
    val sakstyper: Set<String> = emptySet(),
    val distribusjonstype: String? = null,
    val brevtype: String? = null,
    val featureToggle: String? = null,
)

data class DataField(
    val id: FieldId,
    val name: String,
    val kind: FieldKind,
    val nullable: Boolean = false,
    val children: List<DataField> = emptyList(),
    val enumValues: List<String> = emptyList(),
    val typeReference: String? = null,
)

data class CaseworkerChoice(
    val id: ChoiceId,
    val name: String,
    val prompt: Translations,
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Block.Heading::class, name = "HEADING"),
    JsonSubTypes.Type(Block.Paragraph::class, name = "PARAGRAPH"),
    JsonSubTypes.Type(Block.ItemList::class, name = "ITEM_LIST"),
    JsonSubTypes.Type(Block.Table::class, name = "TABLE"),
    JsonSubTypes.Type(Block.Conditional::class, name = "CONDITIONAL"),
    JsonSubTypes.Type(Block.Repeat::class, name = "REPEAT"),
    JsonSubTypes.Type(Block.LockedSource::class, name = "LOCKED_SOURCE"),
)
sealed interface Block {
    val id: BlockId

    data class Heading(override val id: BlockId, val level: Int, val text: List<Inline>) : Block
    data class Paragraph(override val id: BlockId, val text: List<Inline>) : Block
    data class ItemList(override val id: BlockId, val numbered: Boolean, val items: List<List<Inline>>) : Block
    data class Table(override val id: BlockId, val header: List<Translations>, val rows: List<List<List<Inline>>>) : Block
    data class Conditional(override val id: BlockId, val predicate: Predicate, val ifTrue: List<Block>, val ifFalse: List<Block> = emptyList()) : Block
    data class Repeat(override val id: BlockId, val fieldId: FieldId, val body: List<Block>) : Block
    /** Binding ID is resolved exclusively from the trusted, build-time catalogue. Never Kotlin text. */
    data class LockedSource(override val id: BlockId, val bindingId: String) : Block
}

data class SourceReference(val id: SourceId, val commit: GitCommit, val files: Map<RepositoryPath, Sha256Digest>)
data class TestScenario(
    val id: ScenarioId,
    val name: String,
    val values: Map<FieldId, JsonNode>,
    val choices: Map<ChoiceId, Boolean> = emptyMap(),
)
data class DraftBaseline(
    val draft: Draft,
    val kind: String,
    val approvedAt: Instant? = null,
    val approvedBy: AuthorName? = null,
)
data class DraftComment(
    val id: ReviewEventId,
    val anchor: CommentAnchor,
    val text: String,
    val author: AuthorName? = null,
    val at: Instant? = null,
    val replies: List<CommentReply> = emptyList(),
    val states: List<CommentState> = emptyList(),
    val role: String? = null,
    val scenarioId: ScenarioId? = null,
)
data class CommentAnchor(
    val type: String,
    val blockId: BlockId? = null,
    val fieldId: FieldId? = null,
    val choiceId: ChoiceId? = null,
    val language: DraftLanguage? = null,
    val quote: String? = null,
)
data class CommentReply(val id: ReviewEventId, val text: String, val author: AuthorName? = null, val at: Instant? = null)
data class CommentState(val id: ReviewEventId, val resolved: Boolean, val author: AuthorName? = null, val at: Instant? = null)
data class AttachmentCopy(val id: AttachmentReference, val revision: DraftRevision, val draft: Draft, val fieldId: FieldId? = null)

/** Use for the separate approval operation, not an ordinary PUT. */
fun Draft.forBaseline(): Draft = copy(
    baseline = null,
    comments = emptyList(),
    attachments = attachments.map { it.copy(draft = it.draft.forBaseline()) },
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Inline.Text::class, name = "TEXT"),
    JsonSubTypes.Type(Inline.Field::class, name = "FIELD"),
    JsonSubTypes.Type(Inline.NewLine::class, name = "NEW_LINE"),
    JsonSubTypes.Type(Inline.Conditional::class, name = "CONDITIONAL"),
    JsonSubTypes.Type(Inline.FreeText::class, name = "FREE_TEXT"),
)
sealed interface Inline {
    data class Text(val text: Translations, val fontType: FontType = FontType.PLAIN) : Inline
    data class Field(val fieldId: FieldId, val formatter: Formatter = Formatter.DEFAULT, val fallback: Translations? = null) : Inline
    data object NewLine : Inline
    data class Conditional(val predicate: Predicate, val ifTrue: List<Inline>, val ifFalse: List<Inline>) : Inline
    data class FreeText(val prompt: Translations) : Inline
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Predicate.Compare::class, name = "COMPARE"),
    JsonSubTypes.Type(Predicate.IsNotNull::class, name = "IS_NOT_NULL"),
    JsonSubTypes.Type(Predicate.Choice::class, name = "CHOICE"),
    JsonSubTypes.Type(Predicate.All::class, name = "ALL"),
    JsonSubTypes.Type(Predicate.Any::class, name = "ANY"),
    JsonSubTypes.Type(Predicate.Not::class, name = "NOT"),
)
sealed interface Predicate {
    data class Compare(val fieldId: FieldId, val operator: Comparison, val value: Literal) : Predicate
    data class IsNotNull(val fieldId: FieldId) : Predicate
    data class Choice(val choiceId: ChoiceId, val value: Boolean) : Predicate
    data class All(val predicates: List<Predicate>) : Predicate
    data class Any(val predicates: List<Predicate>) : Predicate
    data class Not(val predicate: Predicate) : Predicate
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Literal.Text::class, name = "TEXT"),
    JsonSubTypes.Type(Literal.Number::class, name = "NUMBER"),
    JsonSubTypes.Type(Literal.BooleanValue::class, name = "BOOLEAN"),
    JsonSubTypes.Type(Literal.Date::class, name = "DATE"),
)
sealed interface Literal {
    data class Text(val value: String) : Literal
    data class Number(val value: BigDecimal) : Literal
    data class BooleanValue(val value: Boolean) : Literal
    data class Date(val value: String) : Literal
}
