package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate


data class VedtakAfpEtteroppgjoerToleransebeloepAutoDto(
    val oppgjoersAar: Year,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val forventetPensjonsgivendeInntektBeregnet: Kroner,
    val avvik: Kroner,
    val uttaksdato: LocalDate,
    val opphorsdato: LocalDate?,
    val periode: Periode,
) : AutobrevData {

    /**
     * Hvilken periodevariant av forklaringen som skal vises. Eksstream brukte
     * fire overlappende `showIf`-blokker over rådata for uttaksdato/opphorsdato
     * mot 01.01 og 31.12 i oppgjørsåret — her er logikken løftet ut av malen.
     */
    enum class Periode {
        // AFP_Uttaksdato < 01.01 AND (Opphorsdato >= 31.12 OR Opphorsdato tom)
        // Hadde AFP hele året; hele inntekten regnes som opptjent samtidig med AFP.
        HEL_AFP_HELE_AARET,

        // AFP_Uttaksdato >= 01.01 AND (Opphorsdato >= 31.12 OR Opphorsdato tom)
        // AFP startet i året, opphørte ikke; standardberegning fordeler inntekten
        // mellom periode før uttak og periode med AFP.
        UTTAK_I_AARET,

        // AFP_Opphorsdato < 31.12 AND Opphorsdato satt AND Uttaksdato < 01.01
        // AFP opphørte i året; standardberegning fordeler inntekten mellom
        // periode med AFP og periode etter opphør.
        OPPHOER_I_AARET,

        // AFP_Uttaksdato >= 01.01 AND Opphorsdato < 31.12 AND Opphorsdato satt
        // Både uttak og opphør i samme år; standardberegning fordeler inntekten
        // mellom periode før uttak, periode med AFP og periode etter opphør.
        UTTAK_OG_OPPHOER_I_AARET,
    }
}
