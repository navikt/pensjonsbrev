package no.nav.pensjon.brev.skribenten.services.brev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.GeneriskRedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class RenderService(private val brevbakerService: BrevbakerService) {

    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        saksbehandlerValg: SaksbehandlerValg,
        pesysData: BrevdataResponse.Data
    ): LetterMarkupWithDataUsage =
        brevbakerService.renderMarkup(
            brevkode = brevkode,
            spraak = spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = saksbehandlerValg,
            ),
            felles = pesysData.felles
        )

    suspend fun renderMarkup(brev: Brevredigering, pesysData: BrevdataResponse.Data): LetterMarkupWithDataUsage =
        renderMarkup(
            brevkode = brev.brevkode,
            spraak = brev.spraak,
            saksbehandlerValg = brev.saksbehandlerValg,
            pesysData = pesysData,
        )

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
            alltidValgbareVedlegg = brev.valgteVedlegg?.valgteVedlegg ?: emptyList(),
        ).file
}