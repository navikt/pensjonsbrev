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

private val elektroniskVarslingTrue = JAXBElement(
    QName("", "tillattelektroniskvarsling"),
    Boolean::class.java,
    Sakskontekst::class.java,
    true
)

class ArkivTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler) : TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val arkivClient = ArkivClient(config, securityHandler, callIdHandler).client()

    /**
     * Bestiller Extream brev
     *
     * @param request brev og journalpost informasjon
     * @return en response som enten er Success med journalpostId eller Failure med feiltype og eventuell årsak
     */
    fun bestillBrev(request: BestillBrevExtreamRequestDto): BestillBrevResponseDto {
        try {
            val response = arkivClient.bestillBrev(BestillBrevRequest().apply {
                brevKode = request.brevKode
                brevGruppe = request.brevGruppe
                redigerbart =
                    JAXBElement(QName("redigerbart"), Boolean::class.java, request.isRedigerbart)
                sprakKode = request.sprakkode
                sakskontekst = Sakskontekst().apply {
                    saksbehandlernavn = request.sakskontekstDto.saksbehandlernavn
                    saksbehandlerId = request.sakskontekstDto.saksbehandlerId
                    journalenhet = request.sakskontekstDto.journalenhet
                    gjelder = request.sakskontekstDto.gjelder
                    dokumenttype = request.sakskontekstDto.dokumenttype
                    dokumentdato = request.sakskontekstDto.dokumentdato
                    fagsystem = request.sakskontekstDto.fagsystem
                    fagomradeKode = request.sakskontekstDto.fagomradekode
                    innhold = request.sakskontekstDto.innhold
                    kategori = request.sakskontekstDto.kategori
                    saksid = request.sakskontekstDto.saksid
                }
            })
            logger.info("Opprettet brev med journalpostId: ${response!!.journalpostId} i sakId: ${request.sakskontekstDto.saksid} ")
            //            val response = arkivClient.bestillBrev(BestillBrevRequest().apply {
            //                brevKode = bestillBrevExtreamRequestDto.brevKode
            //                brevGruppe = bestillBrevExtreamRequestDto.brevGruppe
            //                redigerbart =
            //                    JAXBElement(QName("redigerbart"), Boolean::class.java, bestillBrevExtreamRequestDto.isRedigerbart)
            //                sprakKode = bestillBrevExtreamRequestDto.sprakkode
            //                sakskontekst = Sakskontekst().apply {
            //                    saksbehandlernavn = bestillBrevExtreamRequestDto.sakskontekstDto.saksbehandlernavn
            //                    saksbehandlerId = bestillBrevExtreamRequestDto.sakskontekstDto.saksbehandlerId
            //                    journalenhet = bestillBrevExtreamRequestDto.sakskontekstDto.journalenhet
            //                    gjelder = bestillBrevExtreamRequestDto.sakskontekstDto.gjelder
            //                    dokumenttype = bestillBrevExtreamRequestDto.sakskontekstDto.dokumenttype
            //                    dokumentdato = bestillBrevExtreamRequestDto.sakskontekstDto.dokumentdato
            //                    fagsystem = bestillBrevExtreamRequestDto.sakskontekstDto.fagsystem
            //                    fagomradeKode = bestillBrevExtreamRequestDto.sakskontekstDto.fagomradekode
            //                    innhold = bestillBrevExtreamRequestDto.sakskontekstDto.innhold
            //                    kategori = bestillBrevExtreamRequestDto.sakskontekstDto.kategori
            //                    saksid = bestillBrevExtreamRequestDto.sakskontekstDto.saksid
            //                    sensitivitetsgrad = bestillBrevExtreamRequestDto.sakskontekstDto.sensitivitet
            //                }
            //            })
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
    data class Failure(
        val failureType: FailureType,
        val message: String,
        val source: String,
        val type: String,
        val cause: String,
    ) : BestillBrevResponseDto() {
        constructor(failureType: FailureType, stelvioFault: StelvioFault) : this(
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
        val dokumentdato: XMLGregorianCalendar,
        val dokumenttype: String,
        val fagomradekode: String,
        val fagsystem: String,
        val gjelder: String,
        val innhold: String,
        val journalenhet: String,
        val kategori: String,
        val kravtype: String?,
        val land: String?,
        val mottaker: String?,
        val saksbehandlerId: String,
        val saksbehandlernavn: String,
        val saksid: String,
    )
}