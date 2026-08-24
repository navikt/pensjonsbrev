package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.jetbrains.exposed.v1.jdbc.Database

class HentAlltidValgbareVedleggHandler(
    private val brevmalService: BrevmalService,
    database: Database,
) : TransactionHandler<HentAlltidValgbareVedleggHandler.Request, List<ValgbartVedlegg>, Nothing>(database) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val sakstype: Sakstype,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<List<ValgbartVedlegg>, Nothing>? {
        val spraakIBrevet = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId)?.spraak ?: return null

        val vedlegg = brevmalService.getAlltidValgbareVedlegg(request.sakstype).map {
            ValgbartVedlegg(
                kode = it.kode,
                visningstekst = it.visningstekst,
                spraak = it.spraak,
                tilgjengeligForSpraak = it.spraak.contains(spraakIBrevet),
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
