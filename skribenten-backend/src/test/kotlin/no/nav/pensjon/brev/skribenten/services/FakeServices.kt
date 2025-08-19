package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.PenService.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.services.SafService.HentDokumenterResponse
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification


open class FakeNavansattService(
    val harTilgangTilEnhet: Map<Pair<String, String>, Boolean> = emptyMap(),
    val navansatte: Map<String, String> = emptyMap(),
) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String) =
        ServiceResult.Ok(harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false))

    override suspend fun hentNavansatt(ansattId: String): Navansatt? = navansatte[ansattId]?.let {
        Navansatt(
            emptyList(),
            it,
            it.split(' ').first(),
            it.split(' ').last(),
        )
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<List<NAVAnsattEnhet>> = TODO("Not yet implemented")
}

open class FakeNorg2Service(val enheter: Map<String, NavEnhet> = mapOf()) : Norg2Service {
    override suspend fun getEnhet(enhetId: String) = enheter[enhetId]
}

open class FakeSamhandlerService(val navn: Map<String, String> = mapOf()) : SamhandlerService {
    override suspend fun hentSamhandlerNavn(idTSSEkstern: String) = navn[idTSSEkstern]
    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = TODO("Not yet implemented")
    override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto = TODO("Not yet implemented")
}

open class FakeBrevmetadataService(
    val eblanketter: List<BrevdataDto> = listOf(),
    val brevmaler: List<BrevdataDto> = listOf(),
    val maler: Map<String, BrevdataDto> = mapOf(),
) : BrevmetadataService {
    override suspend fun getBrevmalerForSakstype(sakstype: Sakstype) = brevmaler

    override suspend fun getEblanketter() = eblanketter

    override suspend fun getMal(brevkode: String) = maler[brevkode] ?: TODO("Not implemented")

}

open class FakeBrevbakerService(
    val maler: List<TemplateDescription.Redigerbar> = listOf(),
    val redigerbareMaler: Map<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mapOf(),
) : BrevbakerService {
    override suspend fun getTemplates() = ServiceResult.Ok(maler)

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]

    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> = TODO("Not yet implemented")
    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkup> = TODO("Not yet implemented")
    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse> = TODO("Not yet implemented")
}

private val objectMapper = jacksonObjectMapper()

fun <T> settOppHttpClient(body: T): HttpClient =
    HttpClient(MockEngine {
        respond(
            content = objectMapper.writeValueAsString(body),
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", "application/json")
        )

    }) {
        install(ContentNegotiation) {
            jackson {
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }
    }

open class PenServiceStub : PenService {
    override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> = TODO("Not implemented yet")
    override suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): ServiceResult<Pen.BestillDoksysBrevResponse> = TODO("Not implemented yet")
    override suspend fun bestillExstreamBrev(
        bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest,
    ): ServiceResult<BestillExstreamBrevResponse> = TODO("Not implemented yet")
    override suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): ServiceResult<Pen.RedigerDokumentResponse> = TODO("Not implemented yet")
    override suspend fun redigerExstreamBrev(journalpostId: String): ServiceResult<Pen.RedigerDokumentResponse> = TODO("Not implemented yet")
    override suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>> = TODO("Not implemented yet")
    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean> = TODO("Not implemented yet")
    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<KravStoettetAvDatabyggerResult> = TODO("Not implemented yet")
    override suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data> = TODO("Not implemented yet")
    override suspend fun sendbrev(
        sendRedigerbartBrevRequest: SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse> = TODO("Not implemented yet")
}


open class PdlServiceStub : PdlService {
    override suspend fun hentNavn(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<String> = TODO("Not yet implemented")
    override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<List<Pdl.Gradering>> = TODO("Not yet implemented")
}

open class SafServiceStub : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String): JournalpostLoadingResult = TODO("Not yet implemented")
    override suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult<HentDokumenterResponse> = TODO("Not yet implemented")
    override suspend fun hentPdfForJournalpostId(journalpostId: String): ServiceResult<ByteArray> = TODO("Not yet implemented")
}