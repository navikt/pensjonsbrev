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
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, LetterMarkup.Attachment>,
    val redigerbartBrev: BestillRedigerbartBrevRequest<T>,
    // Felta herifra og ned skal slettes, men hvis vi gjør det i denne committen, vil skribenten-bygget feile
    val kode: T? = null,
    val letterData: RedigerbarBrevdata<*>? = null,
    val fagsystemBrevdata: FagsystemBrevdata? = null,
    val saksbehandlervalg: SaksbehandlervalgIDSL? = null,
    val felles: BrevbakerFelles? = null,
    val language: LanguageCode? = null,
    val pdfVedlegg: List<PDFVedleggTittel>? = null,
) : BrevRequest<T> by redigerbartBrev {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequest<*>) return false
        return letterMarkup == other.letterMarkup
                && redigerteVedlegg == other.redigerteVedlegg
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
                && redigerbartBrev == other.redigerbartBrev
    }

    override fun hashCode() = Objects.hash(
        letterMarkup,
        redigerteVedlegg,
        alltidValgbareVedlegg,
        redigerbartBrev
    )

    override fun toString() =
        "BestillRedigertBrevRequest(letterMarkup=$letterMarkup, redigerteVedlegg=$redigerteVedlegg), alltidValgbareVedlegg= $alltidValgbareVedlegg, redigerbartBrev=$redigerbartBrev)"
}
