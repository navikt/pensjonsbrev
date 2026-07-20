package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.domain.Tema
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Arkivsak
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Bruker
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Foerstesidetype
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideRequest
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.GenererFoerstesideResponse
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient.Postboks
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import kotlin.collections.listOf

class GenererFoerstesideHandler(
    private val klient: FoerstesidegeneratorClient
) : UseCaseHandler<GenererFoerstesideHandler.Request, GenererFoerstesideResponse, Nothing> {

    data class Request(
        override val brevId: BrevId,
        val pid: BrevbakerType.Pid,
        val sakstype: Sakstype,
        val tema: Tema,
        val vedlegg: List<Tittel>,
    ) : BrevredigeringRequest

    @JvmInline
    value class Tittel(val tittel: String)

    override suspend fun invoke(request: Request): Outcome<GenererFoerstesideResponse, Nothing>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val tittel = brev.redigertBrev.title.text.joinToString(" ") { it.text }.trim()

        val response = klient.genererFoersteside(GenererFoerstesideRequest(
            spraakkode = when (brev.spraak) {
                LanguageCode.BOKMAL -> FoerstesidegeneratorClient.Spraakkode.NB
                LanguageCode.NYNORSK -> FoerstesidegeneratorClient.Spraakkode.NN
                LanguageCode.ENGLISH -> FoerstesidegeneratorClient.Spraakkode.EN
            },
            netsPostboks = Postboks("1400"),
            bruker = Bruker(
                brukerId = request.pid,
                brukerType = Bruker.BrukerType.PERSON
            ),
            tema = request.tema,
            avsender = null, // TODO: finn ut om vi skal sende med noko her
            behandlingstema = null, // TODO: korleis finn vi denne?
            arkivtittel = tittel,
            vedleggsliste = request.vedlegg.map { it.tittel },
            overskriftstittel = tittel,
            dokumentlisteFoersteside = listOf(tittel) + request.vedlegg.map { it.tittel },
            foerstesidetype = Foerstesidetype.LOESPOST, // TODO: må vi kunne styre denne?
            enhetsnummer = brev.avsenderEnhetId,
            arkivsak = Arkivsak(
                arkivsaksystem = Arkivsak.Arkivsaksystem.PSAK,
                arkivsaksnummer = brev.saksId,
            )
        ))
        return success(response)
    }
}