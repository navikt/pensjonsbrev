package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BestillRedigerbartBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import java.util.Objects

@Suppress("unused")
class BestillRedigertBrevRequest<T : Brevkode<T>>(
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, LetterMarkup.Attachment>,
    val redigerbartBrev: BestillRedigerbartBrevRequest<T>,
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
