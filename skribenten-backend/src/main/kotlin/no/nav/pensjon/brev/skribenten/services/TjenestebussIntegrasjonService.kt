package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevExtreamRequestDto.SakskontekstDto
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.OrderLetterRequest
import org.slf4j.LoggerFactory
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

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
    ): FinnSamhandlerResponseDto =
        tjenestebussIntegrasjonClient.post(call, "/finnSamhandler") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                FinnSamhandlerRequestDto(
                    navn = navn,
                    samhandlerType = SamhandlerTypeCode.valueOf(samhandlerType.name)
                )
            )
        }.toServiceResult<FinnSamhandlerResponseDto>()
            .map {
                FinnSamhandlerResponseDto(samhandlere = it.samhandlere.map { s ->
                    FinnSamhandlerResponseDto.Samhandler(
                        navn = s.navn,
                        samhandlerType = s.samhandlerType,
                        offentligId = s.offentligId,
                        idType = s.idType
                    )
                })
            }.catch { message, status ->
                logger.error("Feil ved samhandler søk. Status: $status Melding: $message")
                FinnSamhandlerResponseDto("Feil ved henting av samhandler")
            }

    suspend fun hentSamhandler(
        call: ApplicationCall,
        idTSSEkstern: String,
        hentDetaljert: Boolean,
    ): HentSamhandlerResponseDto =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandler") {
            HentSamhandlerRequestDto(
                idTSSEkstern = idTSSEkstern,
                hentDetaljert = hentDetaljert
            )
        }.toServiceResult<HentSamhandlerResponseDto>()
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerResponseDto(null, HentSamhandlerResponseDto.FailureType.GENERISK)
            }

    suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        navIdent: String,
        metadata: BrevdataDto,
        name: String
    ): ServiceResult<BestillExtreamBrevResponseDto> {

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
                        vedtaksId = request.vedtaksId?.toString(),
                    )
                )
            )
        }.toServiceResult<BestillExtreamBrevResponseDto>()
    }

    suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): ServiceResult<RedigerDoksysDokumentResponseDto> =
        tjenestebussIntegrasjonClient.post(call, "/redigerDoksysBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(RedigerDoksysDokumentRequestDto(journalpostId = journalpostId, dokumentId = dokumentId))
        }.toServiceResult<RedigerDoksysDokumentResponseDto>()

    suspend fun redigerExtreamBrev(
        call: ApplicationCall,
        dokumentId: String,
    ): ServiceResult<RedigerExtreamDokumentResponseDto> =
        tjenestebussIntegrasjonClient.post(call, "/redigerExtreamBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(RedigerExtreamDokumentRequestDto(dokumentId))
        }.toServiceResult<RedigerExtreamDokumentResponseDto>()

    private fun getCurrentGregorianTime(): XMLGregorianCalendar {
        val cal = GregorianCalendar()
        cal.time = Date()
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
    }
}
