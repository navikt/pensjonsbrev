package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel
import java.util.Objects

@Deprecated(
    "Bruk heller klassane under for å bestille henholdsvis autobrev eller redigerbart brev." +
            "Håper å få fjerna denne i løpet av saksbehandlervalg-omskrivinga"
)
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

class BestillAutobrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: BrevbakerBrevdata,
    val fagsystemBrevdata: FagsystemBrevdata,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillAutobrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && fagsystemBrevdata == other.fagsystemBrevdata
                && felles == other.felles
                && language == other.language
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() = Objects.hash(kode, letterData, fagsystemBrevdata, felles, language, pdfVedlegg)

    override fun toString() =
        "BestillAutobrevRequest(kode=$kode, letterData=$letterData, fagsystemBrevdata=$fagsystemBrevdata, felles=$felles, language=$language, pdfVedlegg=$pdfVedlegg)"
}

class BestillRedigerbartBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*>,
    val fagsystemBrevdata: FagsystemBrevdata,
    val saksbehandlervalg: SaksbehandlervalgIDSL,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigerbartBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && fagsystemBrevdata == other.fagsystemBrevdata
                && saksbehandlervalg == other.saksbehandlervalg
                && felles == other.felles
                && language == other.language
                && pdfVedlegg == other.pdfVedlegg
    }

    override fun hashCode() =
        Objects.hash(kode, letterData, fagsystemBrevdata, saksbehandlervalg, felles, language, pdfVedlegg)

    override fun toString() =
        "BestillRedigerbartBrevRequest(kode=$kode, letterData=$letterData, fagsystemBrevdata=$fagsystemBrevdata, saksbehandlervalg=$saksbehandlervalg felles=$felles, language=$language, pdfVedlegg=$pdfVedlegg)"
}


interface BrevRequest<T : Brevkode<T>>