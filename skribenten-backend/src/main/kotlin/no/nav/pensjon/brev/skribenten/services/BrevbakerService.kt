package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brevbaker.api.model.*

class BrevbakerServiceException(msg: String) : Exception(msg)

class BrevbakerService(config: Config, authService: AzureADService): ServiceStatus {
    private val brevbakerUrl = config.getString("url")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(config.getString("scope"), authService) {
        defaultRequest {
            url(brevbakerUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                registerModule(LetterMarkupModule)
            }
        }
    }

    /**
     * Get model specification for a template.
     *
     * Returns a string because Skribenten-backend doesn't really care about the content.
     */
    suspend fun getModelSpecification(call: ApplicationCall, brevkode: Brevkode.Redigerbar): ServiceResult<String> =
        client.get(call, "/v2/templates/redigerbar/${brevkode.name}/modelSpecification").toServiceResult()

    suspend fun renderLetter(call: ApplicationCall, brevkode: Brevkode.Redigerbar, brevdata: RedigerbarBrevdata<*,*>, felles: Felles): ServiceResult<LetterMarkup> =
        client.post(call, "/v2/letter/redigerbar/markup") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = LanguageCode.BOKMAL,
                )
            )
        }.toServiceResult()

    suspend fun getTemplates(call: ApplicationCall): ServiceResult<List<TemplateDescription>> =
        client.get(call, "/v2/templates/redigerbar"){
            url {
                parameters.append("includeMetadata", "true")
            }
        }.toServiceResult()

    override val name = "Brevbaker"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.get(call, "/ping_authorized")
            .toServiceResult<String>()
            .map { true }

}

object LetterMarkupModule : SimpleModule() {
    private fun readResolve(): Any = LetterMarkupModule

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())
    }

    private fun blockDeserializer() =
        object : StdDeserializer<LetterMarkup.Block>(LetterMarkup.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.Block.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.Block.Type.TITLE1 -> LetterMarkup.Block.Title1::class.java
                    LetterMarkup.Block.Type.TITLE2 -> LetterMarkup.Block.Title2::class.java
                    LetterMarkup.Block.Type.PARAGRAPH -> LetterMarkup.Block.Paragraph::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun paragraphContentDeserializer() =
        object : StdDeserializer<LetterMarkup.ParagraphContent>(LetterMarkup.ParagraphContent::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.ParagraphContent {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.ParagraphContent.Type.ITEM_LIST -> LetterMarkup.ParagraphContent.ItemList::class.java
                    LetterMarkup.ParagraphContent.Type.LITERAL -> LetterMarkup.ParagraphContent.Text.Literal::class.java
                    LetterMarkup.ParagraphContent.Type.VARIABLE -> LetterMarkup.ParagraphContent.Text.Variable::class.java
                    LetterMarkup.ParagraphContent.Type.TABLE -> LetterMarkup.ParagraphContent.Table::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT -> LetterMarkup.ParagraphContent.Form.Text::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE -> LetterMarkup.ParagraphContent.Form.MultipleChoice::class.java
                    LetterMarkup.ParagraphContent.Type.NEW_LINE -> LetterMarkup.ParagraphContent.Text.NewLine::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun textContentDeserializer() =
        object : StdDeserializer<LetterMarkup.ParagraphContent.Text>(LetterMarkup.ParagraphContent.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.ParagraphContent.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val clazz = when (val contentType = LetterMarkup.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.ParagraphContent.Type.LITERAL -> LetterMarkup.ParagraphContent.Text.Literal::class.java
                    LetterMarkup.ParagraphContent.Type.VARIABLE -> LetterMarkup.ParagraphContent.Text.Variable::class.java
                    LetterMarkup.ParagraphContent.Type.NEW_LINE -> LetterMarkup.ParagraphContent.Text.NewLine::class.java
                    LetterMarkup.ParagraphContent.Type.TABLE,
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT,
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE,
                    LetterMarkup.ParagraphContent.Type.ITEM_LIST -> throw BrevbakerServiceException("$contentType is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, clazz)
            }
        }
}
