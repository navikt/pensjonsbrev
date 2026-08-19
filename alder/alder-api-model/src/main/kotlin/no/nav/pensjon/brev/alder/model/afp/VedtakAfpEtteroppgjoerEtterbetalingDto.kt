package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VedtakAfpEtteroppgjoerEtterbetalingDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<VedtakAfpEtteroppgjoerEtterbetalingDto.PesysData> {

    data class PesysData(
        val oppgjoersAar: Year,
        val forlitebetalt: Kroner,
        val pensjonsgivendeInntekt: Kroner,
        val inntektFoerUttak: Kroner,
        val inntektEtterOpphoer: Kroner,
        val inntektIAfpPerioden: Kroner,
        val forventetPensjonsgivendeInntektBeregnet: Kroner,
        val fullAfp: Kroner,
        val fradragBeregnetArbeidsInntekt: Kroner,
        val tidligereArbeidsInntektBeregnet: Kroner,
        val korrigertAfp: Kroner,
        val utbetaltAfp: Kroner,
        val uttaksdato: LocalDate,
        val opphorsdato: LocalDate?,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val periode: AfpPeriode,
    ) : FagsystemBrevdata
}