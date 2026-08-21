package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.LanguageCode

class HentAlltidValgbareVedleggHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    suspend operator fun invoke(request: Request): Outcome<List<ValgbartVedlegg>, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            val spraakIBrevet = brev.spraak

            val vedlegg = brevmalService.getAlltidValgbareVedlegg().map {
                ValgbartVedlegg(
                    kode = it.kode,
                    visningstekst = it.visningstekst,
                    spraak = it.spraak,
                    tilgjengeligForSpraak = it.spraak.contains(spraakIBrevet),
                )
            }.sortedBy { it.visningstekst }

            success(vedlegg)
        }
}

data class ValgbartVedlegg(
    val kode: String,
    val visningstekst: String,
    val spraak: Set<LanguageCode>,
    val tilgjengeligForSpraak: Boolean,
)
