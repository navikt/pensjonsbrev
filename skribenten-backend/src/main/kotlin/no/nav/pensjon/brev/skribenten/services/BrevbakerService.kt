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
                registerModule(RenderedJsonLetterModule)
            }
        }
    }

    suspend fun getTemplate(call: ApplicationCall, brevkode: Brevkode.Redigerbar): ServiceResult<String> =
        client.get(call, "/templates/redigerbar/${brevkode.name}").toServiceResult()

    suspend fun renderLetter(call: ApplicationCall, brevkode: Brevkode.Redigerbar, brevdata: BrevbakerBrevdata): ServiceResult<RenderedJsonLetter> =
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

object RenderedJsonLetterModule : SimpleModule() {
    private fun readResolve(): Any = RenderedJsonLetterModule

    init {
        addDeserializer(RenderedJsonLetter.Block::class.java, blockDeserializer())
        addDeserializer(RenderedJsonLetter.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(RenderedJsonLetter.ParagraphContent.Text::class.java, textContentDeserializer())
    }

    private fun blockDeserializer() =
        object : StdDeserializer<RenderedJsonLetter.Block>(RenderedJsonLetter.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedJsonLetter.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (RenderedJsonLetter.Block.Type.valueOf(node.get("type").textValue())) {
                    RenderedJsonLetter.Block.Type.TITLE1 -> RenderedJsonLetter.Block.Title1::class.java
                    RenderedJsonLetter.Block.Type.TITLE2 -> RenderedJsonLetter.Block.Title2::class.java
                    RenderedJsonLetter.Block.Type.PARAGRAPH -> RenderedJsonLetter.Block.Paragraph::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun paragraphContentDeserializer() =
        object : StdDeserializer<RenderedJsonLetter.ParagraphContent>(RenderedJsonLetter.ParagraphContent::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedJsonLetter.ParagraphContent {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (RenderedJsonLetter.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    RenderedJsonLetter.ParagraphContent.Type.ITEM_LIST -> RenderedJsonLetter.ParagraphContent.ItemList::class.java
                    RenderedJsonLetter.ParagraphContent.Type.LITERAL -> RenderedJsonLetter.ParagraphContent.Text.Literal::class.java
                    RenderedJsonLetter.ParagraphContent.Type.VARIABLE -> RenderedJsonLetter.ParagraphContent.Text.Variable::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private fun textContentDeserializer() =
        object : StdDeserializer<RenderedJsonLetter.ParagraphContent.Text>(RenderedJsonLetter.ParagraphContent.Text::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RenderedJsonLetter.ParagraphContent.Text {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (RenderedJsonLetter.ParagraphContent.Type.valueOf(node.get("type").textValue())) {
                    RenderedJsonLetter.ParagraphContent.Type.LITERAL -> RenderedJsonLetter.ParagraphContent.Text.Literal::class.java
                    RenderedJsonLetter.ParagraphContent.Type.VARIABLE -> RenderedJsonLetter.ParagraphContent.Text.Variable::class.java
                    RenderedJsonLetter.ParagraphContent.Type.ITEM_LIST -> throw BrevbakerServiceException("ITEM_LIST is not allowed in a text-only block.")
                }
                return p.codec.treeToValue(node, type)
            }
        }
}
