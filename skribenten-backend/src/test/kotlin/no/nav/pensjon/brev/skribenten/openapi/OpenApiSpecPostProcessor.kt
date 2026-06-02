package no.nav.pensjon.brev.skribenten.openapi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
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

            // Rebuild the mapping with Jackson discriminator names as keys
            val newMapping = objectMapper.createObjectNode()
            for (fqcn in fqcnKeys) {
                val jvmClassName = loadNestedClass(fqcn)?.name
                val jacksonName = jvmClassName?.let { jacksonNames[it] } ?: fqcn.substringAfterLast('.')
                val refValue = mapping.get(fqcn)?.asText() ?: continue
                newMapping.put(jacksonName, refValue)
            }
            discriminator.set<ObjectNode>("mapping", newMapping)
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
