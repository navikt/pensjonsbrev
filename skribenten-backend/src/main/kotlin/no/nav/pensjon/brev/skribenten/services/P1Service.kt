package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.Api
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

// Disse må være i sync med api-modellen
const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
const val P1_VEDLEGG_KEY = "p1Vedlegg"

sealed class P1Exception(override val message: String): Exception(){
    class ManglerDataException(message: String): P1Exception(message)
}

interface P1Service {

    suspend fun lagreP1Data(p1DataInput: Api.GeneriskBrevdata, brevId: Long, saksId: Long): P1Data?
    suspend fun hentP1Data(brevId: Long, saksId: Long): Api.GeneriskBrevdata?
    suspend fun patchMedP1DataOmP1(
        brevdataResponse: BrevdataResponse.Data,
        brevkode: Brevkode.Redigerbart,
        brevId: Long,
        saksId: Long
    ): BrevdataResponse.Data
}

class P1ServiceImpl(private val penService: PenService) : P1Service {

    override suspend fun lagreP1Data(p1DataInput: Api.GeneriskBrevdata, brevId: Long, saksId: Long): P1Data = transaction {
        val brevredigering = Brevredigering.findByIdAndSaksId(brevId, saksId)
        if (brevredigering != null) {
            P1Data.findSingleByAndUpdate(P1DataTable.id eq brevredigering.id) { p1Data ->
                p1Data.p1data = p1DataInput
            } ?: P1Data.new(brevId) {
                p1data = p1DataInput
            }
        } else throw IllegalArgumentException("Fant ikke brev med id: $brevId")
    }

    override suspend fun hentP1Data(brevId: Long, saksId: Long): Api.GeneriskBrevdata? = newSuspendedTransaction {
        Brevredigering.findByIdAndSaksId(brevId, saksId)?.let {
            it.p1Data?.p1data
                ?: penService.hentP1VedleggData(saksId, it.spraak)
        }
    }

    override suspend fun patchMedP1DataOmP1(
        brevdataResponse: BrevdataResponse.Data,
        brevkode: Brevkode.Redigerbart,
        brevId: Long,
        saksId: Long
    ): BrevdataResponse.Data = if (brevkode.kode() == P1_BREVKODE) {
        brevdataResponse.copy(
            brevdata = brevdataResponse.brevdata.apply { put(P1_VEDLEGG_KEY, hentP1Data(brevId, saksId)) }
        )
    } else brevdataResponse
}