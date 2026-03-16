package no.nav.pensjon.brev.skribenten.eksterntApi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.typesafe.config.ConfigValueFactory
import kotlinx.coroutines.runBlocking
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.application.HentBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.application.OpprettBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandlerImpl
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.FakeBrevbakerService
import no.nav.pensjon.brev.skribenten.services.FakeBrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

@OptIn(InternKonstruktoer::class)
class ExternalAPIServiceTest {

    private val skribentenWebUrl = "https://our-cool-url"
    val saksId = SaksId(1L)
    val brevDto = Dto.BrevInfo(
        id = BrevId(214L),
        saksId = saksId,
        vedtaksId = null,
        opprettetAv = NavIdent("Sakson"),
        opprettet = Instant.now(),
        sistredigertAv = NavIdent("Sakson"),
        sistredigert = Instant.now(),
        redigeresAv = null,
        sistReservert = null,
        brevkode = Testbrevkoder.INFORMASJONSBREV,
        laastForRedigering = false,
        distribusjonstype = Distribusjonstype.SENTRALPRINT,
        mottaker = null,
        avsenderEnhetId = EnhetId("0001"),
        spraak = LanguageCode.BOKMAL,
        journalpostId = null,
        attestertAv = null,
        status = Dto.BrevStatus.KLADD
    )
    val brevmal = TemplateDescription.Redigerbar(
        name = Testbrevkoder.INFORMASJONSBREV.kode(),
        letterDataClass = "a.class",
        languages = listOf(),
        metadata = LetterMetadata(
            "Informasjonsbrev",
            LetterMetadata.Distribusjonstype.ANNET,
            LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
        kategori = TemplateDescription.Redigerbar.Brevkategori("INFORMASJONSBREV"),
        brevkontekst = TemplateDescription.Brevkontekst.SAK,
        sakstyper = emptySet(),
    )
    private val externalAPIService = ExternalAPIService(
        config = ConfigValueFactory.fromMap(mapOf("skribentenWebUrl" to skribentenWebUrl)).toConfig(),
        hentBrevService = object : HentBrevService {
            override fun hentBrevForAlleSaker(saksIder: Set<SaksId>) = listOf(brevDto)
        },
        brevmalService = BrevmalService(
            brevbakerService = FakeBrevbakerService(redigerbareMaler = mutableMapOf(Testbrevkoder.INFORMASJONSBREV to brevmal)),
            penClient = PenClientStub(),
            brevmetadataService = FakeBrevmetadataService(),
        ),
        opprettBrevService = object : OpprettBrevService {
            override suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request): Outcome<Dto.Brevredigering, BrevredigeringError> = Outcome.success(Dto.Brevredigering(
                info = brevDto,
                redigertBrev = TODO(),
                redigertBrevHash = TODO(),
                saksbehandlerValg = TODO(),
                propertyUsage = TODO(),
                valgteVedlegg = TODO()
            ))
        }
    )


    @Test
    fun `legger til url for aa aapne brev i skribenten`(): Unit = runBlocking {
        val brev = externalAPIService.hentAlleBrevForSaker(setOf(saksId)).single()
        Assertions.assertThat(brev.url).startsWith(skribentenWebUrl).endsWith("/214")
    }

    @Test
    fun `modellen som blir returnert matcher med modellen i openapi-deklarasjonen`() {
        val yamlfil = Files.readAllLines(Paths.get("src/main/resources/openapi/external-api.yaml")).joinToString(System.lineSeparator()).replace($$"$ref", "ref")
        val yaml = ObjectMapper(YAMLFactory()).registerModule(KotlinModule.Builder().build()).readValue(yamlfil, Yamlstruktur::class.java)
        val brevinfo = yaml.components.schemas.brevinfo.properties

        val parameters = ExternalAPI.BrevInfo::class.primaryConstructor!!.parameters
        parameters.forEach {
            val forventaType = finnForventaType(it)

            assertThat(brevinfo[it.name]!!.type).isEqualTo(forventaType.first)
            assertThat(brevinfo[it.name]!!.format).isEqualTo(forventaType.second)
        }
        assertThat(parameters.size).isEqualTo(brevinfo.size)
    }

    private fun finnForventaType(parameter: KParameter): Pair<String?, String?> = when (parameter.type.classifier as KClass<*>) {
        Int::class, Long::class -> Pair("number", null)
        String::class -> Pair("string", if (parameter.name == "url") "uri" else null)
        NavIdent::class -> Pair("string", null)
        BrevId::class, SaksId::class, VedtaksId::class, JournalpostId::class -> Pair("number", "int64")
        EnhetId::class -> Pair("string", null)
        SpraakKode::class -> Pair("string", null)
        Brevkode.Redigerbart::class, LetterMetadata.Brevtype::class -> Pair("string", null)
        Instant::class -> Pair("string", "date-time")
        ExternalAPI.OverstyrtMottaker::class -> Pair(null, null)
        ExternalAPI.BrevStatus::class -> Pair("string", null)
        else -> Pair(null, null)
    }
}

class Yamlstruktur(
    val openapi: String,
    val info: Info,
    val servers: List<Server>,
    val paths: Map<String, Map<String, Path>>,
    val components: Components,
) {
    data class Info(val title: String, val description: String, val version: String)
    data class Server(val url: String, val description: String)
    data class Path(val description: String, val security: List<Any>, val parameters: List<Map<String, Any>>?, val requestBody: Any?/*TODO*/, val responses: Map<String, Response>) {
        data class Response(val description: String, val content: Map<String, Map<String, Content>>) {
            data class Content(val type: String?, val items: Map<String, Any>?, val ref: String?)
        }
    }
    data class Components(
        val schemas: Schemas,
        val securitySchemes: Map<String, SecurityScheme>,
    ) {
        data class Schemas(val brevinfo: Brevinfo, val mottaker: Mottaker, val opprettetBrev: OpprettetBrev, val opprettBrevRequest: OpprettBrevRequest) {
            data class Brevinfo(val type: String, val required: List<String>, val properties: Map<String, Property>)
            data class Mottaker(val oneOf: List<OneOfMottaker>) {
                data class OneOfMottaker(val type: String, val description: String, val required: List<String>, val properties: Map<String, Property>)
            }
            data class OpprettetBrev(val type: String, val required: List<String>, val properties: Map<String, Property>)
            data class OpprettBrevRequest(val type: String, val required: List<String>, val properties: Map<String, Property>)

            data class Property(val type: String?, val format: String? = null, val description: String?, val enum: List<String>?, val ref: String?, val additionalProperties: Map<String, String>?)
        }

        data class SecurityScheme(val type: String, val scheme: String, val bearerFormat: String, val description: String)
    }
}