package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.Api
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

// Disse må være i sync med api-modellen
private const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
private const val P1_VEDLEGG_KEY = "p1Vedlegg"
class P1Service(private val penService: PenService) {
    suspend fun lagreP1Data(p1DataInput: Api.GeneriskBrevdata, brevId: Long, saksId: Long): P1Data? = transaction {
        val brevredigering = Brevredigering.findByIdAndSaksId(brevId, saksId)
        if (brevredigering != null) {
            P1Data.findSingleByAndUpdate(P1DataTable.id eq brevredigering.id) { p1Data ->
                p1Data.p1data = p1DataInput
            } ?: P1Data.new(brevId) {
                p1data = p1DataInput
            }
        } else throw IllegalArgumentException("Fant ikke brev med id: $brevId")
    }

    suspend fun hentP1Data(brevId: Long, saksId: Long): Api.GeneriskBrevdata? = newSuspendedTransaction {
        val brevredigering = Brevredigering.findByIdAndSaksId(brevId, saksId)
        if (brevredigering != null) {
            brevredigering.p1Data?.p1data
                ?: penService.hentP1VedleggData(saksId, brevredigering.spraak).resultOrNull()
        } else throw IllegalArgumentException("Fant ikke brev med id: $brevId")
    }
    
    suspend fun patchMedP1DataOmP1(
        brevdata: Api.GeneriskBrevdata,
        brevkode: Brevkode.Redigerbart,
        brevId: Long,
        saksId: Long
    ): FagsystemBrevdata {
        return if(brevkode.kode() == P1_BREVKODE) {
            brevdata.apply { put(P1_VEDLEGG_KEY, hentP1Data(brevId, saksId)) }
        } else brevdata
    }
}