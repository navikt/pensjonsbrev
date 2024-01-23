package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizedHttpClientResult
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse.FailureType.EXTREAM_REDIGERING_GENERELL
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse.FailureType.TJENESTEBUSS_INTEGRASJON
import no.nav.pensjon.brev.skribenten.routes.OrderLetterRequest
import no.nav.pensjon.brev.skribenten.routes.getCurrentGregorianTime
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevExtreamRequestDto.SakskontekstDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto.Success.Samhandler
import org.slf4j.LoggerFactory

class TjenestebussIntegrasjonService(config: Config, authService: AzureADService) {

    private val tjenestebussIntegrasjonUrl = config.getString("url")
    private val tjenestebussIntegrasjonScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(TjenestebussIntegrasjonService::class.java)

    private val tjenestebussIntegrasjonClient =
        AzureADOnBehalfOfAuthorizedHttpClient(tjenestebussIntegrasjonScope, authService) {
            defaultRequest {
                url(tjenestebussIntegrasjonUrl)
            }
            install(ContentNegotiation) {
                jackson()
            }
        }

    suspend fun finnSamhandler(
        call: ApplicationCall,
        samhandlerType: SamhandlerTypeCode,
        navn: String
    ): ServiceResult<FinnSamhandlerResponseDto.Success, FinnSamhandlerResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/finnSamhandler") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                FinnSamhandlerRequestDto(
                    navn = navn,
                    samhandlerType = SamhandlerTypeCode.valueOf(samhandlerType.name)
                )
            )
        }.toServiceResult<FinnSamhandlerResponseDto.Success, FinnSamhandlerResponseDto.Failure>()
            .map {
                FinnSamhandlerResponseDto.Success(samhandlere = it.samhandlere.map { s ->
                    Samhandler(
                        navn = s.navn,
                        samhandlerType = s.samhandlerType,
                        offentligId = s.offentligId,
                        idType = s.idType
                    )
                })
            }.catch { error -> FinnSamhandlerResponseDto.Failure(message = error.message, type = error.type) }

    suspend fun hentSamhandler(
        call: ApplicationCall,
        idTSSEkstern: String,
        hentDetaljert: Boolean,
    ): ServiceResult<HentSamhandlerResponseDto.Success, HentSamhandlerResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandler") {
            HentSamhandlerRequestDto(
                idTSSEkstern = idTSSEkstern,
                hentDetaljert = hentDetaljert
            )
        }.toServiceResult<HentSamhandlerResponseDto.Success, HentSamhandlerResponseDto.Failure>()
            .map {
                HentSamhandlerResponseDto.Success(
                    samhandler = HentSamhandlerResponseDto.Success.Samhandler(
                        navn = it.samhandler.navn,
                        samhandlerType = it.samhandler.samhandlerType,
                        offentligId = it.samhandler.offentligId,
                        idType = it.samhandler.idType
                    )
                )
            }.catch { error ->
                HentSamhandlerResponseDto.Failure(message = error.message, type = error.type)
            }

    suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        navIdent: String,
        metadata: BrevdataDto,
        name: String
    ): ServiceResult<BestillExtreamBrevResponseDto, String> {

        //TODO better error handling.
        // TODO access controls for e-blanketter
        val isEblankett = metadata.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT
        val isNotat = metadata.dokType == BrevdataDto.DokumentType.N

        return tjenestebussIntegrasjonClient.post(call, "/bestillExtreamBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                BestillBrevExtreamRequestDto(
                    brevKode = request.brevkode, // ID på brev,
                    brevGruppe = metadata.brevgruppe
                        ?: throw BadRequestException("Fant ikke brevgruppe gitt extream brev :${request.brevkode}"),
                    isRedigerbart = metadata.redigerbart,
                    sprakkode = request.spraak.toString(),
                    brevMottakerNavn = request.mottakerText?.takeIf { isEblankett },        // custom felt kun for sed/eblankett
                    sakskontekstDto = SakskontekstDto(
                        // TODO sett journalenhet ut fra queryparam/psak eller sakEier
                        journalenhet = request.enhetsId,                              // NAV org enhet nr som skriver brevet. Kommer med i signatur.
                        //    private String decideJournalEnhet(NAVEnhet enhetToSet, BrevmenyForm form) {
                        //        if (form.getValgtAvsenderEnhet().equals(enhetToSet.getEnhetsId())) {
                        //            return enhetToSet.getEnhetsId();
                        //        } else {
                        //            return form.getSak().getEierTilgang().getTilgangGittTil();
                        //        }
                        gjelder = request.gjelderPid,                       // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                        dokumentdato = getCurrentGregorianTime(),           // nåværende dato. TODO Skal dokumentdato komme fra parameter kanskje?
                        dokumenttype = metadata.dokType.toString(),         // Inngående, utgående, notat
                        fagsystem = "PEN",
                        fagomradekode = "PEN",                              // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                        innhold = metadata.dekode,                          // Visningsnavn
                        kategori = metadata.dokumentkategori.toString(),    // Kategori for dokumentet
                        saksid = request.sakId.toString(),// sakid
                        saksbehandlernavn = name,
                        saksbehandlerId = navIdent,
                        kravtype = null, // TODO sett. Brukes for notater
                        land = request.landkode.takeIf { isEblankett },
                        //TODO sett verge om det er verge og samhandler om det overstyres
                        mottaker = if (isEblankett || isNotat) null else request.gjelderPid,  //fnr/tss id for mottaker
                        vedtakId = null, // TODO sett fra queryparam/psak
                    )
                )
            )
        }.toServiceResult<BestillExtreamBrevResponseDto, String>()
    }

    suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): ServiceResult<RedigerDoksysDokumentResponseDto.Success, RedigerDoksysDokumentResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/redigerDoksysBrev") {
            RedigerDoksysDokumentRequestDto(journalpostId = journalpostId, dokumentId = dokumentId)
        }.toServiceResult<RedigerDoksysDokumentResponseDto.Success, RedigerDoksysDokumentResponseDto.Failure>()
            .map {
                RedigerDoksysDokumentResponseDto.Success(url = it.url)
            }.catch { error ->
                RedigerDoksysDokumentResponseDto.Failure(message = error.message, type = error.type)
            }

    suspend fun redigerExtreamBrev(
        call: ApplicationCall,
        dokumentId: String,
    ): BestillOgRedigerBrevResponse {
        val response = tjenestebussIntegrasjonClient.post(call, "/redigerExtreamBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(RedigerExtreamDokumentRequestDto(dokumentId))
        }

        when (response) {
            is AuthorizedHttpClientResult.Response -> {
                val httpResponse = response.response
                val extreamResponse = httpResponse.body<RedigerExtreamDokumentResponseDto>()
                if (httpResponse.status.isSuccess()) {
                    return BestillOgRedigerBrevResponse(
                        url = extreamResponse.url,
                        failureType = extreamResponse.failure?.let {
                            logger.error("Feil ved redigering av extream brev $it")
                            EXTREAM_REDIGERING_GENERELL
                        }
                    )
                } else {
                    logger.error("""Feil ved redigering av extream brev. 
                                |Status: ${httpResponse.status}}
                                |message: ${httpResponse.bodyAsText()}}""".trimMargin())
                    return BestillOgRedigerBrevResponse(TJENESTEBUSS_INTEGRASJON)
                }
            }
            is AuthorizedHttpClientResult.Error -> {
                logger.error(response.error.logString())
                return BestillOgRedigerBrevResponse(TJENESTEBUSS_INTEGRASJON)
            }
        }
    }
}
