package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class OpphoerGjenlevendepensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyFagsystemdata
) : RedigerbarBrevdata<OpphoerGjenlevendepensjonDto.SaksbehandlerValg, EmptyFagsystemdata> {
    data class SaksbehandlerValg(
        @DisplayText("Gifter seg")
        val gifterSeg: Boolean = false,
        @DisplayText("Inngår partnerskap")
        val inngaaPartnerskap: Boolean = false,
        @DisplayText("Blir samboer og har felles barn")
        val blirSamboerBarn: Boolean = false,
        @DisplayText("Er samboer og får felles barn")
        val erSamboerBarn: Boolean = false,
        @DisplayText("Blir samboer med person han/hun tidligere var gift med")
        val blirSamboerTidligereGift: Boolean = false,
    ) : SaksbehandlerValgBrevdata
}