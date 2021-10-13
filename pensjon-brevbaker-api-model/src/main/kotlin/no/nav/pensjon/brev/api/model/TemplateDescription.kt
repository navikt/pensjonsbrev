package no.nav.pensjon.brev.api.model

data class TemplateDescription(
    val name: String,
    val base: String,
    val letterDataClass: String,
    val languages: List<LanguageCode>
)

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;
}
