package no.nav.pensjon.brev.api.model

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.api.model.maler.BrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel

data class BestillRedigertBrevRequestV2<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, Attachment>,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
) : BrevRequest<T>
