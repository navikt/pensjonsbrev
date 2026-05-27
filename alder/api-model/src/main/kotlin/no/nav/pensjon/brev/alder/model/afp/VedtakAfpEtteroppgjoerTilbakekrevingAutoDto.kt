package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerTilbakekrevingAutoDto(
    val oppgjoersAar: Year,
    val formyebetalt: Kroner,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val avvik: Kroner,
    val fullAfp: Kroner,
    val fradragBeregnetArbeidsInntekt: Kroner,
    val korrigertAfp: Kroner,
    val tidligereArbeidsInntektBeregnet: Kroner,
    val utbetaltAfp: Kroner,
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
