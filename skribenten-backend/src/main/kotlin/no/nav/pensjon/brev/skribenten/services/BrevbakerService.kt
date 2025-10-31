package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.io.EOFException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.*
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class BrevbakerServiceException(msg: String) : Exception(msg)

interface BrevbakerService {
    suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification>
    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkupWithDataUsage>
    suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse>
    suspend fun getTemplates(): ServiceResult<List<TemplateDescription.Redigerbar>>
    suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar?
}

class BrevbakerServiceHttp(config: Config, authService: AuthService, val cache: Cache) : BrevbakerService, ServiceStatus {
    private val logger = LoggerFactory.getLogger(BrevredigeringService::class.java)!!

    private val brevbakerUrl = config.getString("url")
    private val scope = config.getString("scope")
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(brevbakerUrl)
        }
        installRetry(logger, shouldNotRetry = { method, url, cause -> method == HttpMethod.Post && url.segments.last() == "pdf" && cause?.unwrapCancellationException() !is EOFException })
        engine {
            requestTimeout = 60.seconds.inWholeMilliseconds
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                registerModule(LetterMarkupModule)
                registerModule(TemplateModelSpecificationModule)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    /**
     * Get model specification for a template.
     */
    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> =
        client.get("/templates/redigerbar/${brevkode.kode()}/modelSpecification").toServiceResult()

    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkupWithDataUsage> =
        client.post("/letter/redigerbar/markup-usage") {
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

    override suspend fun renderPdf(
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

    override suspend fun getTemplates(): ServiceResult<List<TemplateDescription.Redigerbar>> =
        client.get("/templates/redigerbar") {
            url {
                parameters.append("includeMetadata", "true")
            }
        }.toServiceResult()

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar? =
        cache.cached(Cacheomraade.REDIGERBAR_MAL, brevkode) {
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
internal object LetterMarkupModule : SimpleModule() {
    @Suppress("unused")
    private fun readResolve(): Any = LetterMarkupModule

    init {
        addDeserializer(LetterMarkup.Block::class.java, blockDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent::class.java, paragraphContentDeserializer())
        addDeserializer(LetterMarkup.ParagraphContent.Text::class.java, textContentDeserializer())

        addAbstractTypeMapping<LetterMarkup.Sakspart, SakspartImpl>()
        addAbstractTypeMapping<LetterMarkup.Signatur, SignaturImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList, ParagraphContentImpl.ItemListImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.ItemList.Item, ParagraphContentImpl.ItemListImpl.ItemImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Literal, ParagraphContentImpl.TextImpl.LiteralImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.Variable, ParagraphContentImpl.TextImpl.VariableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Text.NewLine, ParagraphContentImpl.TextImpl.NewLineImpl>()
        addAbstractTypeMapping<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table, ParagraphContentImpl.TableImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Row, ParagraphContentImpl.TableImpl.RowImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Cell, ParagraphContentImpl.TableImpl.CellImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.Header, ParagraphContentImpl.TableImpl.HeaderImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Table.ColumnSpec, ParagraphContentImpl.TableImpl.ColumnSpecImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice.Choice, ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.MultipleChoice, ParagraphContentImpl.Form.MultipleChoiceImpl>()
        addAbstractTypeMapping<LetterMarkup.ParagraphContent.Form.Text, ParagraphContentImpl.Form.TextImpl>()
        addAbstractTypeMapping<LetterMarkup, LetterMarkupImpl>()
        addAbstractTypeMapping<LetterMarkupWithDataUsage, LetterMarkupWithDataUsageImpl>()
        addAbstractTypeMapping<LetterMarkupWithDataUsage.Property, LetterMarkupWithDataUsageImpl.PropertyImpl>()
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
}

object TemplateModelSpecificationModule : SimpleModule() {
    // Kreves pga Serializable
    @Suppress("unused")
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