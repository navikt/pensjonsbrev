package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.maskerFnr
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.*
import org.slf4j.LoggerFactory
import javax.xml.bind.JAXBElement
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName

class ArkivTjenestebussService(private val arkivClient: Arkiv) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun bestillBrev(bestillBrevRequestDto: BestillBrevRequestDto): BestillBrevResponseDto {
        // TODO do we need to create a new bean every time to get refreshed auth?
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
            return BestillBrevResponseDto.Journalpost(response.journalpostId)
        } catch (ex: BestillBrevOpprettelseJournalpostFeilet) {
            logger.error("En feil oppstod under opprettelse av journalpost: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillBrevResponseDto.Failure(ex.faultInfo)
        } catch (ex: BestillBrevHenteBrevdataFeilet) {
            logger.error("En feil oppstod under henting av brevdata: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillBrevResponseDto.Failure(ex.faultInfo)
        } catch (ex: BestillBrevManglerObligatoriskInput) {
            logger.error("En feil oppstod under opprettelse av brev, mangler obligatoriske felter: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillBrevResponseDto.Failure(ex.faultInfo)
        } catch (ex: BestillBrevAdresseIkkeRegistrert) {
            logger.error("En feil oppstod under opprettelse av brev, adresse ikke registrert: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillBrevResponseDto.Failure(ex.faultInfo)
        }
    }
}

sealed class BestillBrevResponseDto {
    data class Journalpost(val journalpostId: String) : BestillBrevResponseDto()
    data class Failure(private val fault: no.nav.virksomhet.tjenester.felles.v1.StelvioFault) :
        BestillBrevResponseDto() {
        val message: String = fault.errorMessage
        val source: String = fault.errorSource
        val type: String = fault.errorType
        val cause: String = fault.rootCause
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