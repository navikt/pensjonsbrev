package no.nav.pensjon.brev.brevoppskrift.model

@JvmInline
value class FieldId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class ChoiceId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class BlockId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class ScenarioId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class ReviewEventId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class AttachmentReference(val value: String) {
    override fun toString(): String = value
}
