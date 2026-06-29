package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Varsel (fase 1) om foreløpig beregning av AFP-etteroppgjør (offentlig sektor /
 * Statens pensjonskasse) — redigerbar variant.
 *
 * Konvertert fra Exstream-malen `PE_AF_03_101` (Vedtak_afp_EO_fase1). Brevet er
 * den redigerbare tvillingen til autobrevet [VarselAfpEtteroppgjoerForeloepigAutoDto]
 * (`PE_AF_03_100`) — innholdet er identisk, men her kan saksbehandler redigere
 * teksten i Skribenten. Hele brødteksten deles via frasen
 * `AfpEtteroppgjoerVarselForeloepigInnhold`.
 *
 * Brevet har ingen strukturerte saksbehandlervalg ([EmptySaksbehandlerValg]).
 */
data class VarselAfpEtteroppgjoerForeloepigDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VarselAfpEtteroppgjoerForeloepigDto.PesysData> {

    /**
     * Periodevariant som styrer både tidsrommet i den foreløpige beregningen
     * («du kan ha fått … for mye») og fordelingen av inntekten før/etter uttak
     * og opphør av AFP. Identisk med [VarselAfpEtteroppgjoerForeloepigAutoDto.Periode].
     */
    enum class Periode {
        // Uttaksdato < 01.02 AND (Opphorsdato >= 31.12 OR tom): AFP løp hele året.
        HEL_AFP_HELE_AARET,

        // Uttak i året, ingen opphør: inntekten fordeles før uttak og i AFP-perioden.
        UTTAK_I_AARET,

        // Både uttak og opphør i året: inntekten fordeles før uttak, i AFP-perioden og etter opphør.
        UTTAK_OG_OPPHOER_I_AARET,

        // Opphør i året: inntekten fordeles i AFP-perioden og etter opphør.
        OPPHOER_I_AARET,
    }

    data class PesysData(
        val oppgjoersAar: Year,
        val formyebetalt: Kroner,
        val uttaksdato: LocalDate,
        val opphorsdato: LocalDate?,
        val pensjonsgivendeInntekt: Kroner,
        val inntektFoerUttak: Kroner,
        val inntektEtterOpphoer: Kroner,
        val inntektIAfpPerioden: Kroner,
        val forventetInntekt: Kroner,
        val fullAfp: Kroner,
        val fradragBeregnetArbeidsInntekt: Kroner,
        val korrigertAfp: Kroner,
        val tidligereArbeidsInntektBeregnet: Kroner,
        val utbetaltAfp: Kroner,
        val periode: Periode,
        val toleranseBeloep: Kroner,
    ) : FagsystemBrevdata
}
