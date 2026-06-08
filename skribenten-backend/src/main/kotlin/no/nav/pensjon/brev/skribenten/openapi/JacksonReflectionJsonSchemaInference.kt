package no.nav.pensjon.brev.skribenten.openapi

import io.ktor.openapi.*
import io.ktor.utils.io.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A copy of Ktor's [io.ktor.openapi.reflect.ReflectionJsonSchemaInference] with two improvements:
 *
 * 1. The [adapter] field is typed as our concrete [JacksonSchemaReflectionAdapter] class (not the
 *    upstream interface), so we can call the extra methods [JacksonSchemaReflectionAdapter.getDiscriminatorProperty]
 *    and [JacksonSchemaReflectionAdapter.getDiscriminatorMappingName] without casting.
 *
 * 2. The sealed-class discriminator logic reads Jackson `@JsonSubTypes` names via the adapter instead
 *    of using `KClass.qualifiedName`. Intermediate sealed classes (those not listed in any ancestor's
 *    `@JsonSubTypes`, like `Edit.ParagraphContent.Text`) are detected and their concrete subtypes are
 *    inlined directly into the parent discriminator, so no intermediate discriminator key is generated.
 *
 * When Ktor releases the upstream fixes (tracked via issue reported by the team), this class can be
 * deleted and [JacksonSchemaReflectionAdapter] can be made to simply implement the updated interface.
 */
class JacksonReflectionJsonSchemaInference(
    private val adapter: JacksonSchemaReflectionAdapter,
) : JsonSchemaInference {

    override fun buildSchema(type: KType): JsonSchema =
        buildSchemaInternal(type, LinkedHashSet())

    /**
     * Creates an object schema for [kClass] with properties inferred from Kotlin reflection.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.openapi.reflect.ReflectionJsonSchemaInference.schemaForClass)
     */
    fun schemaForClass(kClass: KClass<*>): JsonSchema {
        return buildSchemaInternal(
            kClass.starProjectedTypeOrNull() ?: return JsonSchema(type = JsonType.OBJECT),
            LinkedHashSet()
        )
    }

    fun schemaRefForClass(kClass: KClass<*>): ReferenceOr<JsonSchema> =
        ReferenceOr.Value(schemaForClass(kClass))

    @OptIn(InternalAPI::class)
    private fun buildSchemaInternal(
        type: KType,
        visiting: MutableSet<String>,
        includeAnnotations: List<Annotation> = emptyList()
    ): JsonSchema {
        val kClass = type.classifier as? KClass<*>
            ?: return JsonSchema(type = JsonType.OBJECT)

        // Nullability: OpenAPI schema has a `nullable` flag
        val nullable = adapter.isNullable(type)

        // Primitives / common JDK types
        val primitiveSchema = primitiveSchemaOrNull(kClass, includeAnnotations, nullable)
        if (primitiveSchema != null) {
            return primitiveSchema
        }

        // Value classes (inline) should be represented as their underlying value
        if (kClass.isValue) {
            kClass.underlyingValueClassTypeOrNull(type)?.let { underlyingType ->
                val unboxedSchema = buildSchemaInternal(
                    underlyingType,
                    visiting,
                    includeAnnotations + kClass.annotations
                )
                return unboxedSchema.wrapIfNullable(nullable)
            }
        }

        // Enums
        if (kClass.java.isEnum) {
            val values = kClass.java.enumConstants
                ?.map { it.toString() }
                .orEmpty()
                .map { GenericElement<String>(it) }

            return jsonSchemaFromAnnotations(
                annotations = includeAnnotations + kClass.annotations,
                reflectSchema = ::schemaRefForClass,
                type = JsonType.STRING.wrapIfNullable(nullable),
                enum = values,
            )
        }

        // Arrays / Iterables
        if (kClass == Array<Any>::class || kClass.java.isArray || kClass.isSubclassOf(Iterable::class)) {
            val itemType = type.arguments.firstOrNull()?.type
            val itemTypeName = itemType?.let { adapter.getName(it) }
            val itemRef = if (itemTypeName != null && itemTypeName in visiting) {
                ReferenceOr.schema(itemTypeName)
            } else {
                val itemSchema = itemType?.let { buildSchemaInternal(it, visiting) }
                    ?: JsonSchema(type = JsonType.OBJECT)
                ReferenceOr.Value(itemSchema)
            }

            return jsonSchemaFromAnnotations(
                annotations = includeAnnotations,
                reflectSchema = ::schemaRefForClass,
                type = JsonType.ARRAY.wrapIfNullable(nullable),
                items = itemRef,
            )
        }

        // Map -> object with additionalProperties
        if (kClass.isSubclassOf(Map::class)) {
            // key type ignored
            val valueType = type.arguments.getOrNull(1)?.type
            val valueTypeName = valueType?.let { adapter.getName(it) }

            // JSON object keys are strings; if key isn't String, we still produce an object schema.
            val additional = if (valueTypeName != null && valueTypeName in visiting) {
                AdditionalProperties.PSchema(ReferenceOr.schema(valueTypeName))
            } else {
                valueType?.let { v ->
                    AdditionalProperties.PSchema(ReferenceOr.Value(buildSchemaInternal(v, visiting)))
                } ?: AdditionalProperties.Allowed(true)
            }

            return jsonSchemaFromAnnotations(
                annotations = includeAnnotations,
                reflectSchema = ::schemaRefForClass,
                type = JsonType.OBJECT.wrapIfNullable(nullable),
                additionalProperties = additional,
            )
        }
        val typeName = adapter.getName(type)?.also(visiting::add)
        try {
            if (kClass.isSealed) {
                val oneOfSchemas = mutableListOf<ReferenceOr<JsonSchema>>()
                val discriminatorMapping = mutableMapOf<String, String>()

                // Recursively collect concrete subtypes, inlining intermediate sealed classes
                // (those not listed in any ancestor's @JsonSubTypes) into the parent discriminator.
                fun collectConcreteSubtypes(parentClass: KClass<*>) {
                    for (subclass in parentClass.sealedSubclasses) {
                        val mappingName = adapter.getDiscriminatorMappingName(subclass)
                        if (mappingName != null) {
                            oneOfSchemas += buildSchemaOrRef(subclass.starProjectedType, visiting)
                            val schemaName = adapter.getName(subclass.starProjectedTypeOrNull() ?: subclass.starProjectedType)
                                ?: subclass.qualifiedName
                                ?: continue
                            discriminatorMapping[mappingName] = "#/components/schemas/$schemaName"
                        } else if (subclass.isSealed) {
                            collectConcreteSubtypes(subclass)
                        }
                    }
                }

                collectConcreteSubtypes(kClass)

                // For nullable sealed classes, add {"type": "null"} to the oneOf instead of
                // using type: ["object", "null"]. This avoids the broken intersection type that
                // openapi-typescript generates for `type: ["object", "null"] + oneOf`, which
                // produces `(Record<string, never> | null) & (SubtypeA | SubtypeB)` where null
                // is not assignable as a standalone value due to how intersection distributes.
                if (nullable) {
                    oneOfSchemas += ReferenceOr.Value(JsonSchema(type = JsonType.NULL))
                }

                return jsonSchemaFromAnnotations(
                    title = typeName,
                    annotations = includeAnnotations + kClass.annotations,
                    reflectSchema = ::schemaRefForClass,
                    type = JsonType.OBJECT,
                    oneOf = oneOfSchemas,
                    discriminator = JsonSchemaDiscriminator(adapter.getDiscriminatorProperty(kClass), discriminatorMapping),
                )
            }

            val properties = mutableMapOf<String, ReferenceOr<JsonSchema>>()
            val required = mutableListOf<String>()

            for (prop in adapter.getProperties(kClass)) {
                if (adapter.isIgnored(prop)) continue

                val propertyName = adapter.getName(prop)
                val resolvedPropertyType = swapTypeArgs(prop.returnType, type)
                val propertyIsNullable = adapter.isNullable(resolvedPropertyType)

                properties[propertyName] = buildSchemaOrRef(resolvedPropertyType, visiting, prop.annotations)

                // Required: non-nullable properties are required (best effort; default values are not detectable reliably)
                if (!propertyIsNullable) {
                    required += propertyName
                }
            }

            return jsonSchemaFromAnnotations(
                title = typeName,
                annotations = includeAnnotations + kClass.annotations,
                reflectSchema = ::schemaRefForClass,
                type = JsonType.OBJECT,
                properties = properties.takeIf { it.isNotEmpty() },
                required = required.takeIf { it.isNotEmpty() },
            ).wrapIfNullable(nullable)
        } finally {
            typeName?.let(visiting::remove)
        }
    }

    private fun buildSchemaOrRef(
        type: KType,
        visiting: MutableSet<String>,
        includeAnnotations: List<Annotation> = emptyList(),
    ): ReferenceOr<JsonSchema> {
        val name = adapter.getName(type)
        val nullable = adapter.isNullable(type)
        return if (name != null && !visiting.add(name)) {
            if (nullable) {
                ReferenceOr.Value(
                    JsonSchema(
                        oneOf = listOf(
                            ReferenceOr.schema(name),
                            ReferenceOr.Value(JsonSchema(type = JsonType.NULL))
                        )
                    )
                )
            } else {
                ReferenceOr.schema(name)
            }
        } else {
            try {
                ReferenceOr.Value(buildSchemaInternal(type, visiting, includeAnnotations))
            } finally {
                visiting.remove(name)
            }
        }
    }

    private fun KClass<*>.underlyingValueClassTypeOrNull(ownerType: KType): KType? {
        val ctorParam = primaryConstructor?.parameters?.singleOrNull()
            ?: return null

        val propType = memberProperties.firstOrNull { it.name == ctorParam.name }?.returnType
            ?: ctorParam.type

        return swapTypeArgs(propType, ownerType)
    }

    private fun swapTypeArgs(propertyType: KType, ownerType: KType): KType {
        val ownerClass = ownerType.classifier as? KClass<*> ?: return propertyType
        val typeParameters = ownerClass.typeParameters
        if (typeParameters.isEmpty() || ownerType.arguments.isEmpty()) return propertyType

        val substitution = typeParameters
            .zip(ownerType.arguments)
            .mapNotNull { (param, arg) -> arg.type?.let { param to it } }
            .toMap()

        if (substitution.isEmpty()) return propertyType

        fun substitute(type: KType): KType {
            val classifier = type.classifier
            if (classifier is KTypeParameter) {
                return substitution[classifier] ?: type
            }

            val kClass = classifier as? KClass<*> ?: return type
            if (type.arguments.isEmpty()) return type

            val newArgs = type.arguments.map { projection ->
                val argType = projection.type ?: return@map projection
                KTypeProjection(projection.variance, substitute(argType))
            }

            return kClass.createType(newArgs, type.isMarkedNullable)
        }

        return substitute(propertyType)
    }

    @OptIn(
        ExperimentalTime::class,
        ExperimentalUuidApi::class,
        InternalAPI::class,
    )
    private fun primitiveSchemaOrNull(
        kClass: KClass<*>,
        annotations: List<Annotation>,
        nullable: Boolean
    ): JsonSchema? = when (kClass) {
        String::class, Char::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable)
        )

        Boolean::class -> jsonSchemaFromAnnotations(annotations, ::schemaRefForClass, type = JsonType.BOOLEAN)

        Byte::class, Short::class, Int::class, Long::class,
        UByte::class, UShort::class, UInt::class, ULong::class,
        java.lang.Byte::class, java.lang.Short::class, Integer::class, java.lang.Long::class ->
            jsonSchemaFromAnnotations(annotations, ::schemaRefForClass, type = JsonType.INTEGER)

        Float::class, Double::class,
        java.lang.Float::class, java.lang.Double::class ->
            jsonSchemaFromAnnotations(annotations, ::schemaRefForClass, type = JsonType.NUMBER)

        Uuid::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "uuid"
        )

        java.time.Instant::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date-time"
        )

        OffsetDateTime::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date-time"
        )

        java.time.LocalDate::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date"
        )

        java.time.LocalDateTime::class,
        kotlinx.datetime.Instant::class,
        Instant::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date-time"
        )

        LocalDate::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date"
        )

        LocalDateTime::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING.wrapIfNullable(nullable),
            format = "date-time"
        )

        Duration::class -> jsonSchemaFromAnnotations(
            annotations,
            ::schemaRefForClass,
            type = JsonType.STRING,
            format = "duration"
        )

        else -> null
    }

    private fun KClass<*>.starProjectedTypeOrNull(): KType? = try {
        @Suppress("UNCHECKED_CAST")
        (this as KClass<Any>).starProjectedType
    } catch (_: Exception) {
        null
    }
}
