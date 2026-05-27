package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * AFP-beregning i privat sektor for endringsvedtak.
 *
 * Modellerer feltene under PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp i
 * Exstream-malene `PE_AF_04_113` / `PE_AF_04_114`. Hvert tilleggsbeløp er nullable —
 * `null` betyr at tillegget ikke er innvilget (tilsvarer `AFP*Innvilget = false` i originalen).
 */
data class AfpPrivatBeregningEndring(
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPLivsvarig_AFPLivsvarBrutto
    // (null hvis AFPLivsvarInnvilget = false)
    val livsvarig: Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKronetillegg_AFPKroneBrutto
    val kronetillegg: Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKompensasjonstillegg_AFPKompBrutto
    val kompensasjonstillegg: Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_TotalPensjon
    val sumAfpFoerSkatt: Kroner,
)

/**
 * Auto-vedtak — endring av opptjening for AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_113`. Brevet finnes kun på bokmål
 * i originalen. Den redigerbare varianten av samme situasjon er
 * [VedtakAfpPrivatEndringDto] (`PE_AF_04_114`).
 */
data class VedtakAfpPrivatEndringOpptjeningAutoDto(
    // PE_Vedtaksdata_VirkningFom
    val virkningFom: LocalDate,
    // PE_Vedtaksdata_BrukerAlder
    val brukerAlder: Int,
    val beregning: AfpPrivatBeregningEndring,
    // PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Bostedsland[1] = "nor"
    val borIForNorge: Boolean,
) : AutobrevData

/**
 * Redigerbart vedtak — endring av AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_114`. Bokmål og nynorsk. Auto-varianten
 * er [VedtakAfpPrivatEndringOpptjeningAutoDto] (`PE_AF_04_113`).
 */
data class VedtakAfpPrivatEndringDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpPrivatEndringDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_VirkningFom
        val virkningFom: LocalDate,
        // PE_Vedtaksdata_BrukerAlder
        val brukerAlder: Int,
        val beregning: AfpPrivatBeregningEndring,
        // PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Bostedsland[1] = "nor"
        val borIForNorge: Boolean,
    ) : FagsystemBrevdata
}
