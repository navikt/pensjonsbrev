package no.nav.pensjon.brev.api.model

import com.fasterxml.jackson.module.jsonSchema.JsonSchema

data class TemplateDescription(
    val name: String,
    val base: String,
    val letterDataSchema: JsonSchema,
    val languages: List<LanguageCode>
)

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;
}
