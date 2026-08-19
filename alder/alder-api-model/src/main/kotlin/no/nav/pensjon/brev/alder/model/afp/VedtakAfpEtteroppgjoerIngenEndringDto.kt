package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakAfpEtteroppgjoerIngenEndringDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<VedtakAfpEtteroppgjoerIngenEndringDto.PesysData> {

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