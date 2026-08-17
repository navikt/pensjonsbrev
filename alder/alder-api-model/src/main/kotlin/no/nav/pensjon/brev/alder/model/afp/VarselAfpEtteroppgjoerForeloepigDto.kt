package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VarselAfpEtteroppgjoerForeloepigDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VarselAfpEtteroppgjoerForeloepigDto.PesysData> {
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
