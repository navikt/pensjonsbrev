package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto


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

    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<PenService.KravStoettetAvDatabyggerResult> {
        TODO("Not yet implemented")
    }

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