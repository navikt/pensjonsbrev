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
        val kategori: Brevkategori,
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

    enum class Brevkontekst { ALLE, SAK, VEDTAK }

    // TODO: Gjer om denne til interface, fleire av desse er pensjonsspesifikke
    enum class Brevkategori {
        ETTEROPPGJOER,
        FEILUTBETALING,
        FOERSTEGANGSBEHANDLING,
        FRITEKSTBREV,
        INFORMASJONSBREV,
        INNHENTE_OPPLYSNINGER,
        KLAGE_OG_ANKE,
        LEVEATTEST,
        OMSORGSOPPTJENING,
        POSTERINGSGRUNNLAG,
        SLUTTBEHANDLING,
        UFOEREPENSJON,
        VARSEL,
        VEDTAK_EKSPORT,
        VEDTAK_ENDRING_OG_REVURDERING,
        VEDTAK_FLYTTE_MELLOM_LAND,
    }
}