package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId

/**
 * Forteller front-end hvilke redigerbare vedlegg som faktisk er inkludert i et gitt brev,
 * slik at klienten slipper å hardkode vedlegg-id-er. Inkluderte vedlegg avgjøres av brevbaker
 * (mal + data), og hvert vedlegg identifiseres av sin editableId (vedleggId).
 */
class HentRedigerbareVedleggHandler(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) : BrevredigeringHandler<HentRedigerbareVedleggHandler.Request, List<RedigerbartVedleggInfo>> {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val pesysdata = brevdataService.hentBrevdata(brev)
        val vedlegg = brevmalService.hentRedigerbareVedlegg(brev, pesysdata)
            .map { (vedleggId, tittel) ->
                RedigerbartVedleggInfo(
                    vedleggId = vedleggId,
                    tittel = tittel.joinToString("") { it.text },
                )
            }

        return success(vedlegg)
    }

    override fun requiresReservasjon(request: Request) = false
}

data class RedigerbartVedleggInfo(
    val vedleggId: String,
    val tittel: String,
)
