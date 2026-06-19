package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

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

        // Lettvekts-sjekk mot brevbaker som ikke krever pesysdata. Har ikke malen redigerbare vedlegg,
        // returnerer vi tomt med en gang og unngår det brevdata-kallet.
        if (!brevmalService.harRedigerbareVedlegg(brev.brevkode)) {
            return success(emptyList())
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val vedlegg = brevmalService.hentRedigerbareVedlegg(brev, pesysdata)
            .map { (vedleggId, malTittel) ->
                // Bruk den redigerte tittelen dersom saksbehandler har overstyrt vedlegget, ellers maltittelen.
                val redigertTittel = brev.hentRedigertVedlegg(vedleggId)?.title?.text
                RedigerbartVedleggInfo(
                    vedleggId = vedleggId,
                    tittel = redigertTittel?.joinToString("") { it.text }
                        ?: malTittel.joinToString("") { it.text },
                )
            }

        return success(vedlegg)
    }

    override fun requiresReservasjon(request: Request) = false
}

data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
)
