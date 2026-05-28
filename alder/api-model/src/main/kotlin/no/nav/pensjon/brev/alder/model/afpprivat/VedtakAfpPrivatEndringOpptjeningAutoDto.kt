package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import java.time.LocalDate

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