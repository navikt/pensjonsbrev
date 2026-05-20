package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    // (rtv-brev brev Vedtaksdata Oppgjorsar)
    val oppgjoersAar: Year,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag PGI)
    // Samlet pensjonsgivende inntekt fra Skatteetaten for oppgjørsåret.
    val pgi: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag IFU)
    // Inntekt opptjent før uttak av AFP. Brukes i scenarier som inkluderer
    // IFU (alle utenom [Scenario.KUN_IEO_REGISTRERT]).
    val ifu: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
    // (rtv-brev brev Grunnlag Persongrunnlag AFPEOGrunnlag IEO)
    // Inntekt opptjent etter opphør av AFP. Brukes i
    // [Scenario.KUN_IEO_REGISTRERT] og [Scenario.IFU_OG_IEO_REGISTRERT].
    val ieo: Kroner,

    // PE_Vedtaksdata_APFEO_IIAP
    // (rtv-brev brev Vedtaksdata APFEO IIAP)
    // Faktisk arbeidsinntekt i perioden med AFP (= PGI − IFU − IEO).
    val iiap: Kroner,

    // PE_Vedtaksdata_AFPEO_fpiberegnet
    // (rtv-brev brev Vedtaksdata AFPEO fpiberegnet)
    // Forventet pensjonsgivende inntekt som ble lagt til grunn da pensjonen
    // ble utbetalt.
    val fpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_AFP_avvik
    // (rtv-brev brev Vedtaksdata AFPEO AFP avvik)
    // Differanse mellom forventet og faktisk pensjonsgivende inntekt. Er
    // mindre enn toleransebeløpet — det er denne testen som gjør at det
    // ikke blir tilbakekreving i dette brevet.
    val avvik: Kroner,

    val scenario: Scenario,
) : AutobrevData {

    /**
     * Scenarier for forklaringen om hvilke nye opplysninger som er lagt fram.
     * Eksstream brukte fem (overlappende) `showIf`-blokker over rådata for
     * `IFUregistrert_STRING` / `IEOregistrert_STRING` / `AFP_Uttaksdato`;
     * her er logikken løftet ut av malen.
     */
    enum class Scenario {
        // IFUregistrert = "" AND IEOregistrert = ""
        // Ingen nye opplysninger registrert — IFU forblir som tidligere
        // beregnet og holdes utenfor etteroppgjøret. Ingen forklaring om
        // faktisk arbeidsinntekt i AFP-perioden vises i dette tilfellet.
        INGEN_NYE_OPPLYSNINGER,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato >= 01.02
        // Bare IFU oppjustert, uttak skjedde i etteroppgjørsåret.
        IFU_UTTAK_I_AARET,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato <= 01.01
        // Bare IFU oppjustert, uttak skjedde før etteroppgjørsåret —
        // inntekten stammer fra tidligere arbeid.
        IFU_UTTAK_FOER_AARET,

        // IFUregistrert != "" AND IEOregistrert != ""
        // Både IFU (før uttak) og IEO (etter opphør) er oppjustert.
        IFU_OG_IEO_REGISTRERT,

        // IEOregistrert = true AND IFUregistrert = false
        // Bare IEO oppjustert — inntekten kom etter at AFP tok slutt.
        KUN_IEO_REGISTRERT,
    }
}
