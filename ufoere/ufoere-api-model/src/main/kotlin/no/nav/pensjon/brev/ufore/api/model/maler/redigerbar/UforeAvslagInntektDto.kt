package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

data class UforeAvslagInntektDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagInntektPendata,
) : BrevdataMedSaksbehandlerValg<UforeAvslagInntektDto.UforeAvslagInntektPendata> {

    data class UforeAvslagInntektPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String,
        val uforetidspunkt: LocalDate,
        val uforegrad: Int,
        val inntektForUforhet: Int,
        val inntektEtterUforhet: Int,
        val vurderingIFU: String,
        val vurderingIEU: String,
    ) : FagsystemBrevdata
}
