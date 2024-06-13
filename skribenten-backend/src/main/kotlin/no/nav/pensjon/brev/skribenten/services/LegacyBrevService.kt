package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExstreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentkategoriCode.SED
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class LegacyBrevService(
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)

    suspend fun bestillOgRedigerDoksysBrev(
        call: ApplicationCall,
        request: Api.BestillDoksysBrevRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse =
        coroutineScope {
            val brevMetadata = async { brevmetadataService.getMal(request.brevkode) }

            if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
                Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
            } else {
                val result = bestillDoksysBrev(call, request, request.enhetsId, saksId)

                if (result.failureType == null && result.journalpostId != null && brevMetadata.await().redigerbart) {
                    ventPaaJournalpostOgRedigerDoksysBrev(call, result.journalpostId).let {
                        Api.BestillOgRedigerBrevResponse(url = it.url, failureType = it.failureType)
                    }
                } else result
            }
        }

    private fun harTilgangTilEnhet(enhetsId: String, enhetsTilganger: List<NAVEnhet>): Boolean =
        enhetsTilganger.any { enhet -> enhet.id == enhetsId }

    suspend fun bestillOgRedigerExstreamBrev(
        call: ApplicationCall,
        gjelderPid: String,
        request: Api.BestillExstreamBrevRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val brevtittel = if (brevMetadata.isRedigerbarBrevtittel()) request.brevtittel else brevMetadata.dekode
        val navansatt = navansattService.hentNavansatt(call, call.principal().navIdent).resultOrNull()

        return if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else if (brevtittel.isNullOrBlank()) {
            Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        } else if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrevPen(
                brevkode = request.brevkode,
                call = call,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                idTSSEkstern = request.idTSSEkstern,
                isSensitive = request.isSensitive,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = request.spraak,
                brevtittel = brevtittel,
                vedtaksId = request.vedtaksId,
                saksbehandler = navansatt
            )

            if (result.failureType == null && result.journalpostId != null && brevMetadata.redigerbart) {
                redigerExstreamBrev(call, result.journalpostId)
            } else result
        }
    }

    suspend fun bestillOgRedigerEblankett(
        call: ApplicationCall,
        gjelderPid: String,
        request: Api.BestillEblankettRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse = coroutineScope {
        val brevMetadataDeffered = async { brevmetadataService.getMal(request.brevkode) }
        val navansatt = navansattService.hentNavansatt(call, call.principal().navIdent).resultOrNull()
        val brevMetadata = brevMetadataDeffered.await()

        if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrevPen(
                brevkode = request.brevkode,
                call = call,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                isSensitive = request.isSensitive,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = SpraakKode.NB,
                brevtittel = brevMetadata.dekode,
                landkode = request.landkode,
                mottakerText = request.mottakerText,
                saksbehandler = navansatt
            )

            if (result.failureType == null && result.journalpostId != null) {
                redigerExstreamBrev(call, result.journalpostId)
            } else result
        }
    }

    private suspend fun bestillExstreamBrevPen(
        call: ApplicationCall,
        brevkode: String,
        enhetsId: String,
        gjelderPid: String,
        idTSSEkstern: String? = null,
        isSensitive: Boolean,
        metadata: BrevdataDto,
        saksId: Long,
        spraak: SpraakKode,
        brevtittel: String,
        vedtaksId: Long? = null,
        landkode: String? = null,
        mottakerText: String? = null,
        saksbehandler: Navansatt,
    ): Api.BestillOgRedigerBrevResponse {
        val isEblankett = metadata.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT
        val isNotat = metadata.dokType == BrevdataDto.DokumentType.N

        if (metadata.brevgruppe == null) {
            logger.warn("Fant ikke brevgruppe for exstream brev: $brevkode", HttpStatusCode.InternalServerError)
            return Api.BestillOgRedigerBrevResponse(failureType = Api.BestillOgRedigerBrevResponse.FailureType.EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        }

        return penService.bestillExstreamBrev(call, Pen.BestillExstreamBrevRequest(
            brevKode = brevkode,
            brevGruppe = metadata.brevgruppe,
            redigerbart = metadata.redigerbart,
            sprakKode = spraak.toString(),
            brevMottakerNavn = mottakerText?.takeIf { isEblankett },    // custom felt kun for sed/eblankett,
            sakskontekst = Pen.BestillExstreamBrevRequest.Sakskontekst(
                journalenhet = enhetsId,                                // NAV org enhet nr som skriver brevet. Kommer med i signatur.
                gjelder = gjelderPid,                                   // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                dokumentdato = LocalDateTime.now(),
                dokumenttype = metadata.dokType.toString(),
                fagsystem = "PEN",
                fagomradeKode = "PEN",                                  // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                innhold = brevtittel,                                   // Visningsnavn
                kategori = if (isEblankett) SED.toString() else metadata.dokumentkategori.toString(),
                saksid = saksId.toString(),
                saksbehandlernavn = saksbehandler.navn,
                saksbehandlerid = call.principal().navIdent,
                kravtype = null, // TODO sett. Brukes dette for notater i det hele tatt?
                land = landkode.takeIf { isEblankett },
                mottaker = if (isEblankett || isNotat) null else idTSSEkstern ?: gjelderPid,

                sensitivt = isSensitive
            ),
            vedtaksInformasjon = vedtaksId?.toString()
        )).map {
            Api.BestillOgRedigerBrevResponse(journalpostId = it.journalpostId)
        }.catch { message, statusCode ->
            logger.error("Feil ved bestilling av brev fra exstream mot PEN: $message - status: $statusCode")
            Api.BestillOgRedigerBrevResponse(failureType = Api.BestillOgRedigerBrevResponse.FailureType.EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        }
    }

    private suspend fun redigerExstreamBrev(
        call: ApplicationCall,
        journalpostId: String,
    ): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                penService.redigerExstreamBrev(call, journalpostId)
                    .map {
                        Api.BestillOgRedigerBrevResponse(url = it.uri)
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av exstream brev mot PEN: $message Status: $httpStatusCode")
                        Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_REDIGERING_GENERELL)
                    }
            }
        }

    private suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): Api.BestillOgRedigerBrevResponse =
        penService.bestillDoksysBrev(call, request, enhetsId, saksId)
            .map { response ->
                if (response.failure != null || response.journalpostId != null) {
                    Api.BestillOgRedigerBrevResponse(journalpostId = response.journalpostId, failureType = response.failure?.toApi())
                } else {
                    logger.error("Tom response fra doksys bestilling")
                    Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av doksys brev $message status: $httpStatusCode")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalpostOgRedigerDoksysBrev(call: ApplicationCall, journalpostId: String): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                safService.getFirstDocumentInJournal(call, journalpostId)
                    .map { safResponse ->
                        if (safResponse.errors != null) {
                            logger.error("Feil fra saf ved henting av dokument med journalpostId $journalpostId ${safResponse.errors}")
                            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                        } else if (safResponse.data != null) {
                            val dokumentId = safResponse.data.journalpost.dokumenter.firstOrNull()?.dokumentInfoId

                            if (dokumentId != null) {
                                redigerDoksysBrev(call, journalpostId, dokumentId)
                            } else {
                                logger.error("Fant ingen dokumenter for redigering ved henting av journalpostId $journalpostId")
                                Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
                            }
                        } else {
                            logger.error("Tom response fra saf ved henting av dokument")
                            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                        }
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved henting av dokumentId fra SAF ved redigering av doksys brev $message status: $httpStatusCode")
                        Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }

    private suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): Api.BestillOgRedigerBrevResponse =
        penService.redigerDoksysBrev(call, journalpostId, dokumentId)
            .map {
                Api.BestillOgRedigerBrevResponse(url = it.uri)
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved redigering av doksys brev $message status: $httpStatusCode")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }

    private fun Pen.BestillDoksysBrevResponse.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            Pen.BestillDoksysBrevResponse.FailureType.ADDRESS_NOT_FOUND -> DOKSYS_BESTILLING_ADDRESS_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNAUTHORIZED -> DOKSYS_BESTILLING_UNAUTHORIZED
            Pen.BestillDoksysBrevResponse.FailureType.PERSON_NOT_FOUND -> DOKSYS_BESTILLING_PERSON_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNEXPECTED_DOKSYS_ERROR -> DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR
            Pen.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE -> DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE
            Pen.BestillDoksysBrevResponse.FailureType.TPS_CALL_FAILIURE -> DOKSYS_BESTILLING_TPS_CALL_FAILIURE
        }

    private fun BestillExstreamBrevResponseDto.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            BestillExstreamBrevResponseDto.FailureType.ADRESSE_MANGLER -> EXSTREAM_BESTILLING_ADRESSE_MANGLER
            BestillExstreamBrevResponseDto.FailureType.HENTE_BREVDATA -> EXSTREAM_BESTILLING_HENTE_BREVDATA
            BestillExstreamBrevResponseDto.FailureType.MANGLER_OBLIGATORISK_INPUT -> EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
            BestillExstreamBrevResponseDto.FailureType.OPPRETTE_JOURNALPOST -> EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST
        }

    private fun RedigerDoksysDokumentResponseDto.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            RedigerDoksysDokumentResponseDto.FailureType.UNDER_REDIGERING -> DOKSYS_REDIGERING_UNDER_REDIGERING
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_REDIGERBART -> DOKSYS_REDIGERING_IKKE_REDIGERBART
            RedigerDoksysDokumentResponseDto.FailureType.VALIDERING_FEILET -> DOKSYS_REDIGERING_VALIDERING_FEILET
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_FUNNET -> DOKSYS_REDIGERING_IKKE_FUNNET
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_TILGANG -> DOKSYS_REDIGERING_IKKE_TILGANG
            RedigerDoksysDokumentResponseDto.FailureType.LUKKET -> DOKSYS_REDIGERING_LUKKET
            RedigerDoksysDokumentResponseDto.FailureType.UFORVENTET -> DOKSYS_REDIGERING_UFORVENTET
            RedigerDoksysDokumentResponseDto.FailureType.ENHETSID_MANGLER -> ENHETSID_MANGLER
        }
}