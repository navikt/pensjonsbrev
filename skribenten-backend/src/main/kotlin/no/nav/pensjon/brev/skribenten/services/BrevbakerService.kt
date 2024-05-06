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
import java.time.*

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
                registerModule(RenderedLetterMarkdownModule)
            }
        }
    }

    suspend fun getTemplate(call: ApplicationCall, brevkode: Brevkode.Redigerbar): ServiceResult<String> =
        client.get(call, "/templates/redigerbar/${brevkode.name}").toServiceResult()

    suspend fun renderLetter(call: ApplicationCall, brevkode: Brevkode.Redigerbar, brevdata: BrevbakerBrevdata): ServiceResult<RenderedLetterMarkdown> =
        client.post(call, "/letter/redigerbar") {
            contentType(ContentType.Application.Json)
            setBody(
                RedigerbartbrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = Felles(
                        dokumentDato = LocalDate.now(),
                        saksnummer = "1234",
                        avsenderEnhet = NAVEnhet("nav.no", "NAV Familie- og pensjonsytelser Porsgrunn", Telefonnummer("22225555")),
                        bruker = Bruker(Foedselsnummer("12345678910"), "Test", null, "Testeson"),
                        vergeNavn = null,
                        signerendeSaksbehandlere = SignerendeSaksbehandlere("Ole Saksbehandler")
                    ),
                    language = LanguageCode.BOKMAL,
                )
            )
        }.toServiceResult()

    override val name = "Brevbaker"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.get(call, "/ping_authorized")
            .toServiceResult<String>()
            .map { true }

}

object RenderedLetterMarkdownModule : SimpleModule() {
    private fun readResolve(): Any = RenderedLetterMarkdownModule

    init {
        addDeserializer(RenderedLetterMarkdown.Block::class.java, blockDeserializer())
        addDeserializer(RenderedLetterMarkdown.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(RenderedLetterMarkdown.ParagraphContent.Text::class.java, textContentDeserializer())
    }

    private fun blockDeserializer() =
        object : StdDeserializer<RenderedLetterMarkdown.Block>(RenderedLetterMarkdown.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedLetterMarkdown.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (RenderedLetterMarkdown.Block.Type.valueOf(node.get("type").textValue())) {
                    RenderedLetterMarkdown.Block.Type.TITLE1 -> RenderedLetterMarkdown.Block.Title1::class.java
                    RenderedLetterMarkdown.Block.Type.TITLE2 -> RenderedLetterMarkdown.Block.Title2::class.java
                    RenderedLetterMarkdown.Block.Type.PARAGRAPH -> RenderedLetterMarkdown.Block.Paragraph::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun paragraphContentDeserializer() =
        object : StdDeserializer<RenderedLetterMarkdown.ParagraphContent>(RenderedLetterMarkdown.ParagraphContent::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedLetterMarkdown.ParagraphContent {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (RenderedLetterMarkdown.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    RenderedLetterMarkdown.ParagraphContent.Type.ITEM_LIST -> RenderedLetterMarkdown.ParagraphContent.ItemList::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.LITERAL -> RenderedLetterMarkdown.ParagraphContent.Text.Literal::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.VARIABLE -> RenderedLetterMarkdown.ParagraphContent.Text.Variable::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.TABLE -> RenderedLetterMarkdown.ParagraphContent.Table::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.FORM_TEXT -> RenderedLetterMarkdown.ParagraphContent.Form.Text::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.FORM_CHOICE -> RenderedLetterMarkdown.ParagraphContent.Form.MultipleChoice::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.NEW_LINE -> RenderedLetterMarkdown.ParagraphContent.Text.NewLine::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun textContentDeserializer() =
        object : StdDeserializer<RenderedLetterMarkdown.ParagraphContent.Text>(RenderedLetterMarkdown.ParagraphContent.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedLetterMarkdown.ParagraphContent.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val clazz = when (val contentType = RenderedLetterMarkdown.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    RenderedLetterMarkdown.ParagraphContent.Type.LITERAL -> RenderedLetterMarkdown.ParagraphContent.Text.Literal::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.VARIABLE -> RenderedLetterMarkdown.ParagraphContent.Text.Variable::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.NEW_LINE -> RenderedLetterMarkdown.ParagraphContent.Text.NewLine::class.java
                    RenderedLetterMarkdown.ParagraphContent.Type.TABLE,
                    RenderedLetterMarkdown.ParagraphContent.Type.FORM_TEXT,
                    RenderedLetterMarkdown.ParagraphContent.Type.FORM_CHOICE,
                    RenderedLetterMarkdown.ParagraphContent.Type.ITEM_LIST -> throw BrevbakerServiceException("$contentType is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, clazz)
            }
        }
}
