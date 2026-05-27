package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
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
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: Year,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        val pgi: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
        val ifu: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
        val ieo: Kroner,
        // PE_Vedtaksdata_APFEO_IIAP
        val iiap: Kroner,
        // PE_Vedtaksdata_AFPEO_FPIberegnet
        val fpiberegnet: Kroner,
        // PE_Vedtaksdata_AFPEO_AFP_avvik
        val avvik: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
        val uttaksdato: LocalDate,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato — null hvis AFP løper videre.
        val opphorsdato: LocalDate?,
        val periode: VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode,
    ) : FagsystemBrevdata
}

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
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: Year,
        // PE_Vedtaksdata_AFPEO_forlitebetalt
        val forlitebetalt: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        val pgi: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
        val ifu: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
        val ieo: Kroner,
        // PE_Vedtaksdata_APFEO_IIAP
        val iiap: Kroner,
        // PE_Vedtaksdata_AFPEO_FPIberegnet
        val fpiberegnet: Kroner,
        // PE_Vedtaksdata_AFPEO_FullAFP — full AFP uten fradrag for inntekt.
        val fullafp: Kroner,
        // PE_Vedtaksdata_AFPEO_FradragBeregnetAI — inntektsfradraget i AFP.
        val fradragberegnetai: Kroner,
        // PE_Vedtaksdata_AFPEO_TPIberegnet — tidligere arbeidsinntekt brukt i fradragsberegningen.
        val tpiberegnet: Kroner,
        // PE_Vedtaksdata_AFPEO_KorrigertAFP — AFP etter fradrag for ny inntekt.
        val korrigertafp: Kroner,
        // PE_Vedtaksdata_AFPEO_UtbetaltAFP — tidligere utbetalt AFP.
        val utbetaltafp: Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
        val uttaksdato: LocalDate,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato
        val opphorsdato: LocalDate?,
        val periode: VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode,
    ) : FagsystemBrevdata
}

/**
 * Redigerbart vedtak — AFP etteroppgjør (offentlig sektor / SPK), ingen endring
 * (andre avvik enn toleransebeløp).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_110`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerIngenEndringAutoDto] (`PE_AF_04_102`).
 */
data class VedtakAfpEtteroppgjoerIngenEndringDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerIngenEndringDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: Year,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        val pgi: Kroner,
        val scenario: VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario,
    ) : FagsystemBrevdata
}
