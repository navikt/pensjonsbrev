package no.nav.pensjon.brev.brevoppskrift.kotlin

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import java.io.File
import kotlin.reflect.KClass

data class GeneratedDtoJsonFixture(
    val dtoClass: String, val templateClass: String, val editable: Boolean, val data: List<JsonNode>,
    val attachment: Boolean = false, val attachmentCount: Int = 0,
)

/**
 * Build-only verification of the actual compiled generator output with the production consumer mapper.
 * Reflection keeps consumer/DSL dependencies out of the backend and the generated public DTOs.
 */
fun main(args: Array<String>) {
    require(args.size == 1)
    val fixtures = draftObjectMapper.readValue(File(args[0]), object : TypeReference<List<GeneratedDtoJsonFixture>>() {})
    check(fixtures.size == 12 && fixtures.count { it.editable } == 4 && fixtures.count { it.attachment } == 4) { "Expected letter and standalone attachment profiles in all four modules." }
    check(fixtures.map { it.dtoClass }.distinct().size == 12) { "DTO fixtures must be distinct." }
    val mapper = Class.forName("no.nav.pensjon.brev.template.UtilsKt")
        .getMethod("brevbakerJacksonObjectMapper").invoke(null) as ObjectMapper
    fixtures.forEach { fixture ->
        check(fixture.data.size == 2) { "Expected populated and nullable DTO scenarios." }
        fixture.data.forEach { verifyGeneratedDto(mapper, fixture, it) }
    }
    println("AUTHORING_DTO_JSON_VERIFIED=${fixtures.size}")
}
private fun verifyGeneratedDto(mapper: ObjectMapper, fixture: GeneratedDtoJsonFixture, data: JsonNode) {
    val dtoClass = Class.forName(fixture.dtoClass)
    if (!fixture.editable) {
        val dto = requireNotNull(mapper.readValue(data.toString(), dtoClass))
        check(dto == mapper.readValue(mapper.writeValueAsString(dto), dtoClass)) { "Autobrev DTO round-trip failed: ${fixture.dtoClass}" }
        check(mapper.valueToTree<JsonNode>(dto) == data) { "Autobrev DTO wire format changed: ${fixture.dtoClass}" }
        if (!fixture.attachment) verifyAttachmentBindings(mapper, fixture, dto, data)
        return
    }
    val choices = listOf<JsonNode?>(
        null, // Missing constructor argument must use Valg().
        mapper.readTree("{}"),
        mapper.readTree("""{"include":true,"spare":false}"""),
        mapper.readTree("""{"include":false}"""),
        mapper.readTree("""{"include":null}"""),
    )
    val genericBrevdata = Class.forName("no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata")
    choices.forEach { choice ->
        val payload = mapper.createObjectNode().apply {
            set<JsonNode>("pesysData", data)
            if (choice != null) set<JsonNode>("saksbehandlerValg", choice)
        }
        val directlyDecoded = requireNotNull(mapper.readValue(payload.toString(), dtoClass))
        // LetterFactory first receives generic BrevbakerBrevdata, then converts it to the template DTO.
        val generic = mapper.readValue(payload.toString(), genericBrevdata)
        val converted = requireNotNull(mapper.convertValue(generic, dtoClass))
        check(directlyDecoded == converted) { "Consumer convertValue disagrees with direct decode: ${fixture.dtoClass}" }
        listOf(directlyDecoded, converted).forEach { dto ->
            val encoded = mapper.valueToTree<ObjectNode>(dto)
            check(encoded.get("pesysData") == data) { "PesysData changed: ${fixture.dtoClass}" }
            check(encoded.get("saksbehandlerValg") == (choice ?: mapper.createObjectNode())) { "Choices must be a flat JSON map: ${fixture.dtoClass}" }
            val roundTrip = requireNotNull(mapper.readValue(mapper.writeValueAsString(dto), dtoClass))
            check(roundTrip == dto) { "Editable DTO round-trip failed: ${fixture.dtoClass}" }
            val decodedChoices = dtoClass.getMethod("getSaksbehandlerValg").invoke(roundTrip) as Map<*, *>
            check(decodedChoices["include"] == choice?.get("include")?.takeUnless { it.isNull }?.booleanValue()) {
                "Boolean choice changed: ${fixture.dtoClass}"
            }
            verifyBooleanDefault(fixture, roundTrip, choice?.get("include")?.asBoolean(false) ?: false)
            verifyAttachmentBindings(mapper, fixture, roundTrip, data)
            // Jackson's MapDeserializer must be able to put entries into the default-constructed map.
            decodedChoices.javaClass.getMethod("put", Any::class.java, Any::class.java).invoke(decodedChoices, "mutable-probe", true)
            check(decodedChoices["mutable-probe"] == true) { "Choice map is immutable: ${fixture.dtoClass}" }
        }

    }
}

private fun verifyAttachmentBindings(mapper: ObjectMapper, fixture: GeneratedDtoJsonFixture, dto: Any, data: JsonNode) {
    val templateClass = Class.forName(fixture.templateClass)
    val template = templateClass.getMethod("getTemplate").invoke(templateClass.getField("INSTANCE").get(null))
    val attachments = template.javaClass.getMethod("getAttachments").invoke(template) as List<*>
    check(attachments.size == fixture.attachmentCount) { "Attachment declarations were lost." }
    listOf("Bokmal", "Nynorsk", "English").forEach { language ->
        val scope = expressionScope(dto, language)
        var active = 0
        attachments.forEach { value ->
            val inclusion = requireNotNull(value)
            val predicate = inclusion.javaClass.getMethod("getPredicate").invoke(inclusion)
            val scopeClass = Class.forName("no.nav.pensjon.brev.template.ExpressionScope")
            if (predicate.javaClass.getMethod("eval", scopeClass).invoke(predicate, scope) == true) {
                active++
                val expression = inclusion.javaClass.getMethod("getData").invoke(inclusion)
                val bound = expression.javaClass.getMethod("eval", scopeClass).invoke(expression, scope)
                val child = inclusion.javaClass.getMethod("getTemplate").invoke(inclusion)
                val type = child.javaClass.getMethod("getDataType").invoke(child) as KClass<*>
                check(type.java.isInstance(bound)) { "Attachment data does not implement its declared DTO type." }
                val encoded = mapper.valueToTree<JsonNode>(bound)
                if (encoded.has("note")) {
                    check(encoded.get("note").textValue() == "Bound attachment") { "Attachment bound the wrong data." }
                } else {
                    check(encoded.isObject && encoded.isEmpty) { "Static attachment unexpectedly has data." }
                }
                check(mapper.readValue(encoded.toString(), type.java) == bound) { "Attachment DTO failed consumer JSON round-trip." }
            }
        }
        val expected = fixture.attachmentCount - if (data.get("optionalAttachment").isNull) 1 else 0
        check(active == expected) { "Nullable attachment inclusion is incorrect." }
        val rendererClass = Class.forName("no.nav.brev.brevbaker.LetterTestRenderer")
        val renderer = rendererClass.getField("INSTANCE").get(null)
        val rendered = rendererClass.methods.single { it.name == "renderAttachmentsOnly" }
            .invoke(renderer, scope, template) as List<*>
        check(rendered.size == expected) { "Production renderer dropped or added attachments." }
        check(mapper.writeValueAsString(rendered).contains("Bound attachment")) { "Rendered attachments lost their bound wording." }
    }
}

private fun expressionScope(dto: Any, languageName: String): Any {
    val factoryClass = Class.forName("no.nav.brev.brevbaker.FellesFactory")
    val felles = factoryClass.getMethod("getFelles").invoke(factoryClass.getField("INSTANCE").get(null))
    val language = Class.forName("no.nav.pensjon.brev.template.Language\$$languageName").getField("INSTANCE").get(null)
    val scopeClass = Class.forName("no.nav.pensjon.brev.template.ExpressionScope")
    val companion = scopeClass.getField("Companion").get(null)
    return companion.javaClass.methods.single { it.name == "invoke" && it.parameterCount == 4 }
        .invoke(companion, dto, felles, language, null)
}

private fun verifyBooleanDefault(fixture: GeneratedDtoJsonFixture, dto: Any, expected: Boolean) {
    val templateClass = Class.forName(fixture.templateClass)
    val template = templateClass.getMethod("getTemplate").invoke(templateClass.getField("INSTANCE").get(null))
    val outline = template.javaClass.getMethod("getOutline").invoke(template) as List<*>
    // Compilation fixtures append a real DSL showIf(saksbehandlervalg(...).bool()) as the last block.
    val conditional = requireNotNull(outline.last())
    val predicate = conditional.javaClass.getMethod("getPredicate").invoke(conditional)
    val scopeClass = Class.forName("no.nav.pensjon.brev.template.ExpressionScope")
    listOf("Bokmal", "Nynorsk", "English").forEach { languageName ->
        val scope = expressionScope(dto, languageName)
        val evaluated = predicate.javaClass.getMethod("eval", scopeClass).invoke(predicate, scope)
        check(evaluated == expected) { "Actual DSL Boolean evaluation failed for ${fixture.dtoClass}/$languageName: expected $expected, got $evaluated" }
    }
}
