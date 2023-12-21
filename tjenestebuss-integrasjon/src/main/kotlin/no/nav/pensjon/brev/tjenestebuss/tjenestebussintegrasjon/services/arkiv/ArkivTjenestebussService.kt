package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.maskerFnr
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Failure
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevResponseDto.Failure.FailureType
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.*
import no.nav.virksomhet.tjenester.felles.v1.StelvioFault
import org.slf4j.LoggerFactory
import javax.xml.bind.JAXBElement
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName

class ArkivTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler): TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val arkivClient = ArkivClient(config, securityHandler, callIdHandler).client()

    fun bestillBrev(bestillBrevRequestDto: BestillBrevRequestDto): BestillBrevResponseDto {
        try {
            val response = arkivClient.bestillBrev(BestillBrevRequest().apply {
                brevKode = bestillBrevRequestDto.brevKode
                brevGruppe = bestillBrevRequestDto.brevGruppe
                redigerbart =
                    JAXBElement(QName("redigerbart"), Boolean::class.java, bestillBrevRequestDto.isRedigerbart)
                sprakKode = bestillBrevRequestDto.sprakkode
                sakskontekst = Sakskontekst().apply {
                    saksbehandlernavn = bestillBrevRequestDto.sakskontekstDto.saksbehandlernavn
                    saksbehandlerId = bestillBrevRequestDto.sakskontekstDto.saksbehandlerId
                    journalenhet = bestillBrevRequestDto.sakskontekstDto.journalenhet
                    gjelder = bestillBrevRequestDto.sakskontekstDto.gjelder
                    dokumenttype = bestillBrevRequestDto.sakskontekstDto.dokumenttype
                    dokumentdato = bestillBrevRequestDto.sakskontekstDto.dokumentdato
                    fagsystem = bestillBrevRequestDto.sakskontekstDto.fagsystem
                    fagomradeKode = bestillBrevRequestDto.sakskontekstDto.fagomradekode
                    innhold = bestillBrevRequestDto.sakskontekstDto.innhold
                    kategori = bestillBrevRequestDto.sakskontekstDto.kategori
                    saksid = bestillBrevRequestDto.sakskontekstDto.saksid
                    sensitivitetsgrad = bestillBrevRequestDto.sakskontekstDto.sensitivitet
                }
            })
            logger.info("Opprettet brev med journalpostId: ${response!!.journalpostId} i sakId: ${bestillBrevRequestDto.sakskontekstDto.saksid} ")
            return BestillBrevResponseDto.Success(response.journalpostId)
        } catch (ex: BestillBrevOpprettelseJournalpostFeilet) {
            logger.error("En feil oppstod under opprettelse av journalpost: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return Failure(FailureType.OPPRETTE_JOURNALPOST, ex.faultInfo)
        } catch (ex: BestillBrevHenteBrevdataFeilet) {
            logger.error("En feil oppstod under henting av brevdata: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return Failure(FailureType.HENTE_BREVDATA, ex.faultInfo)
        } catch (ex: BestillBrevManglerObligatoriskInput) {
            logger.error("En feil oppstod under opprettelse av brev, mangler obligatoriske felter: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return Failure(FailureType.MANGLER_OBLIGATORISK_INPUT, ex.faultInfo)
        } catch (ex: BestillBrevAdresseIkkeRegistrert) {
            logger.error("En feil oppstod under opprettelse av brev, adresse ikke registrert: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return Failure(FailureType.ADRESSE_MANGLER, ex.faultInfo)
        }
    }
}

sealed class BestillBrevResponseDto {
    data class Success(val journalpostId: String) : BestillBrevResponseDto()
    data class Failure(val failureType: FailureType,
        val message: String,
        val source: String,
        val type: String,
        val cause: String,
    ) : BestillBrevResponseDto() {
        constructor(failureType: FailureType, stelvioFault: StelvioFault): this(
            failureType,
            stelvioFault.errorMessage,
            stelvioFault.errorSource,
            stelvioFault.errorType,
            stelvioFault.rootCause,
        )
        enum class FailureType {
            ADRESSE_MANGLER,
            HENTE_BREVDATA,
            MANGLER_OBLIGATORISK_INPUT,
            OPPRETTE_JOURNALPOST,
        }
    }
}

data class BestillBrevRequestDto(
    val brevKode: String,
    val brevGruppe: String,
    val isRedigerbart: Boolean,
    val sprakkode: String,
    val sakskontekstDto: SakskontekstDto,
)


data class SakskontekstDto(
    val journalenhet: String,
    val gjelder: String,
    val dokumenttype: String,
    val dokumentdato: XMLGregorianCalendar,
    val fagsystem: String,
    val fagomradekode: String,
    val innhold: String,
    val kategori: String,
    val saksid: String,
    val saksbehandlernavn: String,
    val saksbehandlerId: String,
    val sensitivitet: String
)