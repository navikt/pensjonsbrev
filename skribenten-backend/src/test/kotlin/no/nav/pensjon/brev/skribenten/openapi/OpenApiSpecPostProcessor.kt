package no.nav.pensjon.brev.skribenten.openapi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Post-processes an OpenAPI spec JSON string to fix discriminator mappings.
 *
 * The Ktor [ReflectionJsonSchemaInference] uses fully qualified class names as discriminator
 * mapping keys, but Jackson uses the `name` values from `@JsonSubTypes` annotations.
 * This post-processor reads those annotations and rewrites the discriminator mappings to match
 * what Jackson actually serializes.
 */
class OpenApiSpecPostProcessor(private val objectMapper: ObjectMapper) {

    fun process(specJson: String): String {
        val root = objectMapper.readValue<ObjectNode>(specJson)
        val schemas = root.at("/components/schemas") as? ObjectNode ?: return specJson

        fixDiscriminatorMappings(schemas)

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root)
    }

    private fun fixDiscriminatorMappings(schemas: ObjectNode) {
        for (name in schemas.fieldNames().asSequence().toList()) {
            val schema = schemas.get(name) as? ObjectNode ?: continue
            val discriminator = schema.get("discriminator") as? ObjectNode ?: continue
            val mapping = discriminator.get("mapping") as? ObjectNode ?: continue

            val fqcnKeys = mapping.fieldNames().asSequence().toList()
            if (fqcnKeys.isEmpty()) continue

            // Load one of the subtype classes to find the parent with @JsonSubTypes
            val parentClass = fqcnKeys.firstNotNullOfOrNull { fqcn ->
                loadNestedClass(fqcn)?.let { findAnnotatedParent(it) }
            } ?: continue

            val jsonSubTypes = parentClass.getAnnotation(JsonSubTypes::class.java) ?: continue
            val typeInfo = parentClass.getAnnotation(JsonTypeInfo::class.java)

            // Build a lookup: JVM class name (dot-separated) -> Jackson name
            val jacksonNames: Map<String, String> = jsonSubTypes.value.associate { type ->
                type.value.java.name to type.name
            }

            // Fix propertyName from @JsonTypeInfo
            if (typeInfo != null && typeInfo.property.isNotEmpty()) {
                discriminator.put("propertyName", typeInfo.property)
            }

            // Rebuild the mapping with Jackson discriminator names as keys.
            // If a mapped class is not a direct Jackson subtype (i.e. it is an intermediate
            // abstract class like Text), skip it — its concrete subtypes are already listed
            // directly in the parent's @JsonSubTypes and will be added in the second pass.
            val newMapping = objectMapper.createObjectNode()
            val newOneOf = objectMapper.createArrayNode()
            val oneOfArray = schema.withArrayProperty("oneOf")

            // Track intermediate abstract classes (present in Ktor's mapping but absent from
            // @JsonSubTypes) so we can inline their concrete subtypes in the second pass.
            val intermediateClasses = mutableListOf<Class<*>>()

            for (fqcn in fqcnKeys) {
                val jvmClass = loadNestedClass(fqcn)
                val jacksonName = jvmClass?.name?.let { jacksonNames[it] }
                val refValue = mapping.get(fqcn)?.asText() ?: continue

                if (jacksonName != null) {
                    // Direct Jackson subtype — add to new mapping and oneOf
                    newMapping.put(jacksonName, refValue)
                    oneOfArray.firstOrNull { it.get("\$ref")?.asText() == refValue }
                        ?.let { newOneOf.add(it) }
                } else if (jvmClass != null) {
                    intermediateClasses += jvmClass
                }
            }

            // Add any Jackson subtypes not found via Ktor's FQCN entries.
            // This covers concrete subtypes of intermediate abstract classes (e.g. Literal, Variable,
            // NewLine are listed in ParagraphContent's @JsonSubTypes but only reachable via Text in
            // Ktor's reflection-based schema).
            // Only add a subtype if it is assignable from one of the intermediate classes, so that
            // sibling schemas (e.g. Text) don't accidentally inherit unrelated subtypes like ItemList.
            for (subtype in jsonSubTypes.value) {
                val name = subtype.name
                if (!newMapping.has(name)) {
                    val subtypeClass = subtype.value.java
                    if (intermediateClasses.any { it.isAssignableFrom(subtypeClass) }) {
                        val ref = "#/components/schemas/${subtypeClass.simpleName}"
                        newMapping.put(name, ref)
                        newOneOf.add(objectMapper.createObjectNode().put("\$ref", ref))
                    }
                }
            }

            discriminator.set<ObjectNode>("mapping", newMapping)
            schema.set<ArrayNode>("oneOf", newOneOf)
        }
    }

    /**
     * Walk up enclosing/super classes to find one annotated with @JsonSubTypes.
     */
    private fun findAnnotatedParent(clazz: Class<*>): Class<*>? {
        var candidate: Class<*>? = clazz.enclosingClass ?: clazz.superclass
        while (candidate != null) {
            if (candidate.isAnnotationPresent(JsonSubTypes::class.java)) {
                return candidate
            }
            candidate = candidate.enclosingClass ?: candidate.superclass
        }
        return null
    }

    /**
     * Load a class from a Kotlin FQCN like "com.example.Outer.Inner.Leaf"
     * by progressively replacing dots with $ from right to left until Class.forName succeeds.
     */
    private fun loadNestedClass(fqcn: String): Class<*>? {
        // Try as-is first (unlikely for nested classes but cheap)
        attemptClassLoad(fqcn)?.let { return it }

        // Progressively replace dots with $ from right to left
        val parts = fqcn.split('.')
        for (i in parts.size - 1 downTo 1) {
            val candidate = parts.subList(0, i).joinToString(".") + "$" +
                parts.subList(i, parts.size).joinToString("$")
            attemptClassLoad(candidate)?.let { return it }
        }
        return null
    }

    private fun attemptClassLoad(name: String): Class<*>? =
        try {
            Class.forName(name)
        } catch (_: ClassNotFoundException) {
            null
        }
}
