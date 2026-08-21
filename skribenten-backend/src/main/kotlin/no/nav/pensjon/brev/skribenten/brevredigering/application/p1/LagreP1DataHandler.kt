package no.nav.pensjon.brev.skribenten.brevredigering.application.p1

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.vedlegg.P1Data
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto
import org.jetbrains.exposed.v1.core.eq

class LagreP1DataHandler(private val brevtilgang: Brevtilgang) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val p1Data: P1RedigerbarDto,
    )

    suspend operator fun invoke(request: Request): Outcome<P1RedigerbarDto, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            val entity = P1Data.findSingleByAndUpdate(P1DataTable.id eq brev.id) { p1Data ->
                p1Data.p1data = request.p1Data
            } ?: P1Data.new(request.brevId) {
                p1data = request.p1Data
            }

            success(entity.p1data)
        }
}
