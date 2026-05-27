package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto(
    val oppgjoersAar: Year,
    val pensjonsgivendeInntekt: Kroner,
    val scenario: Scenario,
) : AutobrevData {

    /**
     * Hvilket av de fire gjensidig utelukkende scenariene som beskriver
     * hvorfor pensjonsberegningen ikke endres. Eksstream uttrykte disse som
     * fire `showIf`-blokker over rådata; her er logikken løftet ut av malen.
     */
    enum class Scenario {
        // PGI < IFU AND IIAP=0 AND FPIberegnet=0 AND UtbetaltAFP=fullAFP
        // AND AFP_Uttaksdato >= Dato0102
        HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK,

        // AFP_Uttaksdato < Dato0102 AND (Opphorsdato >= Dato3112 OR Opphorsdato tom)
        // AND UtbetaltAFP = fullAFP AND PGI = 0
        HEL_AFP_HELE_AARET_INGEN_INNTEKT,

        // UtbetaltAFP = 0 AND PGI >= tidligereArbeidsInntektBeregnet
        IKKE_AFP_FULL_INNTEKT,

        // AFP_Uttaksdato < Dato0102 AND Opphorsdato < Dato3112
        // AND PGI <= 15 100 AND UtbetaltAFP = fullAFP
        HEL_AFP_DELER_AV_AARET,
    }
}
