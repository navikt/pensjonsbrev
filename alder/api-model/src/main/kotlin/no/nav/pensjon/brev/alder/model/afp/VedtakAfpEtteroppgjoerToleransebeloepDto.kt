package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
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
        val oppgjoersAar: BrevbakerType.Year,
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
        // PE_Vedtaksdata_AFPEO_AFP_avvik
        val avvik: BrevbakerType.Kroner,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
        val uttaksdato: LocalDate,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato — null hvis AFP løper videre.
        val opphorsdato: LocalDate?,
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