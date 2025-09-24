package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto.UforeAvslagGrunnbelopPendata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class UforeAvslagInntektDto(
    override val pesysData: UforeAvslagGrunnbelopPendata,
    override val saksbehandlerValg: SaksbehandlervalgGrunnbelop
) : RedigerbarBrevdata<UforeAvslagInntektDto.SaksbehandlervalgGrunnbelop, UforeAvslagGrunnbelopPendata> {

    data class SaksbehandlervalgGrunnbelop(
        @DisplayText("Vis vurdering fra vilkårsvedtak")
        val VisVurderingFraVilkarvedtak: Boolean,
        @DisplayText("Erstatt standardtekst med fritekst")
        val brukVurderingFraVilkarsvedtak: Boolean,
    ) : BrevbakerBrevdata

    data class UforeAvslagGrunnbelopPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String,
        val inntektForUforhet: Int,
        val inntektEtterUforhet: Int,
    ) : BrevbakerBrevdata
}
