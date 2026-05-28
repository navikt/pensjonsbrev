package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

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