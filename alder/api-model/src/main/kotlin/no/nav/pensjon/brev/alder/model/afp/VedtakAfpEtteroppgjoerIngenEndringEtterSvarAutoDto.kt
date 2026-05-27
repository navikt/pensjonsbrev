package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto(
    val oppgjoersAar: Year,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val forventetPensjonsgivendeInntektBeregnet: Kroner,
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
