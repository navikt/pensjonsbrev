package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

class FoerstesidegeneratorService(val klient: FoerstesidegeneratorClient) {
    suspend fun genererFoersteside(request: GenererFoerstesideDto): GenererFoerstesideResponse {
        val request = GenererFoerstesideRequest(
            spraakkode = request.spraakkode,
            netsPostboks = Postboks("1400"), // familie-integrasjoner bruker dette, vi må dobbeltsjekke om det er sant
            bruker = Bruker(
                brukerId = request.brukerId,
                brukerType = Bruker.BrukerType.PERSON
            ),
            tema = Tema.FOR,
            behandlingstema = null,
            arkivtittel = request.tittel,
            vedleggsliste = request.vedlegg,
            overskriftstittel = request.tittel,
            dokumentlisteFoersteside = listOf(), // TODO: må finne ut av kva vi sender her
            foerstesidetype = Foerstesidetype.LOESPOST, // TODO: må vi kunne styre denne?
            enhetsnummer = request.enhetsnummer,
            arkivsak = Arkivsak(
                arkivsaksystem = Arkivsaksystem.PSAK,
                arkivsaksnummer = request.saksnummer.id,
            )
        )
        return klient.genererFoersteside(request)
    }
}

data class GenererFoerstesideDto(
    val spraakkode: SpraakKode,
    val tittel: String,
    val vedlegg: List<String>,
    val enhetsnummer: EnhetId,
    val brukerId: BrevbakerType.Pid,
    val saksnummer: SaksId
)

data class GenererFoerstesideRequest(
    val spraakkode: SpraakKode,
    val adresse: Any? = null,
    val netsPostboks: Postboks,
    val avsender: Avsender? = null,
    val bruker: Bruker,
    val ukjentBrukerPersoninfo: String? = null,
    val tema: Tema,
    val behandlingstema: String?, // TODO: korleis finn vi denne?
    val arkivtittel: String,
    val vedleggsliste: List<String>,
    val navSkjemaId: String? = null,
    val overskriftstittel: String,
    val dokumentlisteFoersteside: List<String>,
    val foerstesidetype: Foerstesidetype,
    val enhetsnummer: EnhetId,
    val arkivsak: Arkivsak,
)

data class GenererFoerstesideResponse(
    val foersteside: ByteArray,
    val loepenummer: Loepenummer,
)

@JvmInline
value class Loepenummer(val value: String)

@JvmInline
value class Postboks(val value: String) {
    init {
        require(value.toIntOrNull() != null)
    }
}

data class Avsender(
    val avsenderId: BrevbakerType.Pid, // TODO, kan også vera orgid
    val avsenderNavn: String
)

data class Bruker(
    val brukerId: BrevbakerType.Pid, // TODO, kan også vera orgid
    val brukerType: BrukerType,
) {
    enum class BrukerType {
        PERSON, ORGANISASJON
    }
}

enum class Tema {
    FOR; // TODO: kva har vi for pensjon eller uføre her? FOR er Foreldrepenger
}

enum class Foerstesidetype {
    ETTERSENDELSE,
    LOESPOST,
    SKJEMA,
    NAV_INTERN
}

data class Arkivsak(
    val arkivsaksystem: Arkivsaksystem,
    val arkivsaksnummer: Long,
)

enum class Arkivsaksystem {
    PSAK,
}