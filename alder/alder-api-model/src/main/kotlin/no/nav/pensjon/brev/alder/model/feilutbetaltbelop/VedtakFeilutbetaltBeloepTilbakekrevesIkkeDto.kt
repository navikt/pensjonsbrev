package no.nav.pensjon.brev.alder.model.feilutbetaltbelop

import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

@Suppress("unused")
data class VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.PesysData> {

    data class PesysData(
        // PE_VedtaksData_Sakskode (brevet har tekst for alder, afp, barnep, gjenlev og uforep)
        // (rtv-brev brev Vedtaksbrev Vedtaksdata Sakskode)
        val sakstype: Sakstype,

        // PE_Vedtaksdata_Kravhode_KravMottatDato
        // (rtv-brev brev Vedtaksbrev Vedtaksdata Kravhode KravMottatDato)
        val forhaandsvarselDato: LocalDate,

        // PE_VedtaksData_VirkningFOM
        // (rtv-brev brev Vedtaksbrev Vedtaksdata VirkningFom)
        val virkningFom: LocalDate,

        // PE_VedtaksData_VirkningTOM
        // (rtv-brev brev Vedtaksbrev Vedtaksdata VirkningTom)
        val virkningTom: LocalDate,

        // PE_Vedtaksdata_TilbakekrevingTotal_Tilbakekrevtype
        // (rtv-brev brev Vedtaksdata TilbakekrevingTotal TilbakekrevType)
        val hjemmel: Hjemmel,
    ) : FagsystemBrevdata

    enum class Hjemmel {
        // Tilbakekrevtype '22-15-1-1', '22-15-1-2u', '22-15-1-2f', '22-15', '22-15-6' eller 'ULOVFESTET'
        PARAGRAF_22_15,

        // Tilbakekrevtype '22-16'
        PARAGRAF_22_16,
    }
}
