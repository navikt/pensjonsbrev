package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerTilbakekrevingAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    // (rtv-brev brev Vedtaksdata Oppgjorsar)
    val oppgjoersAar: Year,

    // PE_Vedtaksdata_AFPEO_formyebetalt
    // (rtv-brev brev Vedtaksdata AFPEO formyebetalt)
    // For mye utbetalt AFP — totalbeløpet som kreves tilbake.
    val formyebetalt: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag PGI)
    // Samlet pensjonsgivende inntekt fra Skatteetaten for oppgjørsåret.
    val pgi: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag IFU)
    // Inntekt opptjent før uttak av AFP. Brukes i [Periode.UTTAK_I_AARET] og
    // [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ifu: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag IEO)
    // Inntekt opptjent etter opphør av AFP. Brukes i
    // [Periode.OPPHOER_I_AARET] og [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ieo: Kroner,

    // PE_Vedtaksdata_APFEO_IIAP
    // (rtv-brev brev Vedtaksdata APFEO IIAP)
    // Inntekt antatt opptjent i perioden med AFP (= PGI − IFU − IEO).
    val iiap: Kroner,

    // PE_Vedtaksdata_AFPEO_AFP_avvik
    // (rtv-brev brev Vedtaksdata AFPEO AFP avvik)
    // Forskjellen mellom forventet og faktisk pensjonsgivende inntekt;
    // overstiger toleransebeløpet — det er denne testen som utløser
    // tilbakekrevingen i dette brevet.
    val avvik: Kroner,

    // PE_Vedtaksdata_AFPEO_fullafp
    // (rtv-brev brev Vedtaksdata AFPEO fullafp)
    // Full AFP for oppgjørsåret (uten fradrag for inntekt).
    val fullafp: Kroner,

    // PE_Vedtaksdata_AFPEO_fradragberegnetai
    // (rtv-brev brev Vedtaksdata AFPEO fradragberegnetai)
    // Nytt beregnet inntektsfradrag i AFP basert på ny inntekt.
    val fradragberegnetai: Kroner,

    // PE_Vedtaksdata_AFPEO_korrigertafp
    // (rtv-brev brev Vedtaksdata AFPEO korrigertafp)
    // AFP etter fradrag for den nye inntekten (= fullafp − fradragberegnetai).
    val korrigertafp: Kroner,

    // PE_Vedtaksdata_AFPEO_tpiberegnet
    // (rtv-brev brev Vedtaksdata AFPEO tpiberegnet)
    // Tidligere arbeidsinntekt som ble lagt til grunn ved utbetalingen av
    // pensjonen; nevneren i formelen for inntektsfradraget.
    val tpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_utbetaltafp
    // (rtv-brev brev Vedtaksdata AFPEO utbetaltafp)
    // Tidligere utbetalt AFP for oppgjørsåret før korrigering.
    val utbetaltafp: Kroner,

    val periode: Periode,
) : AutobrevData {

    /**
     * Periodevariant av forklaringen «Du har ikke lagt fram nye opplysninger
     * …». Eksstream brukte fire overlappende `showIf`-blokker over rådata
     * for uttaksdato/opphorsdato mot 01.01 og 31.12 i oppgjørsåret samt
     * IFUBeregnet_ny/IEOBeregnet_ny — her er logikken løftet ut av malen.
     */
    enum class Periode {
        // AFP_Uttaksdato < 01.01 AND (Opphorsdato >= 31.12 OR Opphorsdato tom)
        // AFP løp hele året; hele PGI gir avkorting av AFP.
        HEL_AFP_HELE_AARET,

        // NOT(HEL_AFP_HELE_AARET) AND IFUBeregnet_ny != 0 AND IEOBeregnet_ny == 0
        // AFP startet i året, opphørte ikke; inntekten fordeles mellom periode
        // før uttak og periode med AFP.
        UTTAK_I_AARET,

        // NOT(HEL_AFP_HELE_AARET) AND IFUBeregnet_ny != 0 AND IEOBeregnet_ny != 0
        // Både uttak og opphør i samme år; inntekten fordeles mellom periode
        // før uttak, periode med AFP og periode etter opphør.
        UTTAK_OG_OPPHOER_I_AARET,

        // NOT(HEL_AFP_HELE_AARET) AND IFUBeregnet_ny == 0 AND IEOBeregnet_ny != 0
        // AFP opphørte i året; inntekten fordeles mellom periode med AFP og
        // periode etter opphør.
        OPPHOER_I_AARET,
    }
}
