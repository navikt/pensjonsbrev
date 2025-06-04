package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import java.util.Objects

internal sealed class LanguageCombination {

    class Single<Lang : Language> internal constructor(val first: Lang) : LanguageCombination(), LanguageSupport.Single<Lang>, StableHash by StableHash.of(first) {
        override fun supports(language: Language): Boolean = language == first
        override fun all(): Set<Language> = setOf(first)

        override fun equals(other: Any?): Boolean {
            if (other !is Single<*>) return false
            return first == other.first
        }
        override fun hashCode(): Int = Objects.hash(first)
        override fun toString() = "Single(first=$first)"

    }

    class Double<Lang1 : Language, Lang2 : Language> internal constructor(
        val first: Lang1,
        val second: Lang2,
    ) : LanguageCombination(), LanguageSupport.Double<Lang1, Lang2>, StableHash by StableHash.of(first, second) {
        override fun supports(language: Language): Boolean = language == first || language == second
        override fun all(): Set<Language> = setOf(first, second)

        override fun equals(other: Any?): Boolean {
            if (other !is Double<*, *>) return false
            return first == other.first && second == other.second
        }
        override fun hashCode(): Int = Objects.hash(first, second)
        override fun toString() = "Double(first=$first, second=$second)"

    }

    class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language> internal constructor(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination(), LanguageSupport.Triple<Lang1, Lang2, Lang3>, StableHash by StableHash.of(first, second, third) {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
        override fun all(): Set<Language> = setOf(first, second, third)

        override fun equals(other: Any?): Boolean {
            if (other !is Triple<*, *, *>) return false
            return first == other.first && second == other.second && third == other.third
        }
        override fun hashCode(): Int = Objects.hash(first, second, third)
        override fun toString() = "Triple(first=$first, second=$second, third=$third)"

    }

}

fun <Lang1 : Language, Lang2 : Language, AttachmentData: Any>
        TemplateRootScope<LanguageSupport.Double<Lang1, Lang2>, *>.includeAttachment(
    attachment: AttachmentTemplate<LanguageSupport.Triple<Lang1, *, Lang2>, AttachmentData>,
    attachmentData: Expression<AttachmentData>,
    predicate: Expression<Boolean> = true.expr(),
) = includeAttachment(castAttachment(attachment), attachmentData, predicate)

// Det er trygt å caste her fordi receiver og phrase begge har Lang1 og Lang2.
@Suppress("UNCHECKED_CAST")
private fun <Lang1 : Language, Lang2 : Language, AttachmentData: Any> castAttachment(attachment: AttachmentTemplate<LanguageSupport.Triple<Lang1, *, Lang2>, AttachmentData>) =
    attachment as AttachmentTemplate<LanguageSupport.Double<Lang1, Lang2>, AttachmentData>