package no.nav.pensjon.brevbaker.api.model

import java.time.LocalDate
import java.util.Objects

class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NavEnhet,
    val bruker: Bruker,
    val vergeNavn: String?,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Felles) return false
        return dokumentDato == other.dokumentDato
                && saksnummer == other.saksnummer
                && avsenderEnhet == other.avsenderEnhet
                && bruker == other.bruker
                && vergeNavn == other.vergeNavn
                && signerendeSaksbehandlere == other.signerendeSaksbehandlere
    }

    override fun hashCode() = Objects.hash(dokumentDato, saksnummer, avsenderEnhet, bruker, vergeNavn, signerendeSaksbehandlere)

    override fun toString() =
        "Felles(dokumentDato=$dokumentDato, saksnummer='$saksnummer', avsenderEnhet=$avsenderEnhet, bruker=$bruker, vergeNavn=$vergeNavn, signerendeSaksbehandlere=$signerendeSaksbehandlere)"

    fun medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere?): Felles =
        Felles(
            dokumentDato = this.dokumentDato,
            saksnummer = this.saksnummer,
            avsenderEnhet = this.avsenderEnhet,
            bruker = this.bruker,
            vergeNavn = this.vergeNavn,
            signerendeSaksbehandlere = signerendeSaksbehandlere,
        )
}

class SignerendeSaksbehandlere(
    val saksbehandler: String,
    val attesterendeSaksbehandler: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is SignerendeSaksbehandlere) return false
        return saksbehandler == other.saksbehandler && attesterendeSaksbehandler == other.attesterendeSaksbehandler
    }

    override fun hashCode() = Objects.hash(saksbehandler, attesterendeSaksbehandler)

    override fun toString() =
        "SignerendeSaksbehandlere(saksbehandler='$saksbehandler', attesterendeSaksbehandler=$attesterendeSaksbehandler)"
}

class Bruker(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Bruker) return false
        return foedselsnummer == other.foedselsnummer
                && fornavn == other.fornavn
                && mellomnavn == other.mellomnavn
                && etternavn == other.etternavn
    }

    override fun hashCode() = Objects.hash(foedselsnummer, fornavn, mellomnavn, etternavn)

    override fun toString() =
        "Bruker(foedselsnummer=$foedselsnummer, fornavn='$fornavn', mellomnavn=$mellomnavn, etternavn='$etternavn')"


}

class NavEnhet(
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer
) {
    override fun equals(other: Any?): Boolean {
        if (other !is NavEnhet) return false
        return nettside == other.nettside
                && navn == other.navn
                && telefonnummer == other.telefonnummer
    }

    override fun hashCode() = Objects.hash(nettside, navn, telefonnummer)

    override fun toString() = "NavEnhet(nettside='$nettside', navn='$navn', telefonnummer=$telefonnummer)"


}