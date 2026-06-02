package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
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
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: BrevbakerType.Year,
        // PE_Vedtaksdata_AFPEO_forlitebetalt
        val forlitebetalt: BrevbakerType.Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        val pgi: BrevbakerType.Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
        val ifu: BrevbakerType.Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
        val ieo: BrevbakerType.Kroner,
        // PE_Vedtaksdata_APFEO_IIAP
        val iiap: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_FPIberegnet
        val fpiberegnet: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_FullAFP — full AFP uten fradrag for inntekt.
        val fullafp: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_FradragBeregnetAI — inntektsfradraget i AFP.
        val fradragberegnetai: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_TPIberegnet — tidligere arbeidsinntekt brukt i fradragsberegningen.
        val tpiberegnet: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_KorrigertAFP — AFP etter fradrag for ny inntekt.
        val korrigertafp: BrevbakerType.Kroner,
        // PE_Vedtaksdata_AFPEO_UtbetaltAFP — tidligere utbetalt AFP.
        val utbetaltafp: BrevbakerType.Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
        val uttaksdato: LocalDate,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato
        val opphorsdato: LocalDate?,
        val periode: VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode,
    ) : FagsystemBrevdata
}