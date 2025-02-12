package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

data class FinnSamhandlerResponseDto(val samhandlere: List<Samhandler>, val failureType: String?) {
    constructor(samhandlere: List<Samhandler>) : this(samhandlere, null)
    constructor(failure: String) : this(emptyList(), failure)

    data class Samhandler(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
        val idTSSEkstern: String,
    )
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(FinnSamhandlerRequestDto.DirekteOppslag::class, name = "DIREKTE_OPPSLAG"),
    JsonSubTypes.Type(FinnSamhandlerRequestDto.Organisasjonsnavn::class, name = "ORGANISASJONSNAVN"),
    JsonSubTypes.Type(FinnSamhandlerRequestDto.Personnavn::class, name = "PERSONNAVN"),
)
sealed class FinnSamhandlerRequestDto {
    abstract val samhandlerType: SamhandlerTypeCode

    data class DirekteOppslag(
        val identtype: String,
        val id: String,
        override val samhandlerType: SamhandlerTypeCode,
    ) : FinnSamhandlerRequestDto()

    data class Organisasjonsnavn(
        val innlandUtland: String,
        val navn: String,
        override val samhandlerType: SamhandlerTypeCode,
    ) : FinnSamhandlerRequestDto()

    data class Personnavn(
        val fornavn: String,
        val etternavn: String,
        override val samhandlerType: SamhandlerTypeCode,
    ) : FinnSamhandlerRequestDto()
}

enum class SamhandlerTypeCode {
    AA, // Ambulansearbeider
    ADVO, // Advokat / Jurist
    AFPO, // AFP-ordning
    AK, // Arbeidskontor
    AMI, // Arbeidsmarkedsinstitutt
    AN, // Ankenemnda for sykepenger
    AP, // Audiopedagog
    APOP, // Apotek - privat
    APOS, // Apotek - sykehus
    ART, // Arbeidstilsynet
    ASEN, // Arbeidslivssenter
    AT, // Apotektekniker
    ATU, // Attfringsutvalg
    AU, // Audiograf
    BAND, // Bandasjist
    BB, // Begravelsesbyr
    BBE, // Bobestyrer
    BEH, // Behandler
    BF, // Bidragsfogden
    BH, // Barnehage
    BHT, // Bedriftshelsetjeneste
    BI, // Bioingenir
    BU, // Bilutvalget
    EOS, // ES-Land
    ET, // Ergoterapeut
    FA, // Farmasyt
    FB, // Fiskehelsebiolog
    FFU, // Folketrygdkontoret for utenlandssaker
    FKL, // Fellesordningen LO/NHO
    FL, // Fylkeslegen
    FO, // Fotterapeut
    FS, // Forsikringsselskap
    FT, // Fysioterapeut
    FTK, // Fylkestrygdekontor
    FULL, // Fullmektig
    GVS, // Grunn- og videregende skole
    HE, // Helsesekretr
    HF, // Helsefagarbeider
    HFO, // Helseforetak
    HJS, // Hjemmesykepleien
    HMS, // Hjelpemiddelsentral
    HP, // Hjelpepleier
    HPRD, // HPR-dummy
    HS, // Helsesster
    HSTA, // Helsestasjon
    HSV, // Hovedstevnevitnet
    HTF, // Helsetjeneste forvaltning
    HU, // Hyskole/universitet
    INST, // Institusjon
    IS, // Innkrevingssentralen
    JO, // Jordmor
    KA, // Klage- og Ankeenhet
    KE, // Klinisk ernringsfysiolog
    KEMN, // Kemner
    KI, // Kiropraktor
    KOMM, // Kommune
    KON, // Konvensjonsland
    KRED, // Kreditor
    KRU, // Klageinstans reiseutgift
    KYND, // Sakkyndig
    LABO, // Laboratorium - offentlig
    LABP, // Laboratorium - privat
    LARO, // Lab/rntgen - privat
    LBS, // Landsdekkende bilsenter
    LE, // Lege
    LEBE, // Bedriftslege
    LEFA, // Fastlege
    LEKO, // Kommunelege
    LERA, // Rdgivende lege
    LFK, // Legemiddel fagkontor
    LHK, // Leverandr helsetrygdkort
    LK, // Ligningskontor
    LM, // Leverandr Motorkjrety
    LOGO, // Logoped
    LRB, // Lsreregister Brnnysund
    LS, // Legesenter
    MT, // Manuellterapeut
    NB, // Namsmannen
    OA, // Omsorgsarbeider
    OI, // Ortopediingenir
    OM, // Ombygger bil
    OP, // Optiker
    OR, // Ortoptist
    ORVE, // Ortopedisk verksted
    PE, // Perfusjonist
    PF, // Provisorfarmasyt
    POLI, // Poliklinikk
    PPT, // Psykologisk pedagogisk tjeneste
    PS, // Psykolog
    RA, // Radiograf
    REGO, // Regional oppgjrsenhet
    RF, // Reseptarfarmasyt
    RFK, // Rogaland fylkestrygdekontor
    RHFO, // Regionalt helseforetak
    RKK, // Regionklagekontor
    ROL, // Rdgivende overlege
    RONO, // Rntgeninstitutt - offentlig
    RONP, // Rntgeninstitutt - privat
    RT, // Rdgivende tannlege
    RTK, // Regionstrygdekontor
    RTV, // Rikstrygdeverket
    SAD, // Samleadressat
    SFK, // Sentralfylkestrygdekontor
    SK, // Sosialkontor
    SP, // Sykepleier
    SR, // Skifteretten
    STKO, // Stttekontakt
    SUD, // Statens utlendingsdirektorat
    SYKH, // Sykehus
    TEPE, // Tjenestepensjonsordningar
    TH, // Tannhelsesekretr
    TI, // Takseringsinstanser
    TK, // Trygdekontor
    TL, // Tannlege
    TLO, // Tannlege offentlig
    TOLK, // Tolk
    TP, // Tannpleier
    TR, // Trygderetten
    TRAN, // Transportr
    TT, // Tanntekniker
    UTL, // Utenlandske samhandler
    VE, // Veterinr
    VIRT, // Virtuelt trygdekontor
    VP, // Vernepleier
    X, // Diverse
    YH, // Yrkeshygieniker
    YM, // Yrkesmedisiner
}
