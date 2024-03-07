package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.maskerFnr
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillExstreamBrevResponseDto.FailureType.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.*
import org.slf4j.LoggerFactory
import javax.xml.bind.JAXBElement
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName
import kotlin.time.Duration.Companion.days

private val elektroniskVarslingTrue = JAXBElement(
    QName("", "tillattelektroniskvarsling"),
    Boolean::class.java,
    Sakskontekst::class.java,
    true
)

class ArkivTjenestebussService(clientFactory: ArkivClientFactory) : TjenestebussService<Arkiv>(clientFactory, pingExpiration = 1.days) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Bestiller Exstream brev
     *
     * @param request brev og journalpost informasjon
     * @return en response som enten er Success med journalpostId eller Failure med feiltype og eventuell årsak
     */
    fun bestillBrev(request: BestillBrevExstreamRequestDto): BestillExstreamBrevResponseDto {
        try {
            val saksKontekst = request.sakskontekstDto
            val response = client.bestillBrev(BestillBrevRequest().apply {
                brevKode = request.brevKode
                brevGruppe = request.brevGruppe
                redigerbart =
                    JAXBElement(QName("redigerbart"), Boolean::class.java, request.isRedigerbart)
                sprakKode = request.sprakkode
                brevMottakerNavn = request.brevMottakerNavn
                sakskontekst = Sakskontekst().apply {
                    dokumentdato = saksKontekst.dokumentdato
                    dokumenttype = saksKontekst.dokumenttype
                    fagomradeKode = saksKontekst.fagomradekode
                    fagsystem = saksKontekst.fagsystem
                    gjelder = saksKontekst.gjelder
                    innhold = saksKontekst.innhold
                    journalenhet = saksKontekst.journalenhet
                    kategori = saksKontekst.kategori
                    kravtype = saksKontekst.kravtype
                    land = saksKontekst.land
                    mottaker = saksKontekst.mottaker
                    saksbehandlerId = saksKontekst.saksbehandlerId
                    saksbehandlernavn = saksKontekst.saksbehandlernavn
                    saksid = saksKontekst.saksid
                    sensitivitetsgrad = saksKontekst.isSensitive.toString()
                    vedtaksInformasjon = saksKontekst.vedtaksId
                    tillattelektroniskvarsling = elektroniskVarslingTrue
                }
            })
            logger.info("Opprettet brev med journalpostId: ${response!!.journalpostId} i sakId: ${saksKontekst.saksid} ")
            return BestillExstreamBrevResponseDto(response.journalpostId)
        } catch (ex: BestillBrevOpprettelseJournalpostFeilet) {
            logger.error("En feil oppstod under opprettelse av journalpost: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExstreamBrevResponseDto(OPPRETTE_JOURNALPOST)
        } catch (ex: BestillBrevHenteBrevdataFeilet) {
            logger.error("En feil oppstod under henting av brevdata: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExstreamBrevResponseDto(HENTE_BREVDATA)
        } catch (ex: BestillBrevManglerObligatoriskInput) {
            logger.error("En feil oppstod under opprettelse av brev, mangler obligatoriske felter: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExstreamBrevResponseDto(MANGLER_OBLIGATORISK_INPUT)
        } catch (ex: BestillBrevAdresseIkkeRegistrert) {
            logger.error("En feil oppstod under opprettelse av brev, adresse ikke registrert: ${maskerFnr(ex.faultInfo.errorMessage)}")
            return BestillExstreamBrevResponseDto(ADRESSE_MANGLER)
        }
    }

    override val name = "ArkivTjenestebuss"
    override fun sendPing(): Boolean? = null
}

data class BestillExstreamBrevResponseDto(
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

data class BestillBrevExstreamRequestDto(
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
        val isSensitive: Boolean,
        val vedtaksId: String?,
    )
}
