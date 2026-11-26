package no.nav.pensjon.brev.brevoppskrift.openapi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.openapi.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability

class JacksonSchemaInference(private val mapper: ObjectMapper) : JsonSchemaInference {
    private val names = mutableMapOf<String, KClass<*>>()

    override fun buildSchema(type: KType): JsonSchema = schema(type, linkedSetOf())

    private fun name(type: KType): String? {
        val klass = type.classifier as? KClass<*> ?: return null
        if (type.arguments.isNotEmpty()) return null
        val parts = requireNotNull(klass.qualifiedName).split('.')
        val name = parts.dropWhile { it.first().isLowerCase() }.joinToString("")
        require(names.putIfAbsent(name, klass).let { it == null || it == klass }) { "OpenAPI schema name collision: $name" }
        return name
    }

    private fun reference(type: KType, visiting: MutableSet<String>): ReferenceOr<JsonSchema> {
        if (type.isMarkedNullable) {
            return ReferenceOr.Value(nullable(reference(type.withNullability(false), visiting)))
        }
        val name = name(type)
        return if (name != null && name in visiting) ReferenceOr.schema(name)
        else ReferenceOr.Value(schema(type, visiting))
    }

    private fun nullable(value: ReferenceOr<JsonSchema>) = JsonSchema(
        oneOf = listOf(value, ReferenceOr.Value(JsonSchema(type = JsonType.NULL))),
    )

    private fun schema(type: KType, visiting: MutableSet<String>): JsonSchema {
        if (type.isMarkedNullable) return nullable(reference(type.withNullability(false), visiting))
        val klass = type.classifier as? KClass<*> ?: error("Unresolved OpenAPI type: $type")
        when (klass) {
            Any::class, JsonNode::class -> return JsonSchema()
            String::class, Char::class -> return JsonSchema(type = JsonType.STRING)
            Boolean::class -> return JsonSchema(type = JsonType.BOOLEAN)
            Byte::class, Short::class, Int::class, Long::class -> return JsonSchema(type = JsonType.INTEGER)
            Float::class, Double::class, BigDecimal::class -> return JsonSchema(type = JsonType.NUMBER)
            UUID::class -> return JsonSchema(type = JsonType.STRING, format = "uuid")
            Instant::class -> return JsonSchema(type = JsonType.STRING, format = "date-time")
        }
        if (klass.isSubclassOf(Map::class)) {
            val key = requireNotNull(type.arguments[0].type).classifier as? KClass<*>
            val value = reference(requireNotNull(type.arguments[1].type), visiting)
            return if (key?.java?.isEnum == true) {
                JsonSchema(
                    type = JsonType.OBJECT,
                    properties = key.java.enumConstants.associate { it.toString() to value },
                    additionalProperties = AdditionalProperties.Allowed(false),
                )
            } else {
                JsonSchema(type = JsonType.OBJECT, additionalProperties = AdditionalProperties.PSchema(value))
            }
        }
        if (klass.isSubclassOf(Iterable::class)) {
            return JsonSchema(
                type = JsonType.ARRAY,
                items = reference(requireNotNull(type.arguments.single().type), visiting),
                uniqueItems = if (klass.isSubclassOf(Set::class)) true else null,
            )
        }
        val name = requireNotNull(name(type))
        check(visiting.add(name)) { "Recursive type must use a schema reference: $type" }
        try {
            if (klass.isValue) {
                return schema(requireNotNull(klass.primaryConstructor).parameters.single().type, visiting).copy(title = name)
            }
            if (klass.java.isEnum) {
                return JsonSchema(
                    title = name,
                    type = JsonType.STRING,
                    enum = klass.java.enumConstants.map { GenericElement(it.toString()) },
                )
            }
            if (klass.isSealed) {
                val discriminator = requireNotNull(klass.findAnnotation<JsonTypeInfo>()).property
                val subtypes = requireNotNull(klass.findAnnotation<JsonSubTypes>()).value
                return JsonSchema(
                    title = name,
                    oneOf = subtypes.map { reference(it.value.starProjectedType, visiting) },
                    discriminator = JsonSchemaDiscriminator(
                        discriminator,
                        subtypes.associate { it.name to "#/components/schemas/${name(it.value.starProjectedType)}" },
                    ),
                )
            }
            val jacksonProperties = mapper.serializationConfig.introspect(mapper.constructType(klass.java))
                .findProperties().filter { it.couldSerialize() }
            val properties = klass.memberProperties.associateBy { it.name }
            val parameters = klass.primaryConstructor?.parameters.orEmpty().associateBy { it.name }
            val required = mutableListOf<String>()
            val schemas = jacksonProperties.associate { property ->
                val kotlinProperty = requireNotNull(properties[property.internalName]) { "Unresolved Jackson property: $property" }
                val parameter = parameters[kotlinProperty.name]
                if (!kotlinProperty.returnType.isMarkedNullable && parameter?.isOptional != true) required += property.name
                property.name to reference(kotlinProperty.returnType, visiting)
            }.toMutableMap()
            for (parent in klass.supertypes.mapNotNull { it.classifier as? KClass<*> }) {
                val subtype = parent.findAnnotation<JsonSubTypes>()?.value?.firstOrNull { it.value == klass } ?: continue
                val discriminator = requireNotNull(parent.findAnnotation<JsonTypeInfo>()).property
                schemas[discriminator] = ReferenceOr.Value(JsonSchema(type = JsonType.STRING, enum = listOf(GenericElement(subtype.name))))
                required += discriminator
            }
            return JsonSchema(
                title = name,
                type = JsonType.OBJECT,
                properties = schemas,
                required = required.distinct(),
                additionalProperties = AdditionalProperties.Allowed(false),
            )
        } finally {
            visiting.remove(name)
        }
    }
}
