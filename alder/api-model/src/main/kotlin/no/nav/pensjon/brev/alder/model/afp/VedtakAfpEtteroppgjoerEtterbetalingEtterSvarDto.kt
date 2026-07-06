package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.PesysData> {
    data class PesysData(
        val oppgjoersAar: Year,
        val forlitebetalt: Kroner,
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
        val uttaksdato: LocalDate,
        val opphorsdato: LocalDate?,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val scenario: Scenario,
        val periode: NyPensjonsberegningPeriode,
    ) : FagsystemBrevdata

    /**
     * Hvilken inntektsforklaring som skal vises. Fem gjensidig utelukkende
     * varianter.
     */
    enum class Scenario {
        // IFUregistrert = "" AND IEOregistrert = "" AND uttaksdato >= 01.02
        INGEN_OVERSTYRING_UTTAK_I_AARET,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato >= 01.02
        IFU_OVERSTYRT_UTTAK_I_AARET,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato <= 01.01
        IFU_OVERSTYRT_HEL_AFP,

        // IFUregistrert != "" AND IEOregistrert != ""
        IFU_OG_IEO_OVERSTYRT,

        // IFUregistrert = "" AND IEOregistrert != ""
        KUN_IEO_OVERSTYRT,
    }

    /**
     * Periodevariant av «Ny pensjonsberegning …»-introen. Fire varianter
     * basert på om uttak skjedde før eller i året (vs. 01.02) og om opphør
     * skjedde i året eller etter (vs. 31.12 / tom dato).
     */
    enum class NyPensjonsberegningPeriode {
        // Uttak < 01.02 AND (opphor >= 31.12 OR opphor tom) — AFP løp hele året.
        HELE_AARET,

        // Uttak >= 01.02 AND (opphor >= 31.12 OR opphor tom) — uttak i året, AFP løper videre.
        UTTAK_I_AARET_LOEPENDE,

        // Uttak < 01.02 AND opphor < 31.12 — AFP fra årets start, opphor i året.
        UTTAK_FOER_AARET_OPPHOR_I_AARET,

        // Uttak >= 01.02 AND opphor < 31.12 — både uttak og opphor i året.
        UTTAK_OG_OPPHOR_I_AARET,
    }
}
