package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorService
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.GenererFoerstesideDto
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.GenererFoerstesideResponse
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.services.toApi
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import kotlin.collections.listOf

class FoerstesideHandler(
    private val brevdataService: BrevdataService,
    private val foerstesidegeneratorService: FoerstesidegeneratorService
) : BrevredigeringHandler<FoerstesideHandler.Request, GenererFoerstesideResponse> {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override fun requiresReservasjon(request: Request) = false

    override suspend fun handle(request: Request): Outcome<GenererFoerstesideResponse, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val brevdata = brevdataService.hentBrevdata(brev)

        val response = foerstesidegeneratorService.genererFoersteside(
            GenererFoerstesideDto(
                spraakkode = brev.spraak.toApi(),
                tittel = brev.redigertBrev.title.text.joinToString(" ") { it.text }.trim(),
                vedlegg = listOf(),
                enhetsnummer = brev.avsenderEnhetId,
                brukerId = brevdata.felles.bruker.foedselsnummer.let { BrevbakerType.Pid(it.value) },
                saksnummer = brev.saksId,
            )
        )
        return success(response)
    }
}