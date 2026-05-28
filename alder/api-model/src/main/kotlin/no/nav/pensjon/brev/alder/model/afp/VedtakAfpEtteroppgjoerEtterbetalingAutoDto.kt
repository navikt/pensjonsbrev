package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Vedtak — AFP etteroppgjør med etterbetaling, fase 1 / varsel (autobrev).
 */
data class VedtakAfpEtteroppgjoerEtterbetalingAutoDto(
    val oppgjoersAar: Year,
    val forlitebetalt: Kroner,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val forventetPensjonsgivendeInntektBeregnet: Kroner,
    val fullAfp: Kroner,
    val fradragBeregnetArbeidsInntekt: Kroner,
    val korrigertAfp: Kroner,
    val tidligereArbeidsInntektBeregnet: Kroner,
    val utbetaltAfp: Kroner,
    val uttaksdato: LocalDate,
    val opphorsdato: LocalDate?,
    val medlemAvApotekerordningen: Boolean,
    val toleranseBeloep: Kroner,
    val periode: Periode,
) : AutobrevData {
    /**
     * Periodevariant av forklaringen. Samme inndeling som
     * [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode] (PE_AF_04_107).
     */
    enum class Periode {
        // Uttak < 01.02 AND (Opphor >= 31.12 OR Opphor tom) — AFP løp hele året.
        HEL_AFP_HELE_AARET,

        // Uttak >= 01.02 AND (Opphor >= 31.12 OR Opphor tom) — uttak i året, AFP løper videre.
        UTTAK_I_AARET,

        // Uttak < 01.02 AND Opphor < 31.12 — AFP fra årets start, opphor i året.
        OPPHOER_I_AARET,

        // Uttak >= 01.02 AND Opphor < 31.12 — både uttak og opphør i året.
        UTTAK_OG_OPPHOER_I_AARET,
    }
}
