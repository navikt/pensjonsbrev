package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import java.util.Objects

/**
 * v2-varianten av [BestillRedigertBrevRequest]. Bærer redigert markup på [LetterMarkupV2]-form slik at
 * konsumenter kan produsere PDF fra v2-markup via `/v2/letter/redigerbar/pdf`.
 *
 * Ligger foreløpig i pensjon-brevbaker (ikke i den publiserte api-model-common) mens v2 er under
 * inkrementell utrulling. Flyttes til api-model-common når en ekstern konsument (Skribenten) skal
 * bruke den, og da med versjonering av det publiserte artefaktet.
 */
@Suppress("unused")
class BestillRedigertBrevRequestV2<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkupV2,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, LetterMarkupV2.Attachment> = emptyMap(),
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequestV2<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && felles == other.felles
                && language == other.language
                && letterMarkup == other.letterMarkup
                && redigerteVedlegg == other.redigerteVedlegg
                && alltidValgbareVedlegg == other.alltidValgbareVedlegg
    }

    override fun hashCode() = Objects.hash(kode, letterData, felles, language, letterMarkup, redigerteVedlegg, alltidValgbareVedlegg)

    override fun toString() =
        "BestillRedigertBrevRequestV2(kode=$kode, letterData=$letterData, felles=$felles, language=$language, letterMarkup=$letterMarkup, redigerteVedlegg=$redigerteVedlegg, alltidValgbareVedlegg=$alltidValgbareVedlegg)"
}
