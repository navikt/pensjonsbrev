package no.nav.brev.brevbaker.serialization

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

/**
 * Polymorfi for [TemplateModelSpecification.FieldType]. Diskriminatoren `type` finnes allerede som
 * en ekte property på modellen, så [JsonTypeInfo.As.EXISTING_PROPERTY] brukes for å unngå at feltet
 * skrives to ganger ved serialisering.
 */
object TemplateModelSpecificationJacksonModule : SimpleModule("TemplateModelSpecificationJacksonModule") {
    @Suppress("unused")
    private fun readResolve(): Any = TemplateModelSpecificationJacksonModule

    init {
        setMixInAnnotation(TemplateModelSpecification.FieldType::class.java, FieldTypeMixin::class.java)
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Scalar::class, name = "scalar"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Enum::class, name = "enum"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Array::class, name = "array"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Object::class, name = "object"),
    )
    abstract class FieldTypeMixin
}
