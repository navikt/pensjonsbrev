package no.nav.pensjon.brev.api.model

import java.time.LocalDate

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val bruker: Bruker,
    val mottaker: Adresse,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
) {
    constructor() : this(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1234",
        avsenderEnhet = NAVEnhet(),
        bruker = Bruker(),
        mottaker = Adresse(),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(),
    )
}

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String) {
    constructor() : this(
        adresseLinje1 = "adresseLinje1Test 1234",
        postNr = "12345",
        postSted = "postStedTest"
    )
}

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String) {
    constructor() : this(
        saksbehandler = "saksbehandlerTest", attesterendeSaksbehandler = "attesterendeSaksbehandlerTest"
    )
}

data class Bruker(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
) {
    constructor() : this(
        foedselsnummer = Foedselsnummer("12345"),
        fornavn = "Test-fornavn",
        mellomnavn = "Test-mellomnavn",
        etternavn = "Test-etternavn",
    )
}

data class Adresse(
    val linje1: String,
    val linje2: String,
    val linje3: String? = null,
    val linje4: String? = null,
    val linje5: String? = null,
) {
    constructor() : this(
        linje1 = "Test 1",
        linje2 = "Test 2",
        linje3 = "Test 3",
        linje4 = "Test 4",
        linje5 = "Test 5",
    )
}

data class NAVEnhet(
    val returAdresse: ReturAdresse,
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer,
) {
    constructor() : this(
        returAdresse = ReturAdresse(),
        nettside = "nettside.nettside",
        navn = "Person",
        telefonnummer = Telefonnummer("1234"),
    )
}