package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.skribenten.fagsystem.domain.Tema
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
                behandlingsnumre = sak.behandlingsnumre,
                tema = sak.tema,
            )
        }

    suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        penClient.hentAvtaleland()
}

@JvmInline
value class Behandlingsnummer(val value: String) {
    init {
        require(value.length == 4) { "Behandlingsnummer må være 4 tegn, var ${value.length}" }
    }
}

data class Fagsak(
    val saksId: SaksId,
    val foedselsdato: LocalDate,
    val navn: Navn,
    val sakType: Sakstype,
    val pid: BrevbakerType.Pid,
    val behandlingsnumre: List<Behandlingsnummer>,
    val tema: Tema,
) {
    data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String) {
        init {
            require(fornavn.length < 128) { "Fornavn må være mindre enn 128 bokstaver" }
            require((mellomnavn?.length ?: 0) < 128) { "Mellomnavn må være mindre enn 128 bokstaver" }
            require(etternavn.length < 128) { "Etternavn må være mindre enn 128 bokstaver" }
        }
    }
}