package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.slf4j.LoggerFactory

class BrevbakerServiceException(msg: String) : Exception(msg)

class BrevbakerService(config: Config, authService: AzureADService) : ServiceStatus {
    private val logger = LoggerFactory.getLogger(BrevredigeringService::class.java)!!

    private val brevbakerUrl = config.getString("url")
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(brevbakerUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                registerModule(LetterMarkupModule)
                registerModule(TemplateModelSpecificationModule)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        callIdAndOnBehalfOfClient(config.getString("scope"), authService)
    }

    /**
     * Get model specification for a template.
     */
    suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> =
        client.get("/templates/redigerbar/${brevkode.kode()}/modelSpecification").toServiceResult()

    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkup> =
        client.post("/letter/redigerbar/markup") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                )
            )
        }.toServiceResult()

    suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse> =
        client.post("/letter/redigerbar/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillRedigertBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                    letterMarkup = redigertBrev
                )
            )
        }.toServiceResult()

    suspend fun getTemplates(): ServiceResult<List<TemplateDescription.Redigerbar>> =
        client.get("/templates/redigerbar") {
            url {
                parameters.append("includeMetadata", "true")
            }
        }.toServiceResult()

    private val templateCache = Cache<Brevkode.Redigerbart, TemplateDescription.Redigerbar>()
    suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar? =
        templateCache.cached(brevkode) {
            client.get("/templates/redigerbar/${brevkode.kode()}").toServiceResult<TemplateDescription.Redigerbar>()
                .onError { error, statusCode -> logger.error("Feilet ved henting av templateDescription for $brevkode: $statusCode - $error") }
                .resultOrNull()
        }

    override val name = "Brevbaker"
    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/ping_authorized")
            .toServiceResult<String>()
            .map { true }

}

@OptIn(InterneDataklasser::class)
object LetterMarkupModule : SimpleModule() {
    private fun readResolve(): Any = LetterMarkupModule

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())

        addDeserializer(LetterMarkup.Sakspart::class.java, object :
            AbstractDeserializer<LetterMarkup.Sakspart, LetterMarkupImpl.SakspartImpl>(LetterMarkupImpl.SakspartImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.Signatur::class.java, object :
            AbstractDeserializer<LetterMarkup.Signatur, LetterMarkupImpl.SignaturImpl>(LetterMarkupImpl.SignaturImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.ItemList, ParagraphContentImpl.ItemListImpl>(
                ParagraphContentImpl.ItemListImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList.Item::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.ItemList.Item, ParagraphContentImpl.ItemListImpl.ItemImpl>(
                ParagraphContentImpl.ItemListImpl.ItemImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Literal::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Text.Literal, ParagraphContentImpl.TextImpl.LiteralImpl>(ParagraphContentImpl.TextImpl.LiteralImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Variable::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Text.Variable, ParagraphContentImpl.TextImpl.VariableImpl>(ParagraphContentImpl.TextImpl.VariableImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.NewLine::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Text.NewLine, ParagraphContentImpl.TextImpl.NewLineImpl>(ParagraphContentImpl.TextImpl.NewLineImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.Attachment::class.java, object :
            AbstractDeserializer<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>(LetterMarkupImpl.AttachmentImpl::class.java) {}
        )


        addDeserializer(LetterMarkup.ParagraphContent.Table::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Table, ParagraphContentImpl.TableImpl>(ParagraphContentImpl.TableImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Row::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Table.Row, ParagraphContentImpl.TableImpl.RowImpl>(ParagraphContentImpl.TableImpl.RowImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Cell::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Table.Cell, ParagraphContentImpl.TableImpl.CellImpl>(ParagraphContentImpl.TableImpl.CellImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Header::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Table.Header, ParagraphContentImpl.TableImpl.HeaderImpl>(ParagraphContentImpl.TableImpl.HeaderImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.ColumnSpec::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Table.ColumnSpec, ParagraphContentImpl.TableImpl.ColumnSpecImpl>(ParagraphContentImpl.TableImpl.ColumnSpecImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice::class.java, object :
            AbstractDeserializer<LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice, ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl>(ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl::class.java) {}
        )

        addDeserializer(LetterMarkup::class.java, object :
            AbstractDeserializer<LetterMarkup, LetterMarkupImpl>(LetterMarkupImpl::class.java) {}
        )
    }

    private fun blockDeserializer() =
        object : StdDeserializer<LetterMarkup.Block>(LetterMarkup.Block::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LetterMarkup.Block {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (LetterMarkup.Block.Type.valueOf(node.get("type").textValue())) {
                    LetterMarkup.Block.Type.TITLE1 -> LetterMarkupImpl.BlockImpl.Title1Impl::class.java
                    LetterMarkup.Block.Type.TITLE2 -> LetterMarkupImpl.BlockImpl.Title2Impl::class.java
                    LetterMarkup.Block.Type.PARAGRAPH -> LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java
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
                    LetterMarkup.ParagraphContent.Type.FORM_TEXT -> ParagraphContentImpl.Form.TextImpl::class.java
                    LetterMarkup.ParagraphContent.Type.FORM_CHOICE -> ParagraphContentImpl.Form.MultipleChoiceImpl::class.java
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

    private abstract class AbstractDeserializer<T, V : T>(private val v: Class<V>) : JsonDeserializer<T>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T =
            parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), v)
    }
}

object TemplateModelSpecificationModule : SimpleModule() {
    private fun readResolve(): Any = TemplateModelSpecificationModule

    init {
        addDeserializer(FieldType::class.java, fieldTypeDeserializer())
    }

    private fun fieldTypeDeserializer() =
        object : StdDeserializer<FieldType>(FieldType::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): FieldType {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (FieldType.Type.valueOf(node.get("type").textValue())) {
                    FieldType.Type.array -> FieldType.Array::class.java
                    FieldType.Type.scalar -> FieldType.Scalar::class.java
                    FieldType.Type.enum -> FieldType.Enum::class.java
                    FieldType.Type.`object` -> FieldType.Object::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

}