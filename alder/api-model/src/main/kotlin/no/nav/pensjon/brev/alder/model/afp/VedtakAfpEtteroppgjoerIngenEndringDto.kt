package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakAfpEtteroppgjoerIngenEndringDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerIngenEndringDto.PesysData> {

    data class PesysData(
        val oppgjoersAar: BrevbakerType.Year,
        val pensjonsgivendeInntekt: Kroner,
        val inntektFoerUttak: Kroner,
        val inntektEtterOpphoer: Kroner,
        val inntektIAfpPerioden: Kroner,
        val forventetPensjonsgivendeInntektBeregnet: Kroner,
        val avvik: Kroner,
        val uttaksdato: LocalDate,
        val opphorsdato: LocalDate?,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val periode: AfpPeriode,
    ) : FagsystemBrevdata
}