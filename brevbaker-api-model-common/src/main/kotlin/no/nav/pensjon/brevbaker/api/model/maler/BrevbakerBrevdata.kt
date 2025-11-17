package no.nav.pensjon.brev.api.model.maler

// Interface for toppnivå-mal-DTO.
sealed interface BrevbakerBrevdata

interface RedigerbarBrevdata<Valg : SaksbehandlerValgBrevdata, Data : FagsystemBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: Valg
    val pesysData: Data
}

interface AutobrevData : BrevbakerBrevdata, FagsystemBrevdata

interface FagsystemBrevdata

interface SaksbehandlerValgBrevdata

interface VedleggData