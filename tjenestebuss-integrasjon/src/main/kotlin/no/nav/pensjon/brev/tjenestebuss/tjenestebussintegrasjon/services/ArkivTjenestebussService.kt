package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import com.typesafe.config.Config
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevResponse
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.Sakskontekst
import no.nav.virksomhet.tjenester.arkiv.v1.Arkiv
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import java.util.*
import javax.xml.bind.JAXBElement
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName

class ArkivTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler) {
    private val tjenestebussUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "ArkivWSEXP_ArkivHttpService"
        val portName = "ArkivWSEXP_ArkivHttpPort"
        val namespace = "http://nav.no/virksomhet/tjenester/arkiv/v1/Binding"
        address = "${tjenestebussUrl}nav-tjeneste-arkiv_v1Web/sca/ArkivWSEXP"
        wsdlURL = "wsdl/nav-tjeneste-arkiv_ArkivWSEXP.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = Arkiv::class.java
        handlers = listOf(securityHandler)
        features = listOf(WSAddressingFeature())        //TODO add Logging feature?
    }

    fun bestillBrev(bestillBrevDto: BestillBrevDto): BestillBrevResponse? {
        // TODO do we need to create a new bean every time to get refreshed auth?
        val arkivClient: Arkiv = jaxWsProxyFactoryBean.create() as Arkiv
        return arkivClient.bestillBrev(BestillBrevRequest().apply {
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
        })
    }

    data class BestillBrevDto(
        val cal: XMLGregorianCalendar, // fjern senere

        val brevKode: String = "PE_OK_06_101",
        val brevGruppe: String = "brevgr001",
        val isRedigerbart: Boolean = true,
        val sprakkode: String = "NB",
        val sakskontekstDto: SakskontekstDto = SakskontekstDto(dokumentdato = cal),
    )

    data class SakskontekstDto(
        val journalenhet: String = "4405",
        val gjelder: String = "09417320595",
        val dokumenttype: String = "N",
        val dokumentdato: XMLGregorianCalendar,
        val fagsystem: String = "PEN",
        val fagomradekode: String = "PEN",
        val innhold: String = "Posteringsgrunnlag",
        val kategori: String = "B",
        val saksid: String = "22972355",
        val saksbehandlernavn: String = "F_Z994249",
        val saksbehandlerId: String = "Z994249",
        val sensitivitet: String = "false"
    )
}