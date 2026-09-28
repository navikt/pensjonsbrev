package no.nav.pensjon.brev.api.model

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brevbaker.api.model.*
import java.util.Objects

class BestillRedigertBrevRequestV2<T : Brevkode<T>>(
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, Attachment>,
    val redigerbartBrev: BestillRedigerbartBrevRequest<T>,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequestV2<*>) return false
        return letterMarkup == other.letterMarkup
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
                && redigerteVedlegg == other.redigerteVedlegg
                && redigerbartBrev == other.redigerbartBrev
    }

    override fun hashCode() = Objects.hash(
        letterMarkup,
        alltidValgbareVedlegg,
        redigerteVedlegg,
        redigerbartBrev
    )

    override fun toString() =
        "BestillRedigertBrevRequestV2(letterMarkup=$letterMarkup, alltidValgbareVedlegg=$alltidValgbareVedlegg, redigerteVedlegg=$redigerteVedlegg, redigerbartBrev=$redigerbartBrev)"
}