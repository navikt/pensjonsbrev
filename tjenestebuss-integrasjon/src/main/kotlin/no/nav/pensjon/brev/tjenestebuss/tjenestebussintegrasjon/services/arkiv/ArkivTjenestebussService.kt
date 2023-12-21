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

    fun bestillBrev(bestillBrevExtreamRequestDto: BestillBrevExtreamRequestDto): BestillBrevResponseDto {
        try {
            val response = arkivClient.bestillBrev(BestillBrevRequest().apply {
                brevKode = bestillBrevExtreamRequestDto.brevKode
                brevGruppe = bestillBrevExtreamRequestDto.brevGruppe
                redigerbart =
                    JAXBElement(QName("redigerbart"), Boolean::class.java, bestillBrevExtreamRequestDto.isRedigerbart)
                sprakKode = bestillBrevExtreamRequestDto.sprakkode
                sakskontekst = Sakskontekst().apply {
                    saksbehandlernavn = bestillBrevExtreamRequestDto.sakskontekstDto.saksbehandlernavn
                    saksbehandlerId = bestillBrevExtreamRequestDto.sakskontekstDto.saksbehandlerId
                    journalenhet = bestillBrevExtreamRequestDto.sakskontekstDto.journalenhet
                    gjelder = bestillBrevExtreamRequestDto.sakskontekstDto.gjelder
                    dokumenttype = bestillBrevExtreamRequestDto.sakskontekstDto.dokumenttype
                    dokumentdato = bestillBrevExtreamRequestDto.sakskontekstDto.dokumentdato
                    fagsystem = bestillBrevExtreamRequestDto.sakskontekstDto.fagsystem
                    fagomradeKode = bestillBrevExtreamRequestDto.sakskontekstDto.fagomradekode
                    innhold = bestillBrevExtreamRequestDto.sakskontekstDto.innhold
                    kategori = bestillBrevExtreamRequestDto.sakskontekstDto.kategori
                    saksid = bestillBrevExtreamRequestDto.sakskontekstDto.saksid
                    sensitivitetsgrad = bestillBrevExtreamRequestDto.sakskontekstDto.sensitivitet
                }
            })
            logger.info("Opprettet brev med journalpostId: ${response!!.journalpostId} i sakId: ${bestillBrevExtreamRequestDto.sakskontekstDto.saksid} ")
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

data class BestillBrevExtreamRequestDto(
    val brevKode: String,
    val brevGruppe: String,
    val isRedigerbart: Boolean,
    val sprakkode: String,
    val sakskontekstDto: SakskontekstDto,
) {
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
}