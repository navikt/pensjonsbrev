package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.maskerFnr
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevResponse
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.*
import org.slf4j.LoggerFactory
import javax.xml.bind.JAXBElement
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName

class ArkivTjenestebussService(private val arkivClient: Arkiv) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun bestillBrev(bestillBrevDto: BestillBrevDto): BestillBrevResponse? {
        // TODO do we need to create a new bean every time to get refreshed auth?
        return try {
            arkivClient.bestillBrev(BestillBrevRequest().apply {
                brevKode = bestillBrevDto.brevKode
                brevGruppe = bestillBrevDto.brevGruppe
                redigerbart = JAXBElement(QName("redigerbart"), Boolean::class.java, bestillBrevDto.isRedigerbart)
                sprakKode = bestillBrevDto.sprakkode
                sakskontekst = Sakskontekst().apply {
                    saksbehandlernavn = bestillBrevDto.sakskontekstDto.saksbehandlernavn
                    saksbehandlerId = bestillBrevDto.sakskontekstDto.saksbehandlerId
                    journalenhet = bestillBrevDto.sakskontekstDto.journalenhet
                    gjelder = bestillBrevDto.sakskontekstDto.gjelder
                    dokumenttype = bestillBrevDto.sakskontekstDto.dokumenttype
                    dokumentdato = bestillBrevDto.sakskontekstDto.dokumentdato
                    fagsystem = bestillBrevDto.sakskontekstDto.fagsystem
                    fagomradeKode = bestillBrevDto.sakskontekstDto.fagomradekode
                    innhold = bestillBrevDto.sakskontekstDto.innhold
                    kategori = bestillBrevDto.sakskontekstDto.kategori
                    saksid = bestillBrevDto.sakskontekstDto.saksid
                    sensitivitetsgrad = bestillBrevDto.sakskontekstDto.sensitivitet
                }
            }).also {
                logger.info("Opprettet brev med journalpostId: ${it!!.journalpostId} i sakId: ${bestillBrevDto.sakskontekstDto.saksid} ")
            }
        } catch (bbojf: BestillBrevOpprettelseJournalpostFeilet) {
            logger.error("En feil oppstod under opprettelse av journalpost: ${maskerFnr(bbojf.faultInfo.errorMessage)}")
            throw bbojf
        } catch (bbhbf: BestillBrevHenteBrevdataFeilet) {
            logger.error("En feil oppstod under henting av brevdata: ${maskerFnr(bbhbf.faultInfo.errorMessage)}")
            throw bbhbf
        }
        catch (bbhbf: BestillBrevManglerObligatoriskInput) {
            logger.error("En feil oppstod under opprettelse av brev, mangler obligatoriske felter: ${maskerFnr(bbhbf.faultInfo.errorMessage)}")
            throw bbhbf
        }
        catch (bbair: BestillBrevAdresseIkkeRegistrert) {
            logger.error("En feil oppstod under opprettelse av brev, adresse ikke registrert: ${maskerFnr(bbair.faultInfo.errorMessage)}")
            throw bbair
        }
    }
}

data class BestillBrevDto(
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