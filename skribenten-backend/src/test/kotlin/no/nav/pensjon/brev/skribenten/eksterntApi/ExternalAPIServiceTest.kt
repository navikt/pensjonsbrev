package no.nav.pensjon.brev.skribenten.eksterntApi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.runBlocking
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.*
import no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.*
import java.time.Instant
import kotlin.reflect.*
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
        distribusjonstype = Distribusjon.SENTRALPRINT,
        mottaker = null,
        avsenderEnhetId = EnhetId("0001"),
        spraak = LanguageCode.BOKMAL,
        journalpostId = null,
        attestertAv = null,
        status = Dto.BrevStatus.KLADD,
        leggVedFoersteside = false,
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
        config = ExternalApiConfig(skribentenWebUrl = skribentenWebUrl),
        hentBrevForAlleSaker = { Outcome.success(listOf(brevDto)) },
        brevmalService = BrevmalService(
            brevbakerService = FakeBrevbakerService(redigerbareMaler = mutableMapOf(Testbrevkoder.INFORMASJONSBREV to brevmal)),
            penClient = PenClientStub(),
            brevmetadataService = FakeBrevmetadataService(),
        ),
        opprettBrevHandler = {
            Outcome.success(Dto.Brevredigering(
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

            assertThat(brevinfo[it.name]!!.type).`as`("typen til ${it.name}").isEqualTo(forventaType.first)
            assertThat(brevinfo[it.name]!!.format).`as`("formatet til ${it.name}").isEqualTo(forventaType.second)
        }
        assertThat(parameters.size).isEqualTo(brevinfo.size)
    }

    @Test
    fun `sender statiskFagsystemBrevdata videre til OpprettBrevHandler naar det er oppgitt i requesten`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }
        val statiskFagsystemBrevdata = Api.GeneriskBrevdata().apply { put("fraFagsystem", "ja") }

        service.opprettBrev(
            lagOpprettBrevRequest(
                saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", true) },
                statiskFagsystemBrevdata = statiskFagsystemBrevdata,
            )
        )

        assertThat(mottattRequest?.statiskFagsystemBrevdata).isEqualTo(statiskFagsystemBrevdata)
    }

    @Test
    fun `konverterer saksbehandlerValg til statiskFagsystemBrevdata naar statiskFagsystemBrevdata mangler`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", true); put("valg2", "tekst") }

        service.opprettBrev(lagOpprettBrevRequest(saksbehandlerValg = saksbehandlerValg, statiskFagsystemBrevdata = null))

        assertThat(mottattRequest?.statiskFagsystemBrevdata).containsAllEntriesOf(saksbehandlerValg)
    }

    @Test
    fun `statiskFagsystemBrevdata blir null naar baade statiskFagsystemBrevdata og saksbehandlerValg mangler`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }

        service.opprettBrev(lagOpprettBrevRequest(saksbehandlerValg = null, statiskFagsystemBrevdata = null))

        assertThat(mottattRequest?.statiskFagsystemBrevdata).isNull()
    }

    private fun lagExternalAPIService(onOpprettBrev: (OpprettBrevHandler.Request) -> Unit) = ExternalAPIService(
        config = ExternalApiConfig(skribentenWebUrl = skribentenWebUrl),
        hentBrevForAlleSaker = { null },
        brevmalService = BrevmalService(
            brevbakerService = FakeBrevbakerService(),
            penClient = PenClientStub(),
            brevmetadataService = FakeBrevmetadataService(),
        ),
        opprettBrevHandler = { request ->
            onOpprettBrev(request)
            Outcome.failure(BrevmalFinnesIkke(request.brevkode))
        },
    )

    private fun lagOpprettBrevRequest(
        saksbehandlerValg: SaksbehandlervalgMap?,
        statiskFagsystemBrevdata: Api.GeneriskBrevdata?,
    ) = ExternalAPI.OpprettBrevRequest(
        saksId = saksId,
        brevkode = Testbrevkoder.INFORMASJONSBREV,
        spraak = SpraakKode.NB,
        avsenderEnhetsId = EnhetId("0001"),
        saksbehandlerValg = saksbehandlerValg,
        statiskFagsystemBrevdata = statiskFagsystemBrevdata,
        reserverForRedigering = null,
        vedtaksId = null,
    )

    private fun finnForventaType(parameter: KParameter): Pair<String?, String?> = when (parameter.type.classifier as KClass<*>) {
        Int::class, Long::class -> Pair("number", null)
        String::class -> Pair("string", if (parameter.name == "url") "uri" else null)
        NavIdent::class -> Pair("string", null)
        BrevId::class, SaksId::class, VedtaksId::class, JournalpostId::class -> Pair("number", "int64")
        EnhetId::class -> Pair("string", null)
        SpraakKode::class -> Pair("string", null)
        RedigerbarBrevkode::class, LetterMetadata.Brevtype::class -> Pair("string", null)
        Instant::class -> Pair("string", "date-time")
        ExternalAPI.OverstyrtMottaker::class -> Pair(null, null)
        ExternalAPI.BrevStatus::class -> Pair("string", null)
        else -> throw IllegalArgumentException("testen mangler definisjon av forventet type for: ${(parameter.type.classifier as KClass<*>).qualifiedName}")
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