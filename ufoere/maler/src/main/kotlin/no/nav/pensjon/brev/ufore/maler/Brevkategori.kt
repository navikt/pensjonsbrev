package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.IBrevkategori

enum class Brevkategori : IBrevkategori {
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
    VEDTAK_FLYTTE_MELLOM_LAND;

    override val kode = name
}