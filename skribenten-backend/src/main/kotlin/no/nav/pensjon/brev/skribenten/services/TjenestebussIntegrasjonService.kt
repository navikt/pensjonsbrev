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
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevExstreamRequestDto.SakskontekstDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentkategoriCode.SED
import org.slf4j.LoggerFactory
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

class TjenestebussIntegrasjonService(config: Config, authService: AzureADService) : ServiceStatus {

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
            .catch { message, status ->
                logger.error("Feil ved samhandler søk. Status: $status Melding: $message")
                FinnSamhandlerResponseDto("Feil ved henting av samhandler")
            }

    suspend fun hentSamhandler(
        call: ApplicationCall,
        idTSSEkstern: String,
        hentDetaljert: Boolean,
    ): HentSamhandlerResponseDto =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandler") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                HentSamhandlerRequestDto(
                    idTSSEkstern = idTSSEkstern,
                    hentDetaljert = hentDetaljert
                )
            )
        }.toServiceResult<HentSamhandlerResponseDto>()
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerResponseDto(null, HentSamhandlerResponseDto.FailureType.GENERISK)
            }

    suspend fun hentSamhandlerAdresse(
        call: ApplicationCall,
        idTSSEkstern: String,
    ): HentSamhandlerAdresseResponseDto =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandlerAdresse") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(HentSamhandlerAdresseRequestDto(idTSSEkstern))
        }.toServiceResult<HentSamhandlerAdresseResponseDto>()
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler adresse fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerAdresseResponseDto(null, GENERISK)
            }


    suspend fun bestillExstreamBrev(
        brevkode: String,
        call: ApplicationCall,
        enhetsId: String,
        gjelderPid: String,
        idTSSEkstern: String? = null,
        isSensitive: Boolean,
        landkode: String? = null,
        metadata: BrevdataDto,
        mottakerText: String? = null,
        name: String,
        navIdent: String,
        sakId: Long,
        spraak: SpraakKode,
        vedtaksId: Long? = null,
    ): ServiceResult<BestillExstreamBrevResponseDto> {
        val isEblankett = metadata.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT
        val isNotat = metadata.dokType == BrevdataDto.DokumentType.N

        return tjenestebussIntegrasjonClient.post(call, "/bestillExstreamBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                BestillBrevExstreamRequestDto(
                    brevKode = brevkode, // ID på brev
                    brevGruppe = metadata.brevgruppe
                        ?: throw BadRequestException("Fant ikke brevgruppe gitt exstream brev :${brevkode}"),
                    isRedigerbart = metadata.redigerbart,
                    sprakkode = spraak.toString(),
                    brevMottakerNavn = mottakerText?.takeIf { isEblankett },// custom felt kun for sed/eblankett

                    sakskontekstDto = SakskontekstDto(
                        journalenhet = enhetsId,                            // NAV org enhet nr som skriver brevet. Kommer med i signatur.
                        gjelder = gjelderPid,                               // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                        dokumentdato = getCurrentGregorianTime(),           // nåværende dato.
                        dokumenttype = metadata.dokType.toString(),         // Inngående, utgående, notat
                        fagsystem = "PEN",
                        fagomradekode = "PEN",                              // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                        innhold = metadata.dekode,                          // Visningsnavn
                        kategori = if (isEblankett) SED.toString() else metadata.dokumentkategori.toString(),    // Kategori for dokumentet
                        saksid = sakId.toString(),                          // sakid
                        saksbehandlernavn = name,
                        saksbehandlerId = navIdent,
                        kravtype = null, // TODO sett. Brukes dette for notater i det hele tatt?
                        land = landkode.takeIf { isEblankett },
                        mottaker = if (isEblankett || isNotat) null else idTSSEkstern ?: gjelderPid,
                        vedtaksId = vedtaksId?.toString(),
                        isSensitive = isSensitive,
                    )
                )
            )
        }.toServiceResult<BestillExstreamBrevResponseDto>()
    }

    suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): ServiceResult<RedigerDoksysDokumentResponseDto> =
        tjenestebussIntegrasjonClient.post(call, "/redigerDoksysBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(RedigerDoksysDokumentRequestDto(journalpostId, dokumentId))
        }.toServiceResult<RedigerDoksysDokumentResponseDto>()

    suspend fun redigerExstreamBrev(
        call: ApplicationCall,
        dokumentId: String,
    ): ServiceResult<RedigerExstreamDokumentResponseDto> =
        tjenestebussIntegrasjonClient.post(call, "/redigerExstreamBrev") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(RedigerExstreamDokumentRequestDto(dokumentId))
        }.toServiceResult<RedigerExstreamDokumentResponseDto>()

    private fun getCurrentGregorianTime(): XMLGregorianCalendar {
        val cal = GregorianCalendar()
        cal.time = Date()
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
    }

    override val name = "Tjenestebuss-integrasjon"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        tjenestebussIntegrasjonClient.get(call, "/isReady").toServiceResult<String>().map { true }

    suspend fun status(call: ApplicationCall): ServiceResult<TjenestebussStatus> =
        tjenestebussIntegrasjonClient.get(call, "/status").toServiceResult()
}
