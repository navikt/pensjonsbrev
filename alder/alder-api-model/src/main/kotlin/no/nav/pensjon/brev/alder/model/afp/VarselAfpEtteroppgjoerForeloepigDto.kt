package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VarselAfpEtteroppgjoerForeloepigDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VarselAfpEtteroppgjoerForeloepigDto.PesysData> {
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
        val periode: AfpPeriode,
        val toleranseBeloep: Kroner,
    ) : FagsystemBrevdata
}
