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
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification


class FakeSafService(val journalpost: Pair<String, List<String>>?) : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String) = JournalpostLoadingResult.READY
    override suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult.Ok<SafService.HentDokumenterResponse> =
        journalpost?.let {
            SafService.HentDokumenterResponse.Journalpost(
                it.first, it.second.map { dok ->
                    SafService.HentDokumenterResponse.Dokument(dok)
                }
            )
        }?.let {
            ServiceResult.Ok(
                SafService.HentDokumenterResponse(
                    SafService.HentDokumenterResponse.Journalposter(
                        it
                    ), null
                )
            )
        } ?: TODO("Not implemented")

    override suspend fun hentPdfForJournalpostId(journalpostId: String) = TODO("Not yet implemented")
}

class FakeNavansattService(val harTilgangTilEnhet: Map<Pair<String, String>, Boolean> = emptyMap(), val navansatte: Map<String, String> = emptyMap()) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String) = ServiceResult.Ok(harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false))

    override suspend fun hentNavansatt(ansattId: String): Navansatt? = navansatte[ansattId]?.let { Navansatt(
            emptyList(),
            it,
            it.split(' ').first(),
            it.split(' ').last(),
        )
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<List<NAVAnsattEnhet>> {
        TODO("Not yet implemented")
    }

}

class FakeNorg2Service(val enheter: Map<String, NavEnhet> = mapOf()) : Norg2Service {
    override suspend fun getEnhet(enhetId: String) = enheter[enhetId]
}

class FakeSamhandlerService(val navn: Map<String, String> = mapOf()): SamhandlerService {
    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun hentSamhandlerNavn(idTSSEkstern: String) = navn[idTSSEkstern]

}

class FakePenService(
    val saker: Map<String, Pen.SakSelection> = mapOf(),
    val journalpostId: String? = null,
    val redigerDoksys: Map<Pair<String, String>, String> = emptyMap(),
    val redigerExstream: Map<String, String> = emptyMap(),
    val kravPaaGammeltRegelverk: Map<String, Boolean> = emptyMap(),
    val kravStoettetAvDatabygger: Map<String, PenService.KravStoettetAvDatabyggerResult> = emptyMap(),
) : PenService {
    override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> = saker[saksId]?.let { ServiceResult.Ok(it) } ?: TODO("Not implemented")

    override suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long,
    ): ServiceResult<Pen.BestillDoksysBrevResponse> = ServiceResult.Ok(Pen.BestillDoksysBrevResponse(journalpostId))

    override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest) =
        ServiceResult.Ok(Pen.BestillExstreamBrevResponse(journalpostId!!))

    override suspend fun redigerDoksysBrev(
        journalpostId: String,
        dokumentId: String,
    ) = redigerDoksys[Pair(journalpostId, dokumentId)]?.let { ServiceResult.Ok(Pen.RedigerDokumentResponse(it)) }
        ?: TODO("Not implemented")

    override suspend fun redigerExstreamBrev(journalpostId: String) = redigerExstream[journalpostId]?.let { ServiceResult.Ok(Pen.RedigerDokumentResponse(it)) }
        ?: TODO("Not implemented")

    override suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>> {
        TODO("Not yet implemented")
    }

    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String) = (kravPaaGammeltRegelverk[vedtaksId] ?: false).let { ServiceResult.Ok(it) }

    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String) = kravStoettetAvDatabygger[vedtaksId]?.let { ServiceResult.Ok(it) }
        ?: TODO("Not implemented")

    override suspend fun hentPesysBrevdata(
        saksId: Long,
        vedtaksId: Long?,
        brevkode: Brevkode.Redigerbart,
        avsenderEnhetsId: String?,
    ): ServiceResult<BrevdataResponse.Data> {
        TODO("Not yet implemented")
    }

    override suspend fun sendbrev(
        sendRedigerbartBrevRequest: Pen.SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse> {
        TODO("Not yet implemented")
    }

}

class FakeBrevmetadataService(
    val eblanketter: List<BrevdataDto> = listOf(),
    val brevmaler: List<BrevdataDto> = listOf(),
    val maler: Map<String, BrevdataDto> = mapOf(),
) : BrevmetadataService {
    override suspend fun getBrevmalerForSakstype(sakstype: Sakstype) = brevmaler

    override suspend fun getEblanketter() = eblanketter

    override suspend fun getMal(brevkode: String) = maler[brevkode] ?: TODO("Not implemented")

}

class FakeBrevbakerService(
    val maler: List<TemplateDescription.Redigerbar> = listOf(),
    val redigerbareMaler: Map<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mapOf()
) : BrevbakerService {
    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> {
        TODO("Not yet implemented")
    }

    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkup> {
        TODO("Not yet implemented")
    }

    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getTemplates() = ServiceResult.Ok(maler)

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]

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
