package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId

class FagsakService(private val penClient: PenClient) {

    suspend fun hentSak(saksId: SaksId): Pen.SakSelection? =
        penClient.hentSak(saksId)

    suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        penClient.hentAvtaleland()

}