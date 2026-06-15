package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Arkivsak
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Arkivsaksystem
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Bruker
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Foerstesidetype
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideRequest
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideResponse
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Postboks
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Tema
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.toApi
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import kotlin.collections.listOf

class GenererFoerstesideHandler(
    private val klient: FoerstesidegeneratorClient
) : BrevredigeringHandler<GenererFoerstesideHandler.Request, GenererFoerstesideResponse> {

    data class Request(override val brevId: BrevId, val pid: BrevbakerType.Pid, val sakstype: Sakstype) : BrevredigeringRequest

    override fun requiresReservasjon(request: Request) = false

    override suspend fun handle(request: Request): Outcome<GenererFoerstesideResponse, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val tittel = brev.redigertBrev.title.text.joinToString(" ") { it.text }.trim()

        val response = klient.genererFoersteside(GenererFoerstesideRequest(
            spraakkode = brev.spraak.toApi(),
            netsPostboks = Postboks("1400"), // familie-integrasjoner bruker dette, vi må dobbeltsjekke om det er sant
            bruker = Bruker(
                brukerId = request.pid,
                brukerType = Bruker.BrukerType.PERSON
            ),
            tema = if (request.sakstype.kode == "UFO") { Tema.UFO } else { Tema.PEN }, // TODO: meh
            behandlingstema = null,
            arkivtittel = tittel,
            vedleggsliste = listOf(), // TODO: må finne ut av kva vi sender her
            overskriftstittel = tittel,
            dokumentlisteFoersteside = listOf(), // TODO: må finne ut av kva vi sender her
            foerstesidetype = Foerstesidetype.LOESPOST, // TODO: må vi kunne styre denne?
            enhetsnummer = brev.avsenderEnhetId,
            arkivsak = Arkivsak(
                arkivsaksystem = Arkivsaksystem.PSAK,
                arkivsaksnummer = brev.saksId,
            )
        ))
        return success(response)
    }
}