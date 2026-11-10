package no.nav.pensjon.brev.skribenten.eksterntApi

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.runBlocking
import no.nav.brev.InternKonstruktoer
import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.*
import no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Adresselinje
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Navn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Poststed
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
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
        markupVersjon = MarkupVersjon.V1,
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
        val yaml = ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule.Builder().build())
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .readValue(yamlfil, Yamlstruktur::class.java)
        val brevinfo = yaml.components.schemas.brevinfo.properties

        val parameters = ExternalAPI.BrevInfo::class.primaryConstructor!!.parameters
        parameters.forEach {
            val forventaType = finnForventaType(it)
            val property = brevinfo[it.name]!!
            val typer = property.type.orEmpty()
            val erNullable = "null" in typer || property.oneOf.orEmpty().any { alternativ -> alternativ.type == listOf("null") }

            assertThat(typer.filterNot { type -> type == "null" }.singleOrNull()).`as`("typen til ${it.name}").isEqualTo(forventaType.first)
            assertThat(property.format).`as`("formatet til ${it.name}").isEqualTo(forventaType.second)
            assertThat(erNullable).`as`("nullable for ${it.name}").isEqualTo(it.type.isMarkedNullable)
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
                saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) },
                statiskFagsystemBrevdata = statiskFagsystemBrevdata,
            )
        )

        assertThat(mottattRequest?.statiskFagsystemBrevdata).isEqualTo(statiskFagsystemBrevdata)
    }

    @Test
    fun `statiskFagsystemBrevdata blir null naar baade statiskFagsystemBrevdata og saksbehandlerValg mangler`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }

        service.opprettBrev(lagOpprettBrevRequest(saksbehandlerValg = null, statiskFagsystemBrevdata = null))

        assertThat(mottattRequest?.statiskFagsystemBrevdata).isNull()
    }

    @Test
    fun `sender samhandler videre som mottaker`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }

        service.opprettBrev(lagOpprettBrevRequest(overstyrtMottaker = ExternalAPI.OverstyrtMottaker.Samhandler(TssId("samhandler1"))))

        assertThat(mottattRequest?.mottaker).isEqualTo(Dto.Mottaker.samhandler(TssId("samhandler1")))
    }

    @ParameterizedTest
    @EnumSource(Dto.Mottaker.ManueltAdressertTil::class)
    fun `bevarer adressefelter og adresseringsvalg ved opprettelse og henting`(adressertTil: Dto.Mottaker.ManueltAdressertTil): Unit = runBlocking {
        for ((ekstern, forventet) in adressepar(adressertTil)) {
            var mottattRequest: OpprettBrevHandler.Request? = null
            val service = lagExternalAPIService(brevInfo = brevDto.copy(mottaker = forventet)) { mottattRequest = it }

            service.opprettBrev(lagOpprettBrevRequest(overstyrtMottaker = ekstern))

            assertThat(mottattRequest?.mottaker).isEqualTo(forventet)
            assertThat(service.hentAlleBrevForSaker(setOf(saksId)).single().overstyrtMottaker).isEqualTo(ekstern)
        }
    }

    @Test
    fun `bruker BRUKER naar manueltAdressertTil er null`(): Unit = runBlocking {
        for ((ekstern, forventet) in adressepar(null)) {
            var mottattRequest: OpprettBrevHandler.Request? = null
            val service = lagExternalAPIService { mottattRequest = it }

            service.opprettBrev(lagOpprettBrevRequest(overstyrtMottaker = ekstern))

            assertThat(mottattRequest?.mottaker).isEqualTo(forventet)
        }
    }

    @Test
    fun `sender null mottaker naar overstyrtMottaker mangler`(): Unit = runBlocking {
        var mottattRequest: OpprettBrevHandler.Request? = null
        val service = lagExternalAPIService { mottattRequest = it }

        service.opprettBrev(lagOpprettBrevRequest())

        assertThat(mottattRequest).isNotNull()
        assertThat(mottattRequest?.mottaker).isNull()
    }

    @Test
    fun `nye mottakerfelter er valgfrie og matcher openapi`() {
        val yamlfil = Files.readString(Paths.get("src/main/resources/openapi/external-api.yaml")).replace($$"$ref", "ref")
        val schemas = ObjectMapper(YAMLFactory())
            .registerModule(KotlinModule.Builder().build())
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .readValue(yamlfil, Yamlstruktur::class.java).components.schemas
        val request = schemas.opprettBrevRequest
        val mottaker = request.properties.getValue("overstyrtMottaker")

        assertThat(request.required).doesNotContain("overstyrtMottaker")
        assertThat(mottaker.oneOf!!.mapNotNull { it.ref }).containsExactly("#/components/schemas/mottaker")
        assertThat(mottaker.oneOf.any { it.type == listOf("null") }).isTrue()
        assertThat(ExternalAPI.OpprettBrevRequest::class.primaryConstructor!!.parameters.single { it.name == "overstyrtMottaker" }.isOptional).isTrue()

        val adressemodeller = listOf(ExternalAPI.OverstyrtMottaker.NorskAdresse::class, ExternalAPI.OverstyrtMottaker.UtenlandskAdresse::class)
        val adresser = schemas.mottaker.oneOf.filter { it.properties.getValue("type").enum!!.single() in adressemodeller.map { modell -> modell.simpleName } }
        assertThat(adresser).hasSize(2)
        adresser.forEach { adresse ->
            val adressering = adresse.properties.getValue("manueltAdressertTil")
            assertThat(adresse.required).doesNotContain("manueltAdressertTil")
            assertThat(adressering.type).containsExactly("string", "null")
            assertThat(adressering.enum).containsExactlyElementsOf(Dto.Mottaker.ManueltAdressertTil.entries.map { it.name } + null)
            assertThat(adressering.default).isEqualTo("BRUKER")
        }
        adressemodeller.forEach { modell ->
            val parameter = modell.primaryConstructor!!.parameters.single { it.name == "manueltAdressertTil" }
            assertThat(parameter.isOptional).isTrue()
            assertThat(parameter.type.isMarkedNullable).isTrue()
        }
    }

    private fun adressepar(adressertTil: Dto.Mottaker.ManueltAdressertTil?): List<Pair<ExternalAPI.OverstyrtMottaker, Dto.Mottaker>> {
        val navn = Navn("Test Mottaker")
        val linje1 = Adresselinje("Adresse 1")
        val linje2 = Adresselinje("Adresse 2")
        val linje3 = Adresselinje("Adresse 3")
        return listOf(
            ExternalAPI.OverstyrtMottaker.NorskAdresse(navn, NorskPostnummer("0001"), Poststed("Oslo"), linje1, linje2, linje3, adressertTil) to
                Dto.Mottaker.norskAdresse(navn, NorskPostnummer("0001"), Poststed("Oslo"), linje1, linje2, linje3, adressertTil ?: Dto.Mottaker.ManueltAdressertTil.BRUKER),
            ExternalAPI.OverstyrtMottaker.UtenlandskAdresse(navn, linje1, linje2, linje3, Landkode("SE"), adressertTil) to
                Dto.Mottaker.utenlandskAdresse(navn, linje1, linje2, linje3, Landkode("SE"), adressertTil ?: Dto.Mottaker.ManueltAdressertTil.BRUKER),
        )
    }

    private fun lagExternalAPIService(
        brevInfo: Dto.BrevInfo = brevDto,
        onOpprettBrev: (OpprettBrevHandler.Request) -> Unit = {},
    ) = ExternalAPIService(
        config = ExternalApiConfig(skribentenWebUrl = skribentenWebUrl),
        hentBrevForAlleSaker = { Outcome.success(listOf(brevInfo)) },
        brevmalService = BrevmalService(
            brevbakerService = FakeBrevbakerService(redigerbareMaler = mutableMapOf(Testbrevkoder.INFORMASJONSBREV to brevmal)),
            penClient = PenClientStub(),
            brevmetadataService = FakeBrevmetadataService(),
        ),
        opprettBrevHandler = { request ->
            onOpprettBrev(request)
            Outcome.failure(BrevmalFinnesIkke(request.brevkode))
        },
    )

    private fun lagOpprettBrevRequest(
        saksbehandlerValg: SaksbehandlervalgMap? = null,
        statiskFagsystemBrevdata: Api.GeneriskBrevdata? = null,
        overstyrtMottaker: ExternalAPI.OverstyrtMottaker? = null,
    ) = ExternalAPI.OpprettBrevRequest(
        saksId = saksId,
        brevkode = Testbrevkoder.INFORMASJONSBREV,
        spraak = SpraakKode.NB,
        avsenderEnhetsId = EnhetId("0001"),
        saksbehandlerValg = saksbehandlerValg,
        statiskFagsystemBrevdata = statiskFagsystemBrevdata,
        reserverForRedigering = null,
        vedtaksId = null,
        overstyrtMottaker = overstyrtMottaker,
    )

    private fun finnForventaType(parameter: KParameter): Pair<String?, String?> = when (parameter.type.classifier as KClass<*>) {
        Int::class, Long::class -> Pair("integer", null)
        String::class -> Pair("string", if (parameter.name == "url") "uri" else null)
        NavIdent::class -> Pair("string", null)
        BrevId::class, SaksId::class, VedtaksId::class, JournalpostId::class -> Pair("integer", "int64")
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

            data class Property(
                val type: List<String>?,
                val format: String? = null,
                val description: String?,
                val enum: List<String?>?,
                val ref: String?,
                val additionalProperties: Map<String, String>?,
                val oneOf: List<Property>?,
                val minLength: Int?,
                val maxLength: Int?,
                val default: Any?,
            )
        }

        data class SecurityScheme(val type: String, val scheme: String, val bearerFormat: String, val description: String)
    }
}
