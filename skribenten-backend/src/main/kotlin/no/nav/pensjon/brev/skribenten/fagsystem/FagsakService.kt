package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import java.time.LocalDate

class FagsakService(private val penClient: PenClient) {

    suspend fun hentSak(saksId: SaksId): Fagsak? =
        penClient.hentSak(saksId)?.let { sak ->
            Fagsak(
                saksId = sak.saksId,
                foedselsdato = sak.foedselsdato,
                navn = Fagsak.Navn(sak.navn.fornavn, sak.navn.mellomnavn, sak.navn.etternavn),
                sakType = sak.sakType,
                pid = sak.pid,
                behandlingsnumre = sak.behandlingsnumre
            )
        }

    suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        penClient.hentAvtaleland()
}

@JvmInline
value class Behandlingsnummer(val value: String)

data class Fagsak(
    val saksId: SaksId,
    val foedselsdato: LocalDate,
    val navn: Navn,
    val sakType: Sakstype,
    val pid: BrevbakerType.Pid,
    val behandlingsnumre: List<Behandlingsnummer>,
    val tema: Tema = if (sakType.kode == "UFO") {
        Tema.UFO
    } else {
        Tema.PEN
    }, // TODO: send med frå PEN
) {
    data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String)
}

enum class Tema {
    PEN, UFO
}