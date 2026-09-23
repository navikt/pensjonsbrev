package no.nav.pensjon.brev.api.model

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brevbaker.api.model.*
import java.util.Objects

class BestillRedigertBrevRequestV2<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*>,
    val fagsystemBrevdata: FagsystemBrevdata?, // todo: endre til påkrevd når brevbaker har gått over
    val saksbehandlervalg: SaksbehandlervalgIDSL?, // todo: endre til påkrevd når brevbaker har gått over
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, Attachment>,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
    val redigerbartBrev: BestillRedigerbartBrevRequest<T>?,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequestV2<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && fagsystemBrevdata == other.fagsystemBrevdata
                && saksbehandlervalg == other.saksbehandlervalg
                && felles == other.felles
                && language == other.language
                && letterMarkup == other.letterMarkup
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
                && redigerteVedlegg == other.redigerteVedlegg
                && pdfVedlegg == other.pdfVedlegg
                && redigerbartBrev == other.redigerbartBrev
    }

    override fun hashCode() = Objects.hash(
        kode,
        letterData,
        fagsystemBrevdata,
        saksbehandlervalg,
        felles,
        language,
        letterMarkup,
        alltidValgbareVedlegg,
        redigerteVedlegg,
        pdfVedlegg,
        redigerbartBrev
    )

    override fun toString() =
        "BestillRedigertBrevRequestV2(kode=$kode, letterData=$letterData, fagsystemBrevdata=$fagsystemBrevdata, saksbehandlervalg=$saksbehandlervalg, felles=$felles, language=$language, letterMarkup=$letterMarkup, alltidValgbareVedlegg=$alltidValgbareVedlegg, redigerteVedlegg=$redigerteVedlegg, pdfVedlegg=$pdfVedlegg, redigerbartBrev=$redigerbartBrev)"
}