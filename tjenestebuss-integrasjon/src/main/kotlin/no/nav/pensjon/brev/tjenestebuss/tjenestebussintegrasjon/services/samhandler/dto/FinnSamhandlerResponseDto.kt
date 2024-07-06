package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

data class FinnSamhandlerResponseDto(val samhandlere: List<Samhandler>, val failureType: String?) {
    constructor(samhandlere: List<Samhandler>) : this(samhandlere, null)
    constructor(failure: String) : this(emptyList(), failure)

    data class Samhandler(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: Identtype,
        val idTSSEkstern: String,
        val innUtland: InnUtland?,
    )
}

/**
 * https://github.com/navikt/pesys/blob/a06c86a76bd38021445f685f417c68c5689612f6/pen/consumer/nav-consumer-pensjon-pen-java/src/main/java/no/nav/consumer/pensjon/pen/samhandler/IdType.kt
 */
enum class Identtype {
    ORG, //Norsk orgnr
    DNR, //Norsk D-nummer
    EOS, //EØS-nummer
    FDAT, //Fødselsdato
    FNR, //Fødselsnummer
    HNR, //H-nummer
    HPR, //Objec Identifier
    INST, //Institusjonsnr
    KOMM, //Kommunenr
    LAND, //Land
    LNR, //Loc-nr
    PNR, //Norsk pnr
    TKNR, //Trygdekontornr
    TPNR, //TP leverandør
    UTOR, //Utenlandsk orgnr
    UTPE, //Utenlandsk pnr
}

enum class InnUtland {
    INNLAND,
    UTLAND,
    ALLE;
}
