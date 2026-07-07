package no.nav.pensjon.brev.skribenten.fagsystem.pesys

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentP1DataHandler
import no.nav.pensjon.brev.skribenten.common.getOrElse
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

// Disse må være i sync med api-modellen
const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
const val P1_VEDLEGG_KEY = "p1Vedlegg"

sealed class P1Exception(override val message: String): Exception(){
    class ManglerDataException(message: String): P1Exception(message)
}

interface P1Service {

    suspend fun patchMedP1DataOmP1(
        brevdataResponse: BrevdataResponse.Data,
        brevkode: Brevkode.Redigerbart,
        brevId: BrevId?,
        saksId: SaksId
    ): BrevdataResponse.Data
}

// Selve henting og lagring av P1-data skjer nå i egne handlere, se HentP1DataHandler og LagreP1DataHandler,
// slik at logikken kan gjenbrukes fra routes/SakBrev.kt uten å måtte gå via P1Service.
class P1ServiceImpl(private val hentP1DataHandler: HentP1DataHandler) : P1Service {

    override suspend fun patchMedP1DataOmP1(
        brevdataResponse: BrevdataResponse.Data,
        brevkode: Brevkode.Redigerbart,
        brevId: BrevId?,
        saksId: SaksId
    ): BrevdataResponse.Data = if (brevkode.kode() == P1_BREVKODE && brevId != null) {
        val p1Data = hentP1DataHandler(HentP1DataHandler.Request(brevId, saksId))
            ?.getOrElse { error("Uventet feil ved henting av P1-data for brev $brevId") }

        brevdataResponse.copy(
            brevdata = brevdataResponse.brevdata.apply { put(P1_VEDLEGG_KEY, p1Data) }
        )
    } else brevdataResponse
}