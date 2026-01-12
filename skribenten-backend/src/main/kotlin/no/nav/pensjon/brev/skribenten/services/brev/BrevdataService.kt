package no.nav.pensjon.brev.skribenten.services.brev

import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.services.PenService

class BrevdataService(private val penService: PenService) {

    suspend fun hentBrevdata(brev: Brevredigering) =
        penService.hentPesysBrevdata(
            saksId = brev.saksId,
            vedtaksId = brev.vedtaksId,
            brevkode = brev.brevkode,
            avsenderEnhetsId = brev.avsenderEnhetId,
        )
}