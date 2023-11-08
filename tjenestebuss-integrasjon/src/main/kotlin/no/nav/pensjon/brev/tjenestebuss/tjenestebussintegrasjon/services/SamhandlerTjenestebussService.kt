package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandlerListe
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName

enum class SamhandlerTypeCode {
    AA,    // Ambulansearbeider
    ADVO,  // Advokat / Jurist
    AFPO,  // AFP-ordning
    AK,    // Arbeidskontor
    AMI,   // Arbeidsmarkedsinstitutt
    AN,    // Ankenemnda for sykepenger
    AP,    // Audiopedagog
    APOP,  // Apotek - privat
    APOS,  // Apotek - sykehus
    ART,   // Arbeidstilsynet
    ASEN,  // Arbeidslivssenter
    AT,    // Apotektekniker
    ATU,   // Attfringsutvalg
    AU,    // Audiograf
    BAND,  // Bandasjist
    BB,    // Begravelsesbyr
    BBE,   // Bobestyrer
    BEH,   // Behandler
    BF,    // Bidragsfogden
    BH,    // Barnehage
    BHT,   // Bedriftshelsetjeneste
    BI,    // Bioingenir
    BU,    // Bilutvalget
    EOS,   // ES-Land
    ET,    // Ergoterapeut
    FA,    // Farmasyt
    FB,    // Fiskehelsebiolog
    FFU,   // Folketrygdkontoret for utenlandssaker
    FKL,   // Fellesordningen LO/NHO
    FL,    // Fylkeslegen
    FO,    // Fotterapeut
    FS,    // Forsikringsselskap
    FT,    // Fysioterapeut
    FTK,   // Fylkestrygdekontor
    FULL,  // Fullmektig
    GVS,   // Grunn- og videregende skole
    HE,    // Helsesekretr
    HF,    // Helsefagarbeider
    HFO,   // Helseforetak
    HJS,   // Hjemmesykepleien
    HMS,   // Hjelpemiddelsentral
    HP,    // Hjelpepleier
    HPRD,  // HPR-dummy
    HS,    // Helsesster
    HSTA,  // Helsestasjon
    HSV,   // Hovedstevnevitnet
    HTF,   // Helsetjeneste forvaltning
    HU,    // Hyskole/universitet
    INST,  // Institusjon
    IS,    // Innkrevingssentralen
    JO,    // Jordmor
    KA,    // Klage- og Ankeenhet
    KE,    // Klinisk ernringsfysiolog
    KEMN,  // Kemner
    KI,    // Kiropraktor
    KOMM,  // Kommune
    KON,   // Konvensjonsland
    KRED,  // Kreditor
    KRU,   // Klageinstans reiseutgift
    KYND,  // Sakkyndig
    LABO,  // Laboratorium - offentlig
    LABP,  // Laboratorium - privat
    LARO,  // Lab/rntgen - privat
    LBS,   // Landsdekkende bilsenter
    LE,    // Lege
    LEBE,  // Bedriftslege
    LEFA,  // Fastlege
    LEKO,  // Kommunelege
    LERA,  // Rdgivende lege
    LFK,   // Legemiddel fagkontor
    LHK,   // Leverandr helsetrygdkort
    LK,    // Ligningskontor
    LM,    // Leverandr Motorkjrety
    LOGO,  // Logoped
    LRB,   // Lsreregister Brnnysund
    LS,    // Legesenter
    MT,    // Manuellterapeut
    NB,    // Namsmannen
    OA,    // Omsorgsarbeider
    OI,    // Ortopediingenir
    OM,    // Ombygger bil
    OP,    // Optiker
    OR,    // Ortoptist
    ORVE,  // Ortopedisk verksted
    PE,    // Perfusjonist
    PF,    // Provisorfarmasyt
    POLI,  // Poliklinikk
    PPT,   // Psykologisk pedagogisk tjeneste
    PS,    // Psykolog
    RA,    // Radiograf
    REGO,  // Regional oppgjrsenhet
    RF,    // Reseptarfarmasyt
    RFK,   // Rogaland fylkestrygdekontor
    RHFO,  // Regionalt helseforetak
    RKK,   // Regionklagekontor
    ROL,   // Rdgivende overlege
    RONO,  // Rntgeninstitutt - offentlig
    RONP,  // Rntgeninstitutt - privat
    RT,    // Rdgivende tannlege
    RTK,   // Regionstrygdekontor
    RTV,   // Rikstrygdeverket
    SAD,   // Samleadressat
    SFK,   // Sentralfylkestrygdekontor
    SK,    // Sosialkontor
    SP,    // Sykepleier
    SR,    // Skifteretten
    STKO,  // Stttekontakt
    SUD,   // Statens utlendingsdirektorat
    SYKH,  // Sykehus
    TEPE,  // Tjenestepensjonsordningar
    TH,    // Tannhelsesekretr
    TI,    // Takseringsinstanser
    TK,    // Trygdekontor
    TL,    // Tannlege
    TLO,   // Tannlege offentlig
    TOLK,  // Tolk
    TP,    // Tannpleier
    TR,    // Trygderetten
    TRAN,  // Transportr
    TT,    // Tanntekniker
    UTL,   // Utenlandske samhandler
    VE,    // Veterinr
    VIRT,  // Virtuelt trygdekontor
    VP,    // Vernepleier
    X,     // Diverse
    YH,    // Yrkeshygieniker
    YM,    // Yrkesmedisiner
}

class SamhandlerTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler) {
    private val tjenestebussUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpService"
        val portName = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpPort"
        val namespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf/Binding"
        address = "${tjenestebussUrl}nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP"
        wsdlURL = "wsdl/PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = PSAKSamhandler::class.java
        handlers = listOf(securityHandler)
        features = listOf(WSAddressingFeature())        //TODO add Logging feature?
    }

    fun hentSamhandler(): ASBOPenSamhandler? {
        // TODO do we need to create a new bean every time to get refreshed auth?
        val psakSamhandlerClient: PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler
        return psakSamhandlerClient.hentSamhandler(ASBOPenHentSamhandlerRequest().apply {
            idTSSEkstern = "00998450349"
            hentDetaljert = true
        })
    }

    fun finnSamhandler(): ASBOPenSamhandlerListe? {
        val psakSamhandlerClient: PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler
        return psakSamhandlerClient.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
            navn = "Advokat"
            samhandlerType = SamhandlerTypeCode.ADVO.name
        })
    }

}