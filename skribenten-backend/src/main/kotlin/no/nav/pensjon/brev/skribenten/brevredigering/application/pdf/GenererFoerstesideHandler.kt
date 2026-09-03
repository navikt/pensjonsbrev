package no.nav.pensjon.brev.skribenten.brevredigering.application.pdf

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
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
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import kotlin.collections.listOf

private const val STANDARD_NETS_POSTBOKS = "1400"

class GenererFoerstesideHandler(
    private val brevtilgang: Brevtilgang,
    private val klient: FoerstesidegeneratorClient,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val pid: BrevbakerType.Pid,
        val sakstype: Sakstype,
        val tema: Tema,
        val vedlegg: List<Tittel>,
    )

    @JvmInline
    value class Tittel(val tittel: String)

    suspend operator fun invoke(request: Request): Outcome<GenererFoerstesideResponse, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
        val tittel = brev.redigertBrev.title.text.joinToString(" ") { it.text }.trim()

        val response = klient.genererFoersteside(GenererFoerstesideRequest(
            spraakkode = when (brev.spraak) {
                LanguageCode.BOKMAL -> FoerstesidegeneratorClient.Spraakkode.NB
                LanguageCode.NYNORSK -> FoerstesidegeneratorClient.Spraakkode.NN
                LanguageCode.ENGLISH -> FoerstesidegeneratorClient.Spraakkode.EN
            },
            netsPostboks = Postboks(STANDARD_NETS_POSTBOKS),
            bruker = Bruker(
                brukerId = request.pid,
                brukerType = Bruker.BrukerType.PERSON
            ),
            tema = request.tema,
            arkivtittel = tittel,
            vedleggsliste = request.vedlegg.map { it.tittel },
            overskriftstittel = tittel,
            dokumentlisteFoersteside = listOf(tittel) + request.vedlegg.map { it.tittel },
            foerstesidetype = Foerstesidetype.SKJEMA,
            enhetsnummer = brev.avsenderEnhetId,
            arkivsak = Arkivsak(
                arkivsaksystem = Arkivsak.Arkivsaksystem.PSAK,
                arkivsaksnummer = brev.saksId,
            )
        ))
            success(response)
        }
}