package no.nav.pensjon.brev.skribenten.brevbaker

import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.common.GeneriskRedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.letter.toMarkup

class RenderService(private val brevbakerService: BrevbakerService) {

    // TODO: For å kunne støtte forskjellige fagsystem, som selv skal ha eierskap til maler, så må renderPdf ta inn LetterMarkup for brev og vedlegg.
    suspend fun renderPdf(brev: Brevredigering, pesysData: BrevdataResponse.Data): ByteArray =
        brevbakerService.renderPdf(
            brevkode = brev.brevkode,
            spraak = brev.spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = brev.saksbehandlerValg,
            ),
            felles = pesysData.felles,
            redigertBrev = brev.redigertBrev.withSakspart(dokumentDato = pesysData.felles.dokumentDato)
                .toMarkup(),
            alltidValgbareVedlegg = brev.valgteVedlegg,
        ).file
}