package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.util.Objects

@Suppress("unused")
interface TemplateDescription {
    val name: String
    val letterDataClass: String
    val languages: List<LanguageCode>
    val metadata: LetterMetadata

    class Autobrev(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata
    ): TemplateDescription {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Autobrev) return false
            return name == other.name && letterDataClass == other.letterDataClass && languages == other.languages && metadata == other.metadata
        }

        override fun hashCode() = Objects.hash(name, letterDataClass, languages, metadata)
        override fun toString(): String =
            "Autobrev(name='$name', letterDataClass='$letterDataClass', languages=$languages, metadata=$metadata)"
    }

    class Redigerbar(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata,
        val kategori: IBrevkategori,
        val brevkontekst: Brevkontekst,
        val sakstyper: Set<Sakstype>,
    ): TemplateDescription {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Redigerbar) return false
            return name == other.name && letterDataClass == other.letterDataClass && languages == other.languages && metadata == other.metadata && kategori == other.kategori && brevkontekst == other.brevkontekst && sakstyper == other.sakstyper
        }

        override fun hashCode() = Objects.hash(name, letterDataClass, languages, metadata, kategori, brevkontekst, sakstyper)
        override fun toString(): String =
            "Redigerbar(name='$name', letterDataClass='$letterDataClass', languages=$languages, metadata=$metadata, kategori=$kategori, brevkontekst=$brevkontekst, sakstyper=$sakstyper)"
    }

    enum class Brevkontekst {
        // Saksbrev som også skal være tilgjengelige hvis du kommer inn fra et vedtak
        ALLE,
        // Brev som ikke trenger informasjon fra et vedtak
        SAK,
        // Brev som er knytta til et vedtak. Ikke nødvendigvis vedtaksbrev, kan også være for eksempel varselbrev.
        // Styrer ikke attestering, det blir styrt gjennom brevtype i lettermetadata
        VEDTAK
    }

    interface IBrevkategori {
        fun kode(): String
        val kategoritekst: String
    }
}