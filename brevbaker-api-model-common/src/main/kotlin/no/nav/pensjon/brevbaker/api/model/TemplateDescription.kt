@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

data class TemplateDescription(
    val name: String,
    val letterDataClass: String,
    val languages: List<LanguageCode>,
    val metadata: LetterMetadata,
)

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;
}