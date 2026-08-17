package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")
data class OpphoerGjenlevendepensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
) : RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata {

    enum class FolketrygdlovenAlternativ(override val displayText: String) : SaksbehandlerValgEnum {
        gifterSeg("Gifter seg"),
        inngaaPartnerskap("Inngår partnerskap"),
        blirSamboerOgHarFellesBarn("Blir samboer og har felles barn"),
        erErSamboerOgFellesBarn("Er samboer og får felles barn"),
        blirSamboerTidligereGift("Blir samboer med personen han/hun tidligere var gift med"),
    }
}