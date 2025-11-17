package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import java.util.Objects

@Suppress("unused")
class BestillAutobrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: AutobrevData,
    val felles: Felles,
    val language: LanguageCode,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillAutobrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && felles == other.felles
                && language == other.language
    }

    override fun hashCode() = Objects.hash(kode, letterData, felles, language)

    override fun toString() = "BestillAutobrevRequest(kode=$kode, letterData=$letterData, felles=$felles, language=$language)"
}

@Suppress("unused")
class BestillRedigerbartBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: Felles,
    val language: LanguageCode,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigerbartBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && felles == other.felles
                && language == other.language
    }

    override fun hashCode() = Objects.hash(kode, letterData, felles, language)

    override fun toString() = "BestillRedigerbartBrevRequest(kode=$kode, letterData=$letterData, felles=$felles, language=$language)"
}

@Suppress("unused")
class BestillRedigertBrevRequest<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: Felles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
) : BrevRequest<T> {
    override fun equals(other: Any?): Boolean {
        if (other !is BestillRedigertBrevRequest<*>) return false
        return kode == other.kode
                && letterData == other.letterData
                && felles == other.felles
                && language == other.language
                && letterMarkup == other.letterMarkup
    }

    override fun hashCode() = Objects.hash(kode, letterData, felles, language, letterMarkup)

    override fun toString() = "BestillRedigertBrevRequest(kode=$kode, letterData=$letterData, felles=$felles, language=$language, letterMarkup=$letterMarkup)"
}

interface BrevRequest<T : Brevkode<T>>