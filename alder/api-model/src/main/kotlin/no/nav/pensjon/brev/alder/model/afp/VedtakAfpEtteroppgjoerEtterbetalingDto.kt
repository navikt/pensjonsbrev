package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Redigerbart vedtak — AFP etteroppgjør (offentlig sektor / SPK) med etterbetaling.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_109`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerEtterbetalingAutoDto] (`PE_AF_04_101`).
 */
data class VedtakAfpEtteroppgjoerEtterbetalingDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerEtterbetalingDto.PesysData> {

    data class PesysData(
        val oppgjoersAar: Year,
        val forlitebetalt: Kroner,
        val pensjonsgivendeInntekt: Kroner,
        val inntektFoerUttak: Kroner,
        val inntektEtterOpphoer: Kroner,
        val inntektIAfpPerioden: Kroner,
        val forventetPensjonsgivendeInntektBeregnet: Kroner,
        // full AFP uten fradrag for inntekt
        val fullAfp: Kroner,
        // inntektsfradraget i AFP
        val fradragBeregnetArbeidsInntekt: Kroner,
        // tidligere arbeidsinntekt brukt i fradragsberegningen
        val tidligereArbeidsInntektBeregnet: Kroner,
        val korrigertAfp: Kroner,
        val utbetaltAfp: Kroner,
        val uttaksdato: LocalDate,
        val opphorsdato: LocalDate?,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val periode: VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode,
    ) : FagsystemBrevdata
}