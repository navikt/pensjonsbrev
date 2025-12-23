package no.nav.pensjon.brev.skribenten.services.brev

import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.GeneriskRedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere

class RenderService(private val brevbakerService: BrevbakerService) {

    suspend fun renderMarkup(brev: Brevredigering, pesysData: BrevdataResponse.Data): LetterMarkupWithDataUsage =
        brevbakerService.renderMarkup(
            brevkode = brev.brevkode,
            spraak = brev.spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = brev.saksbehandlerValg,
            ),
            felles = pesysData.felles.medSignerendeSaksbehandlere(
                SignerendeSaksbehandlere(
                    // Redigerbare brev skal alltid ha saksbehandlers signatur, derfor non-null assertion her.
                    saksbehandler = brev.redigertBrev.signatur.saksbehandlerNavn!!,
                    attesterendeSaksbehandler = brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn,
                )
            ).medAnnenMottakerNavn(null)
        )

}