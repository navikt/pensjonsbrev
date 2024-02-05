package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.maskerFnr
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillExtreamBrevResponseDto.FailureType.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.*
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
    fun bestillBrev(request: BestillBrevExtreamRequestDto): BestillExtreamBrevResponseDto {
        try {
            val response = arkivClient.bestillBrev(BestillBrevRequest().apply {
                brevKode = request.brevKode
                brevGruppe = request.brevGruppe
                redigerbart =
                    JAXBElement(QName("redigerbart"), Boolean::class.java, request.isRedigerbart)
                sprakKode = request.sprakkode
                brevMottakerNavn = request.brevMottakerNavn
                sakskontekst = Sakskontekst().apply {
                    dokumentdato = request.sakskontekstDto.dokumentdato
                    dokumenttype = request.sakskontekstDto.dokumenttype
                    fagomradeKode = request.sakskontekstDto.fagomradekode
                    fagsystem = request.sakskontekstDto.fagsystem
                    gjelder = request.sakskontekstDto.gjelder
                    innhold = request.sakskontekstDto.innhold
                    journalenhet = request.sakskontekstDto.journalenhet
                    kategori = request.sakskontekstDto.kategori
                    kravtype = request.sakskontekstDto.kravtype
                    land = request.sakskontekstDto.land
                    mottaker = request.sakskontekstDto.mottaker
                    saksbehandlerId = request.sakskontekstDto.saksbehandlerId
                    saksbehandlernavn = request.sakskontekstDto.saksbehandlernavn
                    saksid = request.sakskontekstDto.saksid
                    sensitivitetsgrad = "false"
                    vedtaksInformasjon = request.sakskontekstDto.vedtaksId
                    tillattelektroniskvarsling = elektroniskVarslingTrue
                }
            })
            logger.info("Opprettet brev med journalpostId: ${response!!.journalpostId} i sakId: ${request.sakskontekstDto.saksid} ")
            return BestillExtreamBrevResponseDto(response.journalpostId)
        } catch (ex: BestillBrevOpprettelseJournalpostFeilet) {
            logger.error("En feil oppstod under opprettelse av journalpost: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExtreamBrevResponseDto(OPPRETTE_JOURNALPOST)
        } catch (ex: BestillBrevHenteBrevdataFeilet) {
            logger.error("En feil oppstod under henting av brevdata: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExtreamBrevResponseDto(HENTE_BREVDATA)
        } catch (ex: BestillBrevManglerObligatoriskInput) {
            logger.error("En feil oppstod under opprettelse av brev, mangler obligatoriske felter: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExtreamBrevResponseDto(MANGLER_OBLIGATORISK_INPUT)
        } catch (ex: BestillBrevAdresseIkkeRegistrert) {
            logger.error("En feil oppstod under opprettelse av brev, adresse ikke registrert: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExtreamBrevResponseDto(ADRESSE_MANGLER)
        }
    }
}

data class BestillExtreamBrevResponseDto(
    val journalpostId: String?,
    val failureType: FailureType?
) {
    constructor(journalpostId: String) : this(journalpostId = journalpostId, failureType = null)
    constructor(failureType: FailureType) : this(journalpostId = null, failureType = failureType)

    enum class FailureType {
        ADRESSE_MANGLER,
        HENTE_BREVDATA,
        MANGLER_OBLIGATORISK_INPUT,
        OPPRETTE_JOURNALPOST,
    }
}

data class BestillBrevExtreamRequestDto(
    val brevKode: String,
    val brevGruppe: String,
    val isRedigerbart: Boolean,
    val sprakkode: String,
    val sakskontekstDto: SakskontekstDto,
    val brevMottakerNavn: String?,
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
        val vedtaksId: String?,
    )
}
