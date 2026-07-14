package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.jetbrains.exposed.v1.jdbc.Database

class HentAlltidValgbareVedleggHandler(
    private val brevbakerService: BrevbakerService,
    database: Database,
) : PartlyTransactionHandler<HentAlltidValgbareVedleggHandler.Request, LanguageCode?, List<ValgbartVedlegg>, Nothing>(database) {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<LanguageCode, Nothing>? =
        BrevredigeringEntity.findById(request.brevId)?.spraak?.let { success(it) }

    override suspend fun executeOutsideTransaction(request: Request, response: LanguageCode?): Outcome<List<ValgbartVedlegg>, Nothing> {
        val vedlegg = brevbakerService.getAlltidValgbareVedlegg(request.brevId).map {
            ValgbartVedlegg(
                kode = it.kode,
                visningstekst = it.visningstekst,
                spraak = it.spraak,
                tilgjengeligForSpraak = it.spraak.contains(response),
            )
        }.sortedBy { it.visningstekst }

        return success(vedlegg)
    }
}

data class ValgbartVedlegg(
    val kode: String,
    val visningstekst: String,
    val spraak: Set<LanguageCode>,
    val tilgjengeligForSpraak: Boolean,
)
