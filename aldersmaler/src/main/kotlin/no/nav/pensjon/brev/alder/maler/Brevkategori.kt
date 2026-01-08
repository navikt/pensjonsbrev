package no.nav.pensjon.brev.alder.maler

import no.nav.pensjon.brev.api.model.TemplateDescription

enum class Brevkategori(override val kategoritekst: String) : TemplateDescription.IBrevkategori {
    ETTEROPPGJOER("Etteroppgjør"),
    FEILUTBETALING("Feilutbetaling"),
    FOERSTEGANGSBEHANDLING("Førstegangsbehandling"),
    FRITEKSTBREV("Fritekstbrev"),
    INFORMASJONSBREV("Informasjonsbrev"),
    INNHENTE_OPPLYSNINGER("Innhente opplysninger"),
    KLAGE_OG_ANKE("Klage og anke"),
    LEVEATTEST("Leveattest"),
    OMSORGSOPPTJENING("Omsorgsopptjening"),
    POSTERINGSGRUNNLAG("Posteringsgrunnlag"),
    SLUTTBEHANDLING("Sluttbehandling"),
    UFOEREPENSJON("Uførepensjon"),
    VARSEL("Varsel"),
    VEDTAK_EKSPORT("Vedtak - eksport"),
    VEDTAK_ENDRING_OG_REVURDERING("Vedtak - endring og revurdering"),
    VEDTAK_FLYTTE_MELLOM_LAND("Vedtak - flytte mellom land");

    override fun kode(): String = name
}