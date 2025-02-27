package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
interface TemplateDescription {
    val name: String
    val letterDataClass: String
    val languages: List<LanguageCode>
    val metadata: LetterMetadata

    interface Autobrev : TemplateDescription {
        override val name: String
        override val letterDataClass: String
        override val languages: List<LanguageCode>
        override val metadata: LetterMetadata
    }

    interface Redigerbar : TemplateDescription {
        override val name: String
        override val letterDataClass: String
        override val languages: List<LanguageCode>
        override val metadata: LetterMetadata
        val kategori: Brevkategori
        val brevkontekst: Brevkontekst
        val sakstyper: Set<Sakstype>
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