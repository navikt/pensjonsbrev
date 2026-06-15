package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Redigerbart vedtak — AFP etteroppgjør (offentlig sektor / SPK), ingen endring
 * innenfor toleransebeløpet.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_108`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerToleransebeloepAutoDto] (`PE_AF_04_100`). Begge brev
 * deler innholdsfraser i [no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold].
 */
data class VedtakAfpEtteroppgjoerToleransebeloepDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerToleransebeloepDto.PesysData> {

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
        val periode: Periode,
    ) : FagsystemBrevdata

    /**
     * Periodevariant av forklaringen. Samme inndeling som
     * [VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode] / [VedtakAfpEtteroppgjoerIngenEndringAutoDto.Periode].
     */
    enum class Periode {
        HEL_AFP_HELE_AARET,
        UTTAK_I_AARET,
        OPPHOER_I_AARET,
        UTTAK_OG_OPPHOER_I_AARET,
    }
}