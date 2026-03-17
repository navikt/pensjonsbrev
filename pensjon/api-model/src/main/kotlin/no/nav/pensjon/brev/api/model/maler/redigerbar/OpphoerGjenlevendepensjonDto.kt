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
        @DisplayText("Velg § 17-11 alternativ")
        val folketrygdlovenAlternativ: FolketrygdlovenAlternativ,
        @DisplayText("Hvis opphør med tilbakekreving")
        val opphoerMedTilbakekreving: Boolean
    ) : SaksbehandlerValgBrevdata {
        enum class FolketrygdlovenAlternativ {
            @DisplayText("Gifter seg")
            gifterSeg,

            @DisplayText("Inngår partnerskap")
            inngaaPartnerskap,

            @DisplayText("Blir samboer og har felles barn")
            blirSamboerOgHarFellesBarn,

            @DisplayText("Er samboer og får felles barn")
            erErSamboerOgFellesBarn,

            @DisplayText("Blir samboer med personen han/hun tidligere var gift med")
            blirSamboerTidligereGift,
        }
    }
}