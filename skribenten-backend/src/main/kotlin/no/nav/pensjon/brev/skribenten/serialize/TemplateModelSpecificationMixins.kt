package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

object TemplateModelSpecificationMixins: MixinModule {

    override fun register(mapper: ObjectMapper): ObjectMapper = mapper.apply {
        addMixIn(TemplateModelSpecification.FieldType::class.java, FieldTypeMixin::class.java)
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Scalar::class, name = "scalar"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Enum::class, name = "enum"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Array::class, name = "array"),
        JsonSubTypes.Type(TemplateModelSpecification.FieldType.Object::class, name = "object"),
    )
    abstract class FieldTypeMixin
}
