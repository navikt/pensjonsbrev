package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BestillRedigerbartBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
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
    val fagsystemBrevdata: FagsystemBrevdata?, // todo: endre til påkrevd når brevbaker har gått over
    val saksbehandlervalg: SaksbehandlervalgIDSL?, // todo: endre til påkrevd når brevbaker har gått over
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, LetterMarkup.Attachment>,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
    val redigerbartBrev: BestillRedigerbartBrevRequest<T>?,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && fagsystemBrevdata == other.fagsystemBrevdata
                && saksbehandlervalg == other.saksbehandlervalg
                && felles == other.felles
                && language == other.language
                && letterMarkup == other.letterMarkup
                && redigerteVedlegg == other.redigerteVedlegg
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
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
        redigerteVedlegg,
        alltidValgbareVedlegg,
        pdfVedlegg,
        redigerbartBrev
    )

    override fun toString() =
        "BestillRedigertBrevRequest(kode=$kode, letterData=$letterData, fagsystemBrevdata=$fagsystemBrevdata, saksbehandlervalg=$saksbehandlervalg, felles=$felles, language=$language, letterMarkup=$letterMarkup, redigerteVedlegg=$redigerteVedlegg), alltidValgbareVedlegg= $alltidValgbareVedlegg, pdfVedlegg=$pdfVedlegg, redigerbartBrev=$redigerbartBrev)"
}
