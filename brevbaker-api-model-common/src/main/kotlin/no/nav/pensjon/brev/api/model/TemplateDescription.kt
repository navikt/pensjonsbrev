package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

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

        override fun hashCode() = name.hashCode() + 31 * letterDataClass.hashCode() + languages.hashCode() + metadata.hashCode()

        override fun toString() = listOf(name, letterDataClass, languages, metadata).joinToString(", ")
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

        override fun hashCode() = name.hashCode() + 31 * letterDataClass.hashCode() + languages.hashCode() + metadata.hashCode() + kategori.hashCode() + brevkontekst.hashCode() + sakstyper.hashCode()

        override fun toString() = listOf(name, letterDataClass, languages, metadata, kategori, brevkontekst, sakstyper).joinToString(", ")
    }

    enum class Brevkontekst { ALLE, SAK, VEDTAK }

    // TODO: Gjer om denne til interface, fleire av desse er pensjonsspesifikke
    enum class Brevkategori {
        ETTEROPPGJOER,
        FOERSTEGANGSBEHANDLING,
        VEDTAK_ENDRING_OG_REVURDERING,
        VEDTAK_FLYTTE_MELLOM_LAND,
        SLUTTBEHANDLING,
        INFORMASJONSBREV,
        VARSEL,
        VEDTAK_EKSPORT,
        OMSORGSOPPTJENING,
        UFOEREPENSJON,
        INNHENTE_OPPLYSNINGER,
        LEVEATTEST,
        FEILUTBETALING,
        KLAGE_OG_ANKE,
        POSTERINGSGRUNNLAG,
        FRITEKSTBREV,
    }
}