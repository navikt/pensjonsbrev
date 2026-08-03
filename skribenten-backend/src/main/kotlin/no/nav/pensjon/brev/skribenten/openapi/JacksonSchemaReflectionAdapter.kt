package no.nav.pensjon.brev.skribenten.openapi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.openapi.reflect.SchemaReflectionAdapter
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation

/**
 * A concrete [SchemaReflectionAdapter] that reads Jackson annotations to produce
 * correct discriminator property names and mapping keys.
 *
 * Two methods are anticipated by the Ktor team for the upstream [SchemaReflectionAdapter] interface:
 * - [getDiscriminatorProperty] (already added in Ktor main branch)
 * - [getDiscriminatorMappingName] (proposed: https://youtrack.jetbrains.com/issue/KTOR-9648)
 *
 * Once Ktor releases these, the class can be simplified to just override the interface methods
 * and [JacksonReflectionJsonSchemaInference] can be removed.
 *
 * [objectMapper] is used to look up mix-in classes, enabling Jackson annotations on mix-ins
 * (registered via [ObjectMapper.addMixIn]) to be respected for classes that cannot declare
 * Jackson annotations directly (e.g. classes in published library modules without a Jackson dependency).
 */
class JacksonSchemaReflectionAdapter(
    private val objectMapper: ObjectMapper?,
) : SchemaReflectionAdapter {

    // qualifiedName -> assigned schema name
    private val assignedNames = mutableMapOf<String, String>()
    // schema name -> qualifiedName that owns it (for collision detection)
    private val takenNames = mutableMapOf<String, String>()

    /**
     * Returns the effective class to inspect for annotations: the mix-in registered for [kClass]
     * in the [objectMapper], or [kClass] itself if no mix-in is registered.
     */
    private fun effectiveClass(kClass: KClass<*>): KClass<*> =
        objectMapper?.findMixInClassFor(kClass.java)?.kotlin ?: kClass

    /**
     * Returns a dot-free schema name derived from the class hierarchy, guaranteed unique across
     * all classes seen during this schema generation run.
     *
     * Ktor's [mapToPathItemsAndSchema] derives the component key via `title.substringAfterLast('.')`.
     * Cycle-detection $refs in [JacksonReflectionJsonSchemaInference.buildSchemaOrRef] are emitted
     * using the name returned here. By returning a dot-free name, both the component key and our
     * refs always use the same string, avoiding mismatches.
     *
     * The candidate name starts from the first uppercase segment (class hierarchy start) and joins
     * the remaining segments without separator. If that collides with another class, progressively
     * more leading segments are prepended until a unique name is found. As a last resort the full
     * qualified name without dots is used.
     *
     * Examples:
     * - `no.nav...TemplateModelSpecification.FieldType` → `TemplateModelSpecificationFieldType`
     * - `no.nav...LetterMetadata.Distribusjonstype`     → `LetterMetadataDistribusjonstype`
     * - `no.nav...model.Distribusjon`                  → `Distribusjon`
     * - `no.foo.Bar` and `no.baz.Bar`                  → `Bar` and `bazBar`
     *
     * Package vs class boundary is detected by finding the first dot-segment that starts with an
     * uppercase letter (Kotlin/Java convention). For generic types, returns null (inlined).
     */
    override fun getName(type: KType): String? {
        if (type.arguments.isNotEmpty()) return null
        val kClass = type.classifier as? KClass<*> ?: return null
        val qualifiedName = kClass.qualifiedName ?: return kClass.simpleName

        assignedNames[qualifiedName]?.let { return it }

        val segments = qualifiedName.split('.')
        val classStart = segments.indexOfFirst { it.firstOrNull()?.isUpperCase() == true }
            .takeIf { it >= 0 } ?: (segments.size - 1)

        // Minimum candidate: class hierarchy without package prefix (e.g. "FieldType", "TemplateModelSpecificationFieldType")
        val minCandidate = segments.drop(classStart).joinToString("")

        // Start from minCandidate (seed) and grow outward by prepending package segments one by one
        // until a unique name is found. Package segments are reversed so the fold starts closest
        // to the class boundary. Last resort: full qualified name without dots.
        val uniqueName = segments.take(classStart)
            .reversed()
            .runningFold(minCandidate) { acc, seg -> seg + acc }
            .firstOrNull { takenNames[it].let { owner -> owner == null || owner == qualifiedName } }
            ?: segments.joinToString("")

        assignedNames[qualifiedName] = uniqueName
        takenNames[uniqueName] = qualifiedName
        return uniqueName
    }

    /**
     * Returns the discriminator property name for the given sealed [kClass].
     *
     * Reads the [JsonTypeInfo.property] from [kClass] or its ancestors (including mix-ins),
     * defaulting to `"type"`.
     *
     * Mirrors `fun getDiscriminatorProperty(kClass: KClass<*>): String` from Ktor main branch.
     */
    override fun getDiscriminatorProperty(kClass: KClass<*>): String {
        val visited = mutableSetOf<KClass<*>>()
        val queue = ArrayDeque<KClass<*>>()
        queue.add(kClass)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (!visited.add(current)) continue

            val typeInfo = effectiveClass(current).findAnnotation<JsonTypeInfo>()
            if (typeInfo != null && typeInfo.property.isNotEmpty()) {
                return typeInfo.property
            }
            queue.addAll(current.supertypes.mapNotNull { it.classifier as? KClass<*> })
        }
        return "type"
    }

    /**
     * Returns the discriminator mapping name (Jackson name) for a given sealed subclass,
     * or `null` if the subclass is an intermediate abstract class whose own subtypes should
     * be inlined into the parent discriminator.
     *
     * Walks up the supertype hierarchy to find a [JsonSubTypes] annotation on any ancestor
     * (or its mix-in) that directly references [kClass]. If found, returns the Jackson name
     * from that entry. If not found, returns `null` — signalling that [kClass] is an intermediate class.
     *
     * Mirrors the proposed `fun getDiscriminatorMappingName(kClass: KClass<*>): String`
     * from the Ktor team (their default returns `kClass.qualifiedName!!`).
     */
    fun getDiscriminatorMappingName(kClass: KClass<*>): String? {
        val visited = mutableSetOf<KClass<*>>()
        val queue = ArrayDeque<KClass<*>>()
        queue.addAll(kClass.supertypes.mapNotNull { it.classifier as? KClass<*> })

        while (queue.isNotEmpty()) {
            val ancestor = queue.removeFirst()
            if (!visited.add(ancestor)) continue

            val subTypes = effectiveClass(ancestor).findAnnotation<JsonSubTypes>()
            if (subTypes != null) {
                val name = subTypes.value.firstOrNull { it.value == kClass }?.name
                if (name != null) return name
            }
            ancestor.supertypes.mapNotNull { it.classifier as? KClass<*> }.forEach(queue::add)
        }
        return null
    }
}
