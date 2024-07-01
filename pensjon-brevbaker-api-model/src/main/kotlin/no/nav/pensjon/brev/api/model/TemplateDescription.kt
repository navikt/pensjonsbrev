package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class TemplateDescription(
    val name: String,
    val letterDataClass: String,
    val languages: List<LanguageCode>,
    val metadata: LetterMetadata,
    val kategori: Brevkategori?,
) {
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
