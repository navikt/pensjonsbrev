package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel
import java.util.Objects

@Suppress("unused")
class BestillRedigertBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*>,
    val saksbehandlerValg: SaksbehandlervalgIDSL,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, LetterMarkup.Attachment>,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && saksbehandlerValg == other.saksbehandlerValg
                && felles == other.felles
                && language == other.language
                && letterMarkup == other.letterMarkup
                && redigerteVedlegg == other.redigerteVedlegg
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() = Objects.hash(
        kode,
        letterData,
        saksbehandlerValg,
        felles,
        language,
        letterMarkup,
        redigerteVedlegg,
        alltidValgbareVedlegg,
        pdfVedlegg
    )

    override fun toString() =
        "BestillRedigertBrevRequest(kode=$kode, letterData=$letterData, saksbehandlerValg=$saksbehandlerValg, felles=$felles, language=$language, letterMarkup=$letterMarkup, redigerteVedlegg=$redigerteVedlegg), alltidValgbareVedlegg= $alltidValgbareVedlegg, pdfVedlegg=$pdfVedlegg)"
}
