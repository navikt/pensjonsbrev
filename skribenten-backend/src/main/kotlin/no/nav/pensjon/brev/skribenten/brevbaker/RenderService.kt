package no.nav.pensjon.brev.skribenten.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedlegg
import no.nav.pensjon.brev.skribenten.brevredigering.domain.withDokumentDato
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.v2.toMarkup as toMarkupV2
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel

class RenderService(private val brevbakerService: BrevbakerService) {

    // TODO: For å kunne støtte forskjellige fagsystem, som selv skal ha eierskap til maler, så må renderPdf ta inn LetterMarkup for brev og vedlegg.
    suspend fun renderPdf(brev: Brevredigering, pesysData: BrevdataResponse.Data, pdfVedlegg: List<PDFVedleggTittel>): LetterResponse =
        when (val redigertBrev = brev.redigertBrev.withDokumentDato(pesysData.felles.dokumentDato)) {
            is VersjonertBrev.V1 -> brevbakerService.renderPdf(
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                fagsystemBrevdata = pesysData.brevdata,
                saksbehandlervalg = brev.saksbehandlerValg.somSaksbehandlervalgIDSL(),
                felles = pesysData.felles,
                redigertBrev = redigertBrev.brev.toMarkup(),
                alltidValgbareVedlegg = brev.valgteVedlegg,
                redigerteVedlegg = brev.redigerteVedlegg.associate { it.vedleggId to (it.redigertVedlegg as VersjonertVedlegg.V1).vedlegg.toMarkup() },
                pdfVedlegg = pdfVedlegg
            )

            is VersjonertBrev.V2 -> brevbakerService.renderPdfV2(
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                fagsystemBrevdata = pesysData.brevdata,
                saksbehandlervalg = brev.saksbehandlerValg.somSaksbehandlervalgIDSL(),
                felles = pesysData.felles,
                redigertBrev = redigertBrev.brev.toMarkupV2(),
                alltidValgbareVedlegg = brev.valgteVedlegg,
                redigerteVedlegg = brev.redigerteVedlegg.associate { it.vedleggId to (it.redigertVedlegg as VersjonertVedlegg.V2).vedlegg.toMarkupV2() },
                pdfVedlegg = pdfVedlegg
            )
        }
}
