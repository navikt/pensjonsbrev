package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel
import java.util.Objects

@Suppress("unused")
class BestillBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf()
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && felles == other.felles
                && language == other.language
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() = Objects.hash(kode, letterData, felles, language, pdfVedlegg)

    override fun toString() = "BestillBrevRequest(kode=$kode, letterData=$letterData, felles=$felles, language=$language, pdfVedlegg=$pdfVedlegg)"
}

interface BrevRequest<T : Brevkode<T>>