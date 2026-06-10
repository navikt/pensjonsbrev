package no.nav.pensjon.brev.skribenten.openapi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.ktor.openapi.reflect.SchemaReflectionAdapter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
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
class JacksonSchemaReflectionAdapter : SchemaReflectionAdapter {
class JacksonSchemaReflectionAdapter(
    private val objectMapper: ObjectMapper?,
) : SchemaReflectionAdapter {

    /**
     * Returns the effective class to inspect for annotations: the mix-in registered for [kClass]
     * in the [objectMapper], or [kClass] itself if no mix-in is registered.
     */
    private fun effectiveClass(kClass: KClass<*>): KClass<*> =
        objectMapper?.findMixInClassFor(kClass.java)?.kotlin ?: kClass


    /**
     * Returns the discriminator property name for the given sealed [kClass].
     *
     * Reads the [JsonTypeInfo.property] from [kClass] or its ancestors (including mix-ins),
     * defaulting to `"type"`.
     *
     * Mirrors `fun getDiscriminatorProperty(kClass: KClass<*>): String` from Ktor main branch.
     */
    fun getDiscriminatorProperty(kClass: KClass<*>): String {
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
